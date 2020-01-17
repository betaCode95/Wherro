package com.shuttl.location_pings.config.open_lib

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.components.LocationRetrofit
import com.shuttl.location_pings.config.components.LocationsDB
import com.shuttl.location_pings.data.repo.LocationRepo
import com.shuttl.location_pings.service.LocationPingService
import com.shuttl.location_pings.service.LocationSaveService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor

object LocationsHelper {

    private val serviceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            }
        }
    }

    private fun setNetworkingDebug(inteceptor: Interceptor?) {
        LocationRetrofit.networkDebug = inteceptor
    }

    fun initLocationsModule(app: Application, interceptor: Interceptor? = null, locationConfigs: LocationConfigs) {
        setNetworkingDebug(interceptor)
        val pingIntent = Intent(app, LocationPingService::class.java)
        pingIntent.putExtra("config", locationConfigs)
        val saveIntent = Intent(app, LocationSaveService::class.java)
        saveIntent.putExtra("config", locationConfigs)
        app.startService(pingIntent)
        app.bindService(saveIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun stop(app: Application) {
        val pingIntent = Intent(app, LocationPingService::class.java)
        val saveIntent = Intent(app, LocationSaveService::class.java)
        app.stopService(pingIntent)
        app.stopService(saveIntent)
    }

    fun stopAndClearAll(app: Application) {
        val pingIntent = Intent(app, LocationPingService::class.java)
        val saveIntent = Intent(app, LocationSaveService::class.java)
        app.stopService(pingIntent)
        app.stopService(saveIntent)
        GlobalScope.launch(Dispatchers.IO) {
            LocationRepo(LocationsDB.create(app)?.locationsDao()).clearLocations()
        }
    }
}
