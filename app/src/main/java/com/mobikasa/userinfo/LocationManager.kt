package com.mobikasa.userinfo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class LocationManager(var mContext:Context) {

     var locationProviderClient:FusedLocationProviderClient
    lateinit var locationCallback:LocationCallback
    private var mLocation: Location? = null


    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 3000
        fastestInterval = 3000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 5000
    }


    init {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(UserInfoApp.applicationContext())
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun fetchUpdatedLocation(mLocationListener: LocationListener) {
        //todo create boolean method
        //checkGpsStatus()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(mLocationResult: LocationResult) {
                if (mLocationResult.lastLocation != null && mLocationResult.locations.size > 0) {
                    for (location in mLocationResult.locations) {
                        mLocation = location
                    }
                    mLocationListener.onLocationGet(mLocation!!)
                }
            }
        }
        locationProviderClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }




    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun fetchLastDeviceLastLocation(mLocationListener: LocationListener) {
        locationProviderClient?.lastLocation?.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                mLocation = it.result
                mLocationListener.onLocationGet(mLocation!!)
            } else {
                fetchLastDeviceLastLocation(mLocationListener)
            }
        }
    }

    fun stopLocation()
    {
        locationProviderClient.removeLocationUpdates(locationCallback)

    }

    companion object {

        val RC_LOCATION_ENABLE = 11111


        fun checkGpsStatus(mContext: Context) {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(
                    LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                )
                .setAlwaysShow(true)
            val settingsClient =
                LocationServices.getSettingsClient(UserInfoApp.applicationContext())
            settingsClient.checkLocationSettings(builder.build()).addOnCompleteListener() {
                try {
                    val response = it.getResult(ApiException::class.java)
                    //Success
                    Log.d(javaClass.simpleName, "Location is enabled")

                } catch (exception: ApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            val resolvable = exception as ResolvableApiException
                            ActivityCompat.startIntentSenderForResult(
                                mContext as Activity,
                                resolvable.resolution.intentSender,
                                RC_LOCATION_ENABLE,
                                null,
                                0,
                                0,
                                0,
                                null
                            )
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                        }

                    }
                }
            }
        }



    }




    interface LocationListener {
        fun onLocationGet(mLocation: Location)
    }


}