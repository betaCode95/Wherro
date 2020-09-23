package com.shuttl.location_pings.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.*
import android.util.Log
import com.google.android.gms.location.*
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.components.LocationsDB
import com.shuttl.location_pings.custom.notification
import com.shuttl.location_pings.data.model.entity.GPSLocation
import com.shuttl.location_pings.data.repo.LocationRepo

class LocationSaveService : Service() {

    private var serviceStarted = false
    private val TAG: String = javaClass.name
    private val fusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(applicationContext) as FusedLocationProviderClient }
    private var configs: LocationConfigs = LocationConfigs()
    private val repo by lazy { LocationRepo(LocationsDB.create(applicationContext)?.locationsDao()) }
    private val timer by lazy {
        object : CountDownTimer(configs.timeout.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                stopForeground(true)
            }
        }
    }

    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                saveLocation(locationResult.lastLocation)
                Log.d("Shuttl_UITest" , "Location Has Changed Lat FA: " + locationResult.lastLocation.latitude + " Long : " + locationResult.lastLocation.longitude )
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return Binder()
    }

    override fun onCreate() {
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        configs = intent?.getParcelableExtra("config") ?: LocationConfigs()
        if (!serviceStarted) {
            startForeground(
                1,
                notification(
                    this,
                    "Updating trip details...",
                    configs.smallIcon,
                    null
                )
            )
            serviceStarted = true
            work()
        }
        return START_STICKY // this makes the service restart if it was stopped by system, when memory gets restored in the system
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (configs.timeout > 0)
                timer.cancel()
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun work() {
        try {
            if (configs.timeout > 0)
                timer.start()
          setUpLocationListener()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "GPS can't be accessed. Asked for permission?")
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val locationRequest = LocationRequest().setInterval(configs.minTimeInterval.toLong()).setSmallestDisplacement(configs.minDistanceInterval.toFloat())
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private fun saveLocation(location: Location?) {
        repo.addLocation(
            GPSLocation.create(
                location
            ), configs.bufferSize
        )
    }
}