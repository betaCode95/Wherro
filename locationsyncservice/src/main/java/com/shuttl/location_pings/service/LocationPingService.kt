package com.shuttl.location_pings.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.components.LocationsDB
import com.shuttl.location_pings.custom.notification
import com.shuttl.location_pings.data.repo.LocationRepo

class LocationPingService : Service() {

    private var configs: LocationConfigs = LocationConfigs()

    private val timer by lazy {
        object :
            CountDownTimer(configs.timeout.toLong() * 1000, configs.minSyncInterval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                pingLocations()
            }

            override fun onFinish() {
                stopSelf()
            }
        }
    }

    private fun pingLocations() {
        try {
            LocationRepo(LocationsDB.create(applicationContext)?.locationsDao()).syncLocations(
                configs.xApiKey
                    ?: "", configs.syncUrl ?: "",
                configs.batchSize
            )
        } catch (e: Exception) {
            Log.d("LocationsHelper", e.toString())
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        configs = intent?.getParcelableExtra("config") ?: LocationConfigs()
        return Binder()
    }

    override fun onCreate() {
        startForeground(1, notification(this, "Updating trip details.."))
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            timer.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        configs = intent?.getParcelableExtra("config") ?: LocationConfigs()
        work()
        return START_STICKY
    }

    private fun work() {
        try {
            timer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}