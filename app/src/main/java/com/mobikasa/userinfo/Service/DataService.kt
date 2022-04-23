package com.mobikasa.userinfo.Service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.text.format.Formatter
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mobikasa.userinfo.LocationManager
import com.mobikasa.userinfo.MainActivity
import com.mobikasa.userinfo.R
import com.mobikasa.userinfo.UserInfoApp
import com.mobikasa.userinfo.Utils.Constants
import com.mobikasa.userinfo.Utils.TimeUtil
import com.mobikasa.userinfo.db.HeaderEntity
import com.mobikasa.userinfo.db.MyDataBase
import kotlinx.coroutines.*

class DataService: Service() {


    override fun onCreate() {
        super.onCreate()



    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val type = intent?.getSerializableExtra(Constants.BUNDLE_KEYS.SERVICE_TYPE)
        if (type == Constants.SERVICE.START) {

            LocationManager(this).fetchLastDeviceLastLocation(
            object :
                LocationManager.LocationListener {
                override fun onLocationGet(mLocation: Location) {
                    CoroutineScope(Dispatchers.Default).launch { while (isActive) {
                            Log.d(Constants.TAG,"ServiceStarted ")
                           /* val wifiManager = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                            val ipAddress: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
                            val android_id: String = Settings.Secure.getString(applicationContext.contentResolver,
                                Settings.Secure.ANDROID_ID)
                            val model = Build.MANUFACTURER + Build.MODEL
                            val timestamp = TimeUtil().getUTCTime()

                            Log.d(Constants.TAG,"${mLocation.toString()} ")

                            val userinfoTable = HeaderEntity(ipAddress, model, android_id,
                                timestamp
                            )
                            MyDataBase.getInstance(UserInfoApp.applicationContext()).HeaderDao()
                                .insert(userinfoTable)*/
                        Toast.makeText(applicationContext,"${mLocation.toString()}",Toast.LENGTH_SHORT);

                            delay(1 * 60 * 1000L)
                        }
                    }
                }
            })
            createNotificationChanel()
            try {
                startForeground(1,createNotification())
            } catch (exception: Exception) {

            }
        } else {
            stopForeground(true)
        }
        return START_NOT_STICKY


    }




    override fun onDestroy() {
        super.onDestroy()
       LocationManager(this).stopLocation()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChanel() {
        val notificationChannelId = "Location channel id"
        val channelName = "Background Service"
        val chan = NotificationChannel(notificationChannelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0, notificationIntent, 0
            )
        }
        val notification = NotificationCompat.Builder(this, "Location channel id")
            .setContentTitle("Mitr")
            .setContentText("We are checking your offline data in every 1 min.")
            .setSmallIcon(R.mipmap.small_notificaion)
            .setContentIntent(pendingIntent)
        return notification.build()
    }


}