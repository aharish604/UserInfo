package com.mobikasa.userinfo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class MyLocationManager(private val mContext: Context) {
    private var mLocation: Location? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null

    companion object {
        const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
        const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
        const val RC_LOCATION_ENABLE = 11111
        fun checkPermission(mContext: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun requestPermission(mContext: Context) {
            ActivityCompat.requestPermissions(
                mContext as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                102
            )
        }

        fun checkGpsStatus(mContext: Context) {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
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

    init {
        mLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(UserInfoApp.applicationContext())
        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest?.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun fetchLastDeviceLastLocation(mLocationListener: LocationListener) {
        //todo create boolean method
        //checkGpsStatus()
        mLocationProviderClient?.lastLocation?.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                mLocation = it.result
                mLocationListener.onLocationGet(mLocation!!)
            } else {
                fetchLastDeviceLastLocation(mLocationListener)
            }
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun fetchUpdatedLocation() {
        //todo create boolean method
        //checkGpsStatus()
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                if ( p0.lastLocation != null && p0.locations.size > 0) {
                    for (location in p0?.locations!!) {
                        mLocation = location
                    }
                    //  mLocationListener.onLocationGet(mLocation!!)
                }
            }
        }
        mLocationProviderClient?.requestLocationUpdates(
            mLocationRequest!!,
            mLocationCallback as LocationCallback,
            Looper.myLooper()!!
        )
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun fetchUpdatedLocation(mLocationListener: LocationListener) {
        //todo create boolean method
        //checkGpsStatus()
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                if ( p0.lastLocation != null && p0.locations.size > 0) {
                    for (location in p0.locations) {
                        mLocation = location
                    }
                    mLocationListener.onLocationGet(mLocation!!)
                }
            }
        }
        mLocationProviderClient?.requestLocationUpdates(
            mLocationRequest!!,
            mLocationCallback as LocationCallback,
            Looper.myLooper()!!
        )

    }


    fun removeLocationUpdate() {
        if (mLocationCallback != null)
            mLocationProviderClient?.removeLocationUpdates(mLocationCallback!!)
    }

    interface LocationListener {
        fun onLocationGet(mLocation: Location)
    }
}
