package com.shuttl.location_pings.reciever

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log

abstract class RestartReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "Receiver " + intent?.action)
        onReceivingAnEvent(context, intent)
    }

    // Add your implementation in this
    abstract fun onReceivingAnEvent(context: Context?, intent: Intent?)

}