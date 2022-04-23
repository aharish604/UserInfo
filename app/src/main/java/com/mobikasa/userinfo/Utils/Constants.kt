package com.mobikasa.userinfo.Utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.util.Log

class Constants {


    object BUNDLE_KEYS {
        const val SERVICE_TYPE = "service_type"
    }

    companion object{


        val enc_key="EPySKsJM9tiYA1hKNfVMqIJ6ehya/KuEWcqpKEIR49c="
        val iv_key="vuKeoL5qxBV7N6vlmD4yqg=="

        /* Count is Responsible for the Image Uplod Count*/
        val ImagesCount= 10
        val COntactsCount= 50

        val TAG="FireStore"



        fun setDeviceToken(applicationContext: Context,msg:String)
        { SharedPref(applicationContext).save(SharedPref.KEY_NAMES.Device_TOKEN,msg)
        }

        fun getStoragePath(msg:String):String
        { return "${Collections.StoragePath}$msg/"
        }


        fun getDeviceToken(applicationContext: Context): String? {
            return SharedPref(applicationContext).getValueString(SharedPref.KEY_NAMES.Device_TOKEN)
        }

        fun isMyServiceRunning(serviceClass: Class<*>, mActivity: Activity): Boolean {
            val manager: ActivityManager =
                mActivity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    Log.i("Service status", "Running")
                    return true
                }
            }
            Log.i("Service status", "Not running")
            return false
        }



    }

    object Collections{

        val Name="Name"
        val MobileNumber="MobileNumver"
        val BASE_COLLECTION="Users"

        const val IMAGES: String = "Images"
        const val CONTACTS: String = "Contancts"
        const val IP_Address: String = "IpAddress"
        const val Device_Model: String = "DeviceModel"
        const val Device_Token: String = "DeviceToken"
        const val Time_Stamp: String = "TImeStamp"
       const val ImagePath="ImagePath"
       const val StoragePath: String="/Images/"


    }
    enum class SERVICE {
        START,
        CLOSE
    }





}