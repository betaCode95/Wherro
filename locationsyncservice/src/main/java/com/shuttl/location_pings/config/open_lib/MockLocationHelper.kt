package com.shuttl.location_pings.config.open_lib

import android.app.Application
import android.content.Intent
import android.util.Log
import com.shuttl.location_pings.service.MockLocationSaveService
import com.shuttl.location_pings.config.components.LocationConfigs


object MockLocationHelper {

    private val TAG: String = "MockLocation"

    fun initMockLocationsModule(
        app: Application,
        locationConfigs: LocationConfigs
    ) {
        Log.d(TAG, "initMockLocationsModule : init mock location module")
        val saveMockLocationIntent = Intent(app, MockLocationSaveService::class.java)
        saveMockLocationIntent.putExtra("config", locationConfigs)
        app.startService(saveMockLocationIntent)
    }

    fun startMockLocationService(app: Application) {
        Log.d(TAG, "startMockLocationService : Start mock location service")
        val startMockIntent = Intent(app, MockLocationSaveService::class.java)
        app.startService(startMockIntent)
    }

    fun stopMockLocationService(app: Application){
        Log.d(TAG, "stopMockLocationService : Stop mock location service")
        val stopMockIntent = Intent(app, MockLocationSaveService::class.java)
        app.stopService(stopMockIntent)
    }
}