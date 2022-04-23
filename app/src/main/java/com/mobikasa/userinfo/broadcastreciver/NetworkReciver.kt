package com.mobikasa.userinfo.broadcastreciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.mobikasa.userinfo.UserInfoApp

class NetworkReciver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val int=intent?.getBooleanExtra("state",false)

        if(int==true)
        {
            Toast.makeText(UserInfoApp.applicationContext(),"is on",Toast.LENGTH_SHORT)

        }else{
            Toast.makeText(UserInfoApp.applicationContext(),"is off",Toast.LENGTH_SHORT)

        }
    }
}