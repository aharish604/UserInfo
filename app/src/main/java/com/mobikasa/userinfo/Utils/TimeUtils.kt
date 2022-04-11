package com.mobikasa.userinfo.Utils

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class TimeUtil {

    fun getTimeStampForEvent(startTime: Long): Long {
        return try {
            abs(System.currentTimeMillis() - startTime)
        }catch (e:Exception){
            System.currentTimeMillis() - startTime
        }

    }



    fun getUTCTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a")
        sdf.timeZone = TimeZone.getDefault()
        Log.d("date_time","date_time"+sdf.format(Date()))
        return sdf.format(Date())
    }




}