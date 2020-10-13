package com.shuttl.location_pings.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import com.shuttl.location_pings.callbacks.LocationPingServiceCallback
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.components.LocationsDB
import com.shuttl.location_pings.config.open_lib.getWakeLock
import com.shuttl.location_pings.config.open_lib.releaseSafely
import com.shuttl.location_pings.custom.notification
import com.shuttl.location_pings.data.repo.LocationRepo
import java.util.*

class LocationPingService : Service() {

    private var configs: LocationConfigs = LocationConfigs()
    private var callback: LocationPingServiceCallback<Any>? = null
    private val customBinder = CustomBinder()

    private var wakeLock: PowerManager.WakeLock? = null

    private val timer by lazy {
        object :
            CountDownTimer(configs.timeout.toLong(), configs.minSyncInterval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                pingLocations()
            }

            override fun onFinish() {
                callback?.serviceStopped()
                stopForeground(true)
            }
        }
    }

    private val longTimer by lazy { Timer() }
    private val timerTask by lazy {
        object : TimerTask() {
            override fun run() {
                pingLocations()
            }
        }
    }

    private fun pingLocations() {
        try {
            LocationRepo(LocationsDB.create(applicationContext)?.locationsDao()).syncLocations(
                configs.xApiKey
                    ?: "",
                configs.syncUrl ?: "",
                configs.batchSize,
                callback
            )
        } catch (e: Exception) {
            Log.e("LocationsHelper", e.toString())
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        configs = intent?.getParcelableExtra("config") ?: LocationConfigs()
        startForeground(
            1,
            notification(
                this,
                "Updating trip details...",
                configs.smallIcon,
                intent?.getParcelableExtra("pendingIntent")
            )
        )
        return customBinder
    }

    override fun onCreate() {

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (configs.timeout <= 0) {
                longTimer.cancel()
                timerTask.cancel()
            } else timer.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action.equals("STOP")) {
            wakeLock?.releaseSafely {}
            callback?.serviceStoppedManually()
        }
        configs = intent?.getParcelableExtra("config") ?: LocationConfigs()
        return START_STICKY
    }

    private fun work() {
        try {
            callback?.serviceStarted()
            if (configs.wakeLock == true) {
                wakeLock = this.getWakeLock()
                wakeLock?.acquire(200 * 60 * 1000L /*200 minutes*/)
            }
            if (configs.timeout <= 0)
                longTimer.schedule(timerTask, 0, configs.minSyncInterval.toLong())
            else timer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setCallbackAndWork(c: LocationPingServiceCallback<Any>?) {
        callback = c
        work()
    }

    inner class CustomBinder : Binder() {
        fun getService(): LocationPingService {
            return this@LocationPingService
        }
    }
}