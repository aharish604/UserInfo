package com.mobikasa.userinfo.Utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref(val context: Context) {

    private val PREFS_NAME = "userinfo"
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        object KEY_NAMES{
            const val Device_TOKEN = "device_token"
        }


        fun save(key: String, value: String) {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getValueString(KEY_NAME: String): String? {
            return sharedPref.getString(KEY_NAME, null)
        }

        fun save(key: String, value: Int) {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putInt(key, value)
            editor.apply()
        }


        fun save(key: String, value: Boolean) {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun getValueInt(KEY_NAME: String): Int {
            return sharedPref.getInt(KEY_NAME, 0)
        }



        fun getValueBool(KEY_NAME: String): Boolean {
            return sharedPref.getBoolean(KEY_NAME, false)
        }


        fun clearPref(){
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()
        }

    }


