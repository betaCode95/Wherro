package com.shuttl.packagetest

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.open_lib.LocationsHelper


class MainActivity : AppCompatActivity() {
    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestLocationPermission()
        LocationsHelper.initLocationsModule(app = application,
            locationConfigs = LocationConfigs(syncUrl = "https://gps.shuttlstage.com/streams/driver/record"))
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