package com.shuttl.packagetest

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.shuttl.location_pings.callbacks.LocationPingServiceCallback
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.open_lib.LocationsHelper
import com.shuttl.location_pings.data.model.entity.GPSLocation

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.name
    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val callback = object : LocationPingServiceCallback {
        override fun afterSyncLocations(locations: List<GPSLocation>?) {
            Log.i(TAG, "afterSyncLocations, number of locations synced: " + locations?.size)
        }

        override fun errorWhileSyncLocations(error: String?) {
            Log.i(TAG, "errorWhileSyncLocations$error")
        }

        override fun serviceStarted() {
            Log.i(TAG, "serviceStarted")
        }

        override fun serviceStopped() {
            Log.i(TAG, "serviceStopped")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestLocationPermission()
        LocationsHelper.initLocationsModule(app = application,
            locationConfigs = LocationConfigs(100, 0, 10000, 3, 100, 10, 180000, "", "http://10.191.1.41:3000/record", "", "", R.drawable.ic_loc), callback = callback)
    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            locationPermissions,
            1
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        LocationsHelper.stopAndClearAll(application)
    }
}