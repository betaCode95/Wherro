package com.shuttl.location_pings.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
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

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i("LPing", "Broadcast ${intent?.action}")

            context?.let {
                when (intent?.action) {
                    ACTION_ALARM -> {
                        Log.i("LPing", "Alarm")
                    }
                    else -> return
                }
            }
        }
    }

    private val ACTION_ALARM by lazy { "loc_save_alarm" }


    private fun pingLocations() {
        try {
            LocationRepo(LocationsDB.create(applicationContext)?.locationsDao()).syncLocations(
                configs.xApiKey
                    ?: "",
                configs.syncUrl ?: "",
                configs.batchSize,
                this.applicationContext,
                configs.canReuseLastLocation ?: false,
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
            if (configs.alarm == true) {
                cancelAlarm()
                unregisterReceiver(receiver)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action.equals("STOP")) {
            wakeLock?.releaseSafely {}
            callback?.serviceStoppedManually()
            try {
                if (configs.timeout <= 0) {
                    longTimer.cancel()
                    timerTask.cancel()
                } else timer.cancel()
                if (configs.alarm == true) {
                    cancelAlarm()
                    unregisterReceiver(receiver)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            stopSelf()
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
            if (configs.alarm == true) {
                registerReceiver(receiver, IntentFilter(ACTION_ALARM));
                scheduleAlarm()
            }
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


    private fun getAlarmIntent(): PendingIntent {
        val i = Intent(ACTION_ALARM)
        i.setPackage(packageName)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_IMMUTABLE)        }
        else {
            return PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    fun scheduleAlarm() {
        Log.i("LPing", "Scheduling at alarm ${Date(System.currentTimeMillis() + 30000)}")
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                (getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 5000,
                    getAlarmIntent()
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                (getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExact(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 5000,
                    getAlarmIntent()
                )
            }
            else -> {
                (getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 5000,
                    getAlarmIntent()
                )
            }
        }
    }

    private fun cancelAlarm() {
        (getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(getAlarmIntent())
    }

}