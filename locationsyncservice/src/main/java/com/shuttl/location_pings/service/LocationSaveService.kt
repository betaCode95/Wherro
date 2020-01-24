package com.shuttl.location_pings.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.components.LocationsDB
import com.shuttl.location_pings.custom.notification
import com.shuttl.location_pings.data.model.entity.GPSLocation
import com.shuttl.location_pings.data.repo.LocationRepo
import com.shuttl.locations_sync.R

class LocationSaveService : Service() {

    private var serviceStarted = false
    private val TAG: String = javaClass.name
    private val locManager by lazy { applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private var configs: LocationConfigs = LocationConfigs()
    private val repo by lazy { LocationRepo(LocationsDB.create(applicationContext)?.locationsDao()) }
    private val timer by lazy {
        object : CountDownTimer(configs.timeout.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                stopSelf()
            }
        }
    }
    private val locListener by lazy {
        object : LocationListener {

            override fun onLocationChanged(location: Location?) {
                saveLocation(location)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
                locManager.removeUpdates(this)
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
                    configs.smallIcon ?: R.drawable.ic_loc
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
            timer.cancel()
            locManager.removeUpdates(locListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun work() {
        try {
            timer.start()
            locManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                configs.minTimeInterval.toLong(),
                configs.minDistanceInterval.toFloat(),
                locListener,
                null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "GPS can't be accessed. Asked for permission?")
        }
    }

    private fun saveLocation(location: Location?) {
        repo.addLocation(GPSLocation.create(location), configs.bufferSize)
    }
}