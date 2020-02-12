package com.shuttl.location_pings.service

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
import com.shuttl.location_pings.mockLocation.MockLocationProvider
import com.shuttl.locations_sync.R

class MockLocationSaveService : Service() {

    private val TAG: String = "MockLocation"
    private val locManager by lazy { applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private var configs: LocationConfigs = LocationConfigs()
    private val mockLocationProvider by lazy { MockLocationProvider() }
    private val repo by lazy { LocationRepo(LocationsDB.create(applicationContext)?.locationsDao()) }
    private var startMockLocationService = false
    private val timer by lazy {
        object : CountDownTimer(configs.timeout.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                stopSelf()
            }
        }
    }

    private val mockLocationListener by lazy {
        object : LocationListener {

            override fun onLocationChanged(location: Location?) {
                if (location != null) {
                    mockLocationProvider.setMockProviderLocationData(
                        LocationManager.GPS_PROVIDER,
                        applicationContext
                    )
                    Log.d(
                        TAG,
                        "onLocationChanged : Update changed location in DB ${location.toString()}"
                    )
                    saveMockLocationInDB(location)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Log.d(TAG, "onStatusChanged : Changed status for $provider = $status")
            }

            override fun onProviderEnabled(provider: String?) {
                Log.d(TAG, "onProviderEnabled : Enabled provider $provider to update mock location")
            }

            override fun onProviderDisabled(provider: String?) {
                locManager.removeUpdates(this)
                Log.d(
                    TAG,
                    "onProviderDisabled : Removed mock location update for provider $provider"
                )
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return Binder()
    }

    override fun onCreate() {
        startForeground(
            1,
            notification(this, "Updating mock location details...", R.drawable.ic_loc, null)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        configs = intent?.getParcelableExtra("config") ?: LocationConfigs()
        if (!startMockLocationService) {
            startMockLocationService = true
            work()
        }
        return START_STICKY
    }

    private fun work() {
        timer.start()
        mockLocationProvider.addMockLocationProvider(
            locManager,
            applicationContext,
            mockLocationListener,
            configs
        )
        Log.d(
            TAG,
            "work : Start mock location provider with config : minTimeInterval= " + configs.minTimeInterval.toLong() + " minDistanceInterval = " + configs.minDistanceInterval.toFloat()
        )
    }

    fun saveMockLocationInDB(location: Location?) {
        if (location != null) {
            Log.d(TAG, " Latitude in DB = " + location.getLatitude())
            Log.d(TAG, " Longitude in DB = " + location.getLongitude())
        }
        repo.addLocation(
            GPSLocation.create(
                location,
                configs.userId ?: "",
                configs.vehicleNumber ?: ""
            ), configs.bufferSize
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            timer.cancel()
            locManager.removeUpdates(mockLocationListener)
        } catch (e: Exception) {
            Log.e(TAG, "onDestroy")
            e.printStackTrace()
        }
    }
}
