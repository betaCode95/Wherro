package com.shuttl.packagetest

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.open_lib.LocationsHelper
import com.shuttl.location_pings.data.model.entity.GPSLocation
import com.shuttl.location_pings.service.LocationPingService

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.name
    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val callback = object : LocationPingService.LocationPingServiceCallback {
        override fun afterSyncLocations(locations: List<GPSLocation>?) {
            Log.i(TAG, "afterSyncLocations")
        }

        override fun errorWhileSyncLocations(error: String?) {
            Log.i(TAG, "errorWhileSyncLocations")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestLocationPermission()
        LocationsHelper.initLocationsModule(app = application,
            locationConfigs = LocationConfigs(syncUrl = "http://10.191.7.30:3000/record"), callback = callback)
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