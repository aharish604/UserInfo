package com.mobikasa.userinfo.Utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.mobikasa.userinfo.BuildConfig

class RTPermissions(context: Context) {

        var mContext = context

        companion object{

            const val Group_Permisiion_Request_Code=1000
            const val READ_EXTERNAL_STORAGE_Request_Code=100
            const val READ_CONTACTS_Request_Code=101
            const val ACCESS_FINE_LOCATION_Request_Code=102
            const val ACCESS_BACKGROUND_LOCATION_Request_Code=103

        }


        //=============================================================================CheckSelf Permisiion============================================================

        fun Check_READ_EXTERNAL_STORAGE() = ActivityCompat.checkSelfPermission(
            mContext,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        fun Check_READ_CONTACTS() = ActivityCompat.checkSelfPermission(
            mContext,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        fun Check_ACCESS_FINE_LOCATION() = ActivityCompat.checkSelfPermission(
            mContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        fun Check_ACCESS_BACKGROUND_LOCATION() = ActivityCompat.checkSelfPermission(
            mContext,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


        //========================================================================Check shouldShowRequestPermissionRationale==============================================================================

        fun rationale_READ_EXTERNAL_STORAGE() = ActivityCompat.shouldShowRequestPermissionRationale(
            mContext as Activity,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        fun rationale_READ_CONTACTS_Rationale() = ActivityCompat.shouldShowRequestPermissionRationale(
            mContext as Activity,
            android.Manifest.permission.READ_CONTACTS
        )

        fun rationale_ACCESS_FINE_LOCATION_Rationale() = ActivityCompat.shouldShowRequestPermissionRationale(
            mContext as Activity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        fun rationale_ACCESS_BACKGROUND_LOCATION_Rationale() =
            ActivityCompat.shouldShowRequestPermissionRationale(
                mContext as Activity,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//=================================================================================RequestGroupPermisiions===================================================

        fun requestPermissons() {

            var permissionlist = mutableListOf<String>()

            if (!Check_READ_EXTERNAL_STORAGE()) {
                permissionlist.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (!Check_READ_CONTACTS()) {
                permissionlist.add(android.Manifest.permission.READ_CONTACTS)
            }
            if (!Check_ACCESS_FINE_LOCATION()) {
                permissionlist.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (!Check_ACCESS_FINE_LOCATION()) {
                permissionlist.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
            if (permissionlist.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    mContext as Activity,
                    permissionlist.toTypedArray(),
                    Group_Permisiion_Request_Code)
            }
        }
//============================================================================Request Single Permisiion=========================================================


        fun request_ACCESS_FINE_LOCATION()=ActivityCompat.requestPermissions(mContext as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            ACCESS_FINE_LOCATION_Request_Code)

        fun request_ACCESS_BACKGROUND_LOCATION()=ActivityCompat.requestPermissions(mContext as Activity,
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            ACCESS_BACKGROUND_LOCATION_Request_Code)

        fun request_READ_CONTACTS()=ActivityCompat.requestPermissions(mContext as Activity,
            arrayOf(Manifest.permission.READ_CONTACTS),
            READ_CONTACTS_Request_Code)

        fun request_READ_EXTERNAL_STORAGE()=ActivityCompat.requestPermissions(mContext as Activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_EXTERNAL_STORAGE_Request_Code)



        fun showBackgroundPermissionAlert(mContext:Context,msg:String)
        {
            val arr = msg.split("\\")
            var Contacts=""
            var Storage=""
            var Location=""
            var BackGroundLocation=""
            for(m in arr)
            {
                if(m.isNotEmpty())
                {
                    if(m.equals(Manifest.permission.READ_CONTACTS))
                    {
                        Contacts="Contacts"
                    }else if(m.equals(Manifest.permission.READ_EXTERNAL_STORAGE))
                    {
                        Storage="Storage"
                    }else if(m.equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                    {
                        BackGroundLocation="BackGround Location"

                    }else if(m.equals(Manifest.permission.ACCESS_FINE_LOCATION))
                    {
                        Location="Location"
                    }

                }
            }


            AlertDialog.Builder(mContext).apply {
                setTitle("App Required below Permissions")
                setMessage("$Storage \n$Location \n$Contacts \n$BackGroundLocation")
                setCancelable(false)
                setPositiveButton("Close App",
                    DialogInterface.OnClickListener { dialog, id ->
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    })
                setNegativeButton("Go to Settings",
                    DialogInterface.OnClickListener { dialog, id ->

                        //  RTPermissions.requestBackgroundLocation(this@MainActivity)
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", mContext.packageName, null)
                        intent.data = uri
                        mContext.startActivity(intent)
                    })
            }.create().show()


        }

    }





