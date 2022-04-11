package com.mobikasa.userinfo

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.WorkManager
import com.mobikasa.userinfo.Utils.RTPermissions
import com.mobikasa.userinfo.WorkManagers.UploadContacts
import com.mobikasa.userinfo.WorkManagers.UploadImages
import com.mobikasa.userinfo.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    val TAG="MainActivity"
    private  lateinit var databinding:ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RTPermissions.requestAllPermission(this,RTPermissions.permissionlist)
        databinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        GlobalScope.launch {
            (application as UserInfoApp).updateHeaderTable()

        }

        databinding.button1.setOnClickListener(View.OnClickListener {
            (application as UserInfoApp).contactsUpoadOneTimeWorkRequest()
        })

        databinding.button2.setOnClickListener(View.OnClickListener {
            (application as UserInfoApp).imageUpoadOneTimeWorkRequest()
        })

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

        }
    }



}