package com.shuttl.location_pings.config.open_lib

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.shuttl.location_pings.service.MockLocationSaveService

object MockLocationHelper {

    private val serviceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }

    fun startMockLocationService(app: Application) {
        val mockIntent = Intent(app, MockLocationSaveService::class.java)
        app.startService(mockIntent)
    }

    fun stopMockLocationService(app: Application){
        val mockIntent = Intent(app, MockLocationSaveService::class.java)
        app.stopService(mockIntent)
    }
}