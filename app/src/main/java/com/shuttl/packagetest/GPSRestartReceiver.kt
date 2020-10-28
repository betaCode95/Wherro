package com.shuttl.packagetest

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.shuttl.location_pings.callbacks.LocationPingServiceCallback
import com.shuttl.location_pings.config.open_lib.LocationsHelper
import com.shuttl.location_pings.data.model.entity.GPSLocation
import com.shuttl.location_pings.reciever.RestartReceiver
import com.shuttl.location_pings.service.LocationPingService
import com.shuttl.location_pings.service.LocationSaveService

class GPSRestartReceiver : RestartReceiver() {

    override fun onReceivingAnEvent(context: Context?, intent: Intent?) {
        if (LocationsHelper.isServiceRunning(context, LocationSaveService::class.java) && LocationsHelper.isServiceRunning(context, LocationPingService::class.java)) {
            return
        }
        Toast.makeText(context, "Shuttl: Successfully Restarted", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, LocationPingService::class.java)
        intent.action = "STOP"
        context?.let {
            LocationsHelper.initSilently(
                context = it.applicationContext, callback = callback, intent = intent
            )
        }
    }

    private val callback = object : LocationPingServiceCallback<GPSLocation> {
        override fun afterSyncLocations(locations: List<GPSLocation>?) {
        }

        override fun errorWhileSyncLocations(error: Exception?) {
        }

        override fun serviceStarted() {
        }

        override fun serviceStopped() {
        }

        override fun serviceStoppedManually() {
        }

        override fun beforeSyncLocations(locations: List<GPSLocation>?): List<GPSLocation> {
            return locations?: emptyList()
        }
    }
}