package com.shuttl.location_pings.config.open_lib

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.shuttl.location_pings.service.MockLocationSaveService

object MockLocationHelper {

    private val TAG: String = MockLocationHelper::class.java.simpleName
    private val serviceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d(TAG, "onServiceDisconnected : Mock location service")
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.d(TAG, "onServiceConnected : Mock location service")
            }
        }
    }

    fun startMockLocationService(app: Application) {
        Log.d(TAG, "startMockLocationService : Start mock location service")
        val mockIntent = Intent(app, MockLocationSaveService::class.java)
        app.startService(mockIntent)
    }

    fun stopMockLocationService(app: Application){
        Log.d(TAG, "stopMockLocationService : Stop mock location service")
        val mockIntent = Intent(app, MockLocationSaveService::class.java)
        app.stopService(mockIntent)
    }
}