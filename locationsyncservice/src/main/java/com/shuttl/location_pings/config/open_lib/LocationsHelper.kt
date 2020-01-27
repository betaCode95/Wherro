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

    var callback: LocationPingService.LocationPingServiceCallback? = null
    private val serviceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                (service as LocationPingService.CustomBinder).getService().setCallbackAndWork(callback)
            }
        }
    }
  
    private fun setNetworkingDebug(inteceptor: Interceptor?) {
        LocationRetrofit.networkDebug = inteceptor
    }

    fun initLocationsModule(
        app: Application,
        interceptor: Interceptor? = null,
        locationConfigs: LocationConfigs,
        callback: LocationPingService.LocationPingServiceCallback?
    ) {
        this.callback = callback
        setNetworkingDebug(interceptor)
        val pingIntent = Intent(app, LocationPingService::class.java)
        pingIntent.putExtra("config", locationConfigs)
        val saveIntent = Intent(app, LocationSaveService::class.java)
        saveIntent.putExtra("config", locationConfigs)
        app.startService(saveIntent)
        app.bindService(pingIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun stop(app: Application) {
        val pingIntent = Intent(app, LocationPingService::class.java)
        val saveIntent = Intent(app, LocationSaveService::class.java)
        app.stopService(pingIntent)
        app.stopService(saveIntent)
    }

    fun stopAndClearAll(app: Application) {
        val saveIntent = Intent(app, LocationSaveService::class.java)
        app.stopService(saveIntent)
        app.unbindService(serviceConnection)
        GlobalScope.launch(Dispatchers.IO) {
            LocationRepo(LocationsDB.create(app)?.locationsDao()).clearLocations()
        }
    }

    fun unBindLocationPingService(app: Application) {
        app.unbindService(serviceConnection)
    }

    fun stopLocationPingService(app: Application) {
        val pingIntent = Intent(app, LocationPingService::class.java)
        app.stopService(pingIntent)
    }

    fun stopLocationSaveService(app: Application) {
        val saveIntent = Intent(app, LocationSaveService::class.java)
        app.stopService(saveIntent)
    }

    fun startLocationPingService(
        app: Application,
        interceptor: Interceptor? = null,
        locationConfigs: LocationConfigs
    ) {
        setNetworkingDebug(interceptor)
        val pingIntent = Intent(app, LocationPingService::class.java)
        pingIntent.putExtra("config", locationConfigs)
        app.startService(pingIntent)
    }

    fun startLocationSaveService(app: Application, locationConfigs: LocationConfigs) {
        val saveIntent = Intent(app, LocationSaveService::class.java)
        saveIntent.putExtra("config", locationConfigs)
        app.startService(saveIntent)
    }

    fun clearSavedLocations(app: Application) {
        GlobalScope.launch(Dispatchers.IO) {
            LocationRepo(LocationsDB.create(app)?.locationsDao()).clearLocations()
        }
    }
}
