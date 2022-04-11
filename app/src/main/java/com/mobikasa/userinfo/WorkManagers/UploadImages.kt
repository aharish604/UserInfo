package com.mobikasa.userinfo.WorkManagers

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.work.Constraints
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mobikasa.userinfo.UserInfoApp
import com.mobikasa.userinfo.Utils.Constants
import com.mobikasa.userinfo.Utils.SharedPref
import com.mobikasa.userinfo.db.ImagesEntity
import com.mobikasa.userinfo.db.MyDataBase
import com.mvpmobdealer.aes.Decrypt
import com.mvpmobdealer.aes.Encrypt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.util.ArrayList

class UploadImages(mcOntext:Context,workerParameters: WorkerParameters):Worker(mcOntext,workerParameters) {

    val token=SharedPref(mcOntext).getValueString(SharedPref.KEY_NAMES.Device_TOKEN)
    val mContext: Context = mcOntext;

  private val objDB= MyDataBase.getInstance(UserInfoApp.applicationContext()).ImagesDao()
    var pathlist: ArrayList<String> = ArrayList()

    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference

    override fun doWork(): Result {
        try {

            getImagesFromStorage()

            return Result.success()
        }catch (e:Exception)
        {
            return Result.failure()
        }

    }


    fun getImagesFromStorage() {
        pathlist.clear()
        var gpath: String = Environment.getExternalStorageDirectory().absolutePath
        var spath = "DCIM/Camera"
        var fullpath = File(gpath + File.separator + spath)
        val listAllFiles = fullpath.listFiles()
        var count = 0
        if (listAllFiles != null && listAllFiles.size > 0) {
            for (currentFile in listAllFiles) {
                if (count <= Constants.ImagesCount) {
                    count++
                    if (currentFile.name.endsWith(".jpg")) {
                        /*Insert Images path to DB*/
                        uploadImagesToFireStoreage(currentFile.getAbsolutePath(), currentFile.name)
                    }
                } else {
                    count = 0

                    break;
                }
            }




            }
        }


    fun uploadImagesToFireStoreage(filePath:String,fileName:String) {
        val storage= FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("${token?.let { Constants.getStoragePath(it) }}$fileName")
        val stream = FileInputStream(File("$filePath"))
        val uploadTask = mountainsRef.putStream(stream)
        uploadTask.addOnFailureListener {
                e -> Log.w(Constants.TAG, "Error upload image ", e)
        }.addOnSuccessListener { taskSnapshot ->
            Log.d(Constants.TAG, "Image Upload Sucess ! ${taskSnapshot.metadata.toString()}")

            GlobalScope.launch {

                val path="${token?.let {Constants.getStoragePath(it)}}$fileName"

                objDB.insert(ImagesEntity(path))

             //   Log.d(Constants.TAG, "Enc data: ${Encrypt.startEnc(path,mContext)}")

              //  Log.d(Constants.TAG, "Decription data: ${Decrypt.startDec(Encrypt.startEnc(path,mContext))}")


                uploadURLtoFIreStore("${token?.let {Constants.getStoragePath(it)}}$fileName")
            }



        }

    }

    fun uploadURLtoFIreStore(path:String)
    {

        var str = path
        val oldValue = "/"
        val newValue = "*"
        var docid=str.replace(oldValue, newValue)

        val db = Firebase.firestore
        val user = hashMapOf(
            Constants.Collections.ImagePath to path)
        db.collection(Constants.Collections.BASE_COLLECTION).document("$token").collection(Constants.Collections.IMAGES).document(
             docid) .set(user)
            .addOnSuccessListener {
                Log.d(Constants.TAG, "Images Urls Uploded to Fire Store!")
            }
            .addOnFailureListener {
                    e ->
                Log.w(Constants.TAG, "Error to upload  Images Urls ", e)
            }

    }


}



