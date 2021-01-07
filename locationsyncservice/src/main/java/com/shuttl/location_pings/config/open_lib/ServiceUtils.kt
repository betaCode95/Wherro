package com.shuttl.location_pings.config.open_lib

import android.app.Service
import android.content.Context
import android.os.PowerManager
import android.util.Log

fun PowerManager.WakeLock.releaseSafely(lmb: () -> Unit) {
    try {
        this.let {
            if (it.isHeld) {
                it.release()
            }
        }
        lmb()
    } catch (e: Exception) {
        Log.e("Error", e.toString())
    }
}

fun Service.getWakeLock(): PowerManager.WakeLock? {
    return (this.getSystemService(Context.POWER_SERVICE) as? PowerManager)?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.packageName ?: "")
}