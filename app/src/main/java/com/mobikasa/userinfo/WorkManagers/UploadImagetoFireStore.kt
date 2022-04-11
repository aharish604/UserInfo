package com.mobikasa.userinfo.WorkManagers

import android.content.Context
import android.util.Log
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class UploadImagetoFireStore(mCOntext:Context,parameters: WorkerParameters):Worker(mCOntext,parameters) {

    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val token= SharedPref(mCOntext).getValueString(SharedPref.KEY_NAMES.Device_TOKEN)
    private val objDB= MyDataBase.getInstance(UserInfoApp.applicationContext()).ImagesDao()

    override fun doWork(): Result {
        try {

                uploadurlstoFireStore()


            return Result.success()
        }catch (e:Exception)
        {
            return  Result.failure()
        }



    }

     fun uploadurlstoFireStore() {
        val list: List<ImagesEntity> =objDB.getAll()

        for(url in list) {
            GlobalScope.launch {
                storageRef.child(url.imagePath).downloadUrl.addOnSuccessListener {
                    Log.d(Constants.TAG, "Url Downloaded ! ${it}")
                  //  imageurldao.insert(ImageUrlEntity(it.encodedQuery.toString(),it.toString()))

                    uploadURLtoFIreStore(it.encodedQuery.toString(),it.toString())


                }.addOnFailureListener {
                    Log.d(Constants.TAG, "Url Downloaded ${url.imagePath} Exception! ${it}")

                }

            }
        }

       /* val listurls: List<ImageUrlEntity> =imageurldao.getAll()
        for(url in listurls)
        {
            uploadURLtoFIreStore(url)

        }*/

    }


    fun uploadURLtoFIreStore(id:String,url:String)
    {
        val db = Firebase.firestore
        val user = hashMapOf(
            Constants.Collections.ImagePath to url)

        db.collection(Constants.Collections.BASE_COLLECTION).document("$token").collection(Constants.Collections.IMAGES).document(
            id) .set(user)
            .addOnSuccessListener {
                Log.d(Constants.TAG, "Images Urls Uploded to Fire Store!")
               // imageurldao.delete(imageentity)
            }
            .addOnFailureListener {
                    e ->
                Log.w(Constants.TAG, "Error to upload  Images Urls ", e)
            }

    }


}