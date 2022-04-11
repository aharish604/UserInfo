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
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.mobikasa.userinfo.BuildConfig
import com.mobikasa.userinfo.MainActivity

class RTPermissions {

    companion object {
        val locationRequestCode = 101
        val storageRequestCode = 102
        val contactRequestCode = 103
        val allpermissioncode = 100

        val permissionlist= arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE)


        fun isLocationPermissionGranted(mContext: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        }


        fun isReadStoragePermissionGranted(mContext: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun isReadContactPermissionGranted(mContext: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun requestAllPermission(mContext: Context, permissionlist: Array<String>) {

            ActivityCompat.requestPermissions(
                mContext as Activity,
                permissionlist,
                allpermissioncode
            )


        }

        fun isAllPermissionGranted(mContext: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.READ_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED

                    && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }


        fun requestLocationPermission(mContext: Context) {
            ActivityCompat.requestPermissions(
                mContext as Activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                locationRequestCode
            )
        }

        fun requestReadStoragePermissiion(mContext: Context) {
            ActivityCompat.requestPermissions(
                mContext as Activity,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                ),
                storageRequestCode
            )
        }


        fun requestReadContactPermissiion(mContext: Context) {
            ActivityCompat.requestPermissions(
                mContext as Activity,
                arrayOf(
                    android.Manifest.permission.READ_CONTACTS,
                ),
                contactRequestCode
            )
        }


        fun showPermissionDeniedDialog(mContext: Context,msg:String) {
            AlertDialog.Builder(mContext)
                .setTitle("Permission Denied")
                .setMessage("Allow Below Permissions $msg")
                .setPositiveButton("App Settings",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        // send to app settings if permission is denied permanently
                        mContext.startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                            )
                        )
                    })
                //.setNegativeButton("Cancel",null)
                .setCancelable(false)
                .show()
        }


        fun isPermissionsAllowed(mContext: Context): Boolean {
            return if (ContextCompat.checkSelfPermission(mContext, permissionlist.toString()) != PackageManager.PERMISSION_GRANTED) {
                false
            } else true
        }

        fun askForPermissions(mContext: Context): Boolean {
            if (!isPermissionsAllowed(mContext)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this as Activity,Manifest.permission.READ_EXTERNAL_STORAGE )) {
                 //   showPermissionDeniedDialog(this)
                } else {
                    ActivityCompat.requestPermissions(this as Activity, permissionlist,200)
                }
                return false
            }
            return true
        }



    }




}