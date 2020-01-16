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
import android.os.IBinder
import android.util.Log
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.components.LocationsDB
import com.shuttl.location_pings.custom.notification
import com.shuttl.location_pings.data.model.entity.GPSLocation
import com.shuttl.location_pings.data.repo.LocationRepo

class LocationSaveService : Service() {


    private val locManager by lazy { applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private var configs: LocationConfigs = LocationConfigs()
    private val repo by lazy { LocationRepo(LocationsDB.create(applicationContext)?.locationsDao()) }
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
        startForeground(1, notification(this, "Updating trip details.."))
        work()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // this makes the service restart if it was stopped by system, when memory gets restored in the system
    }

    override fun onDestroy() {
        super.onDestroy()
        locManager.removeUpdates(locListener)
    }

    @SuppressLint("MissingPermission")
    private fun work() {
        try {
            locManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                configs.minTimeInterval.toLong(),
                configs.minDistanceInterval.toFloat(),
                locListener,
                null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("LocationSave", "GPS can't be accessed. Asked for permission?")
        }
    }

    public fun saveLocation(location: Location?) {
        repo.addLocation(GPSLocation.create(location))
    }

}