package com.shuttl.location_pings.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.shuttl.location_pings.config.components.LocationsDB
import com.shuttl.location_pings.custom.notification
import com.shuttl.location_pings.data.model.entity.GPSLocation
import com.shuttl.location_pings.data.repo.LocationRepo
import com.shuttl.location_pings.mockLocation.MockLocationProvider

class MockLocationSaveService : Service() {

    private val TAG: String = "MockLocation"
    private val locManager by lazy { applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val repo by lazy { LocationRepo(LocationsDB.create(applicationContext)?.locationsDao()) }
    private val locListener by lazy {
        object : LocationListener {

            override fun onLocationChanged(location: Location?) {
                Log.d(TAG, "onLocationChanged : Update changed location in DB ${location.toString()}")
                saveMockLocationInDB(location)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Log.d(TAG, "onStatusChanged : Changed status for $provider = $status")
            }

            override fun onProviderEnabled(provider: String?) {
                Log.d(TAG, "onProviderEnabled : Enabled provider $provider to update mock location")
            }

            override fun onProviderDisabled(provider: String?) {
                locManager.removeUpdates(this)
                Log.d(TAG, "onProviderDisabled : Removed mock location update for provider $provider")
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        startForeground(1, notification(this, "Updating mock location details..."))
        Log.d(TAG, "onCreate : Start foreground service to sync mock location")
        work()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun work() {
        val mMockLocationProvider = MockLocationProvider(applicationContext)
        mMockLocationProvider.addMockLocationProvider(
            locManager,
            applicationContext,
            locListener
        )
        Log.d(TAG, "work : Start mock location provider ")
    }

    fun saveMockLocationInDB(location: Location?) {
        if (location != null) {
            Log.d(TAG, " Latitude in DB = " + location.getLatitude())
            Log.d(TAG, " Longitude in DB = " + location.getLongitude())
        }
        repo.addLocation(GPSLocation.create(location))
    }
}
