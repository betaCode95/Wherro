package com.shuttl.location_pings.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.*
import android.util.Log
import com.google.android.gms.location.*
import com.shuttl.location_pings.config.components.LocationConfigs
import com.shuttl.location_pings.config.components.LocationsDB
import com.shuttl.location_pings.config.open_lib.getWakeLock
import com.shuttl.location_pings.config.open_lib.releaseSafely
import com.shuttl.location_pings.custom.notification
import com.shuttl.location_pings.data.model.entity.GPSLocation
import com.shuttl.location_pings.data.repo.LocationRepo
import java.util.*

class LocationSaveService : Service() {

    private var serviceStarted = false
    private val TAG: String = javaClass.name
    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            applicationContext
        ) as FusedLocationProviderClient
    }
    private var configs: LocationConfigs = LocationConfigs()
    private val repo by lazy {
        LocationRepo(
            LocationsDB.create(applicationContext)?.locationsDao()
        )
    }
    private val timer by lazy {
        object : CountDownTimer(configs.timeout.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                try {
                    wakeLock?.releaseSafely {}
                } catch (e: Exception) {
                    Log.e("ERROR", e.toString())
                }
                stopForeground(true)
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i(TAG, "Broadcast ${intent?.action}")
            scheduleAlarm()
            context?.let {
                when (intent?.action) {
                    ACTION_ALARM -> {
                        Log.i(TAG, "Alarm")
                    }
                    else -> return
                }
            }
        }
    }

    private val ACTION_ALARM by lazy { "alarm" }

    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                saveLocation(locationResult.lastLocation)
                Log.d(
                    "Shuttl_UITest",
                    "Location Has Changed Lat FA: " + locationResult.lastLocation.latitude + " Long : " + locationResult.lastLocation.longitude
                )
            }
        }
    }

    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent?): IBinder? {
        return Binder()
    }

    override fun onCreate() {
        registerReceiver(receiver, IntentFilter(ACTION_ALARM));
        scheduleAlarm()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        configs = intent?.getParcelableExtra("config") ?: LocationConfigs()
        startForeground(
            1,
            notification(
                this,
                "Updating trip details...",
                configs.smallIcon,
                null
            )
        )
        if (!serviceStarted) {
            serviceStarted = true
            work()
        }
        return START_STICKY // this makes the service restart if it was stopped by system, when memory gets restored in the system
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            wakeLock?.releaseSafely {}
            if (configs.timeout > 0)
                timer.cancel()
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            cancelAlarm()
            unregisterReceiver(receiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun work() {
        try {
            if (configs.wakeLock == true) {
                wakeLock = this.getWakeLock()
                wakeLock?.acquire(200 * 60 * 1000L /*200 minutes*/)
            }
            if (configs.timeout > 0)
                timer.start()
            setUpLocationListener()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "GPS can't be accessed. Asked for permission?")
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val locationRequest = LocationRequest().setInterval(configs.minTimeInterval.toLong())
            .setSmallestDisplacement(configs.minDistanceInterval.toFloat())
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private fun saveLocation(location: Location?) {
        repo.addLocation(
            GPSLocation.create(
                location
            ), configs.bufferSize
        )
    }

    private fun getAlarmIntent(): PendingIntent {
        val i = Intent(ACTION_ALARM)
        i.setPackage(packageName)
        return PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun scheduleAlarm() {
        Log.i(TAG, "Scheduling at alarm ${Date(System.currentTimeMillis() + 30000)}")
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                (getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 3000,
                    getAlarmIntent()
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                (getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExact(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 3000,
                    getAlarmIntent()
                )
            }
            else -> {
                (getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + 3000,
                    getAlarmIntent()
                )
            }
        }
    }

    private fun cancelAlarm() {
        (getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(getAlarmIntent())
    }
}