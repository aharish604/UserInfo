package com.mobikasa.userinfo.WorkManagers

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.text.format.Formatter
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobikasa.userinfo.UserInfoApp
import com.mobikasa.userinfo.Utils.Constants
import com.mobikasa.userinfo.Utils.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DemoWorkManager(mCOntext:Context,parameters:WorkerParameters):Worker(mCOntext,parameters) {

    var context=mCOntext
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {

        updateHeaderTable()


        return Result.success()

    }

    fun updateHeaderTable(){
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        val android_id: String = Settings.Secure.getString(applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID)
        val model = Build.MANUFACTURER + Build.MODEL
        val timestamp = TimeUtil().getUTCTime()

        CoroutineScope(IO).launch {
            postHeaderTable(ipAddress,model, android_id,timestamp)
        }


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

}