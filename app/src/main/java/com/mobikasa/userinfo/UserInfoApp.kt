package com.mobikasa.userinfo

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues.TAG
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.provider.ContactsContract
import android.provider.Settings
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LifecycleOwner
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.WorkManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobikasa.userinfo.Utils.Constants
import com.mobikasa.userinfo.Utils.RTPermissions
import com.mobikasa.userinfo.Utils.SharedPref
import com.mobikasa.userinfo.Utils.TimeUtil
import com.mobikasa.userinfo.WorkManagers.UploadContacts
import com.mobikasa.userinfo.WorkManagers.UploadImages
import com.mobikasa.userinfo.db.*
import java.io.File
import java.net.URI.create
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class UserInfoApp:Application() {

    init {
        instance = this
    }





    companion object {
        private var instance: UserInfoApp? = null
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateHeaderTable(){
        val db = Firebase.firestore
        val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        val android_id: String = Settings.Secure.getString(applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID)
        val model = Build.MANUFACTURER + Build.MODEL




        val timestamp = TimeUtil().getUTCTime()

        Constants.setDeviceToken(applicationContext(),android_id)

        insertHeaderTable(ipAddress,model, android_id)

        postHeaderTable(ipAddress,model, android_id,timestamp)

    }

    fun insertHeaderTable(ipAddress:String, model:String, devicetoken:String)
    {
        val userinfoTable = HeaderEntity(ipAddress, model, devicetoken)
        MyDataBase.getInstance(UserInfoApp.applicationContext()).HeaderDao()
            .insert(userinfoTable)
    }

    fun postHeaderTable(ipAddress:String, model:String, devicetoken:String,timeStamp:String)
    {
        val db=Firebase.firestore

        val user = hashMapOf(
            Constants.Collections.IP_Address to "$ipAddress",
            Constants.Collections.Device_Model to "$model",
            Constants.Collections.Device_Token to "$devicetoken",
            Constants.Collections.Time_Stamp to "$timeStamp"
        )

        db.collection(Constants.Collections.BASE_COLLECTION).document("$devicetoken")
            .set(user)
            .addOnSuccessListener {
                Log.d(Constants.TAG, "Header Table Updated Sucessfully!") }
            .addOnFailureListener {
                    e -> Log.w(Constants.TAG, "Header Table Failed to Upload!", e) }

    }


     fun imageUpoadOneTimeWorkRequest()
    { val workmanager= WorkManager.getInstance(applicationContext)
        val constraint= Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val imageuploadonetimeworkreques= androidx.work.OneTimeWorkRequest.Builder(UploadImages::class.java).setConstraints(constraint).build()
        workmanager.enqueue(imageuploadonetimeworkreques)

    }

     fun contactsUpoadOneTimeWorkRequest()
    {   val workmanager= WorkManager.getInstance(applicationContext)
        val constraint= Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val contactsloadonetimeworkreques= androidx.work.OneTimeWorkRequest.Builder(UploadContacts::class.java).setConstraints(constraint).build()
        workmanager.enqueue(contactsloadonetimeworkreques)
    }


}