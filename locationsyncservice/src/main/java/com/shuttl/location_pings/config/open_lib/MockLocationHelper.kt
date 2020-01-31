package com.shuttl.location_pings.config.open_lib

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.shuttl.location_pings.callbacks.LocationPingServiceCallback
import com.shuttl.location_pings.service.MockLocationSaveService
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.service.LocationPingService


object MockLocationHelper {

    private val TAG: String = "MockLocation"
    var callback: LocationPingServiceCallback? = null
    private val serviceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                (service as LocationPingService.CustomBinder).getService().setCallbackAndWork(callback)
            }
        }
    }

    fun initMockLocationsModule(
        app: Application,
        locationConfigs: LocationConfigs,
        callback: LocationPingServiceCallback?
    ) {
        this.callback = callback
        val pingIntent = Intent(app, LocationPingService::class.java)
        pingIntent.putExtra("config", locationConfigs)
        val saveMockLocationIntent = Intent(app, MockLocationSaveService::class.java)
        saveMockLocationIntent.putExtra("config", locationConfigs)
        app.startService(saveMockLocationIntent)
        app.bindService(pingIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        Log.d(TAG, "initMockLocationsModule : init mock location module")
    }

    fun startMockLocationService(app: Application, locationConfigs: LocationConfigs) {
        Log.d(TAG, "startMockLocationService : Start mock location service")
        val startMockIntent = Intent(app, MockLocationSaveService::class.java)
        startMockIntent.putExtra("config", locationConfigs)
        app.startService(startMockIntent)
    }

    fun stopMockLocationService(app: Application){
        Log.d(TAG, "stopMockLocationService : Stop mock location service")
        val stopMockIntent = Intent(app, MockLocationSaveService::class.java)
        app.stopService(stopMockIntent)
    }
}