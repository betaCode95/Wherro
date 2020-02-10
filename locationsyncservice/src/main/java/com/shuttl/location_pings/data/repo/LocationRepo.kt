package com.shuttl.location_pings.data.repo

import android.text.TextUtils
import android.util.Log
import com.shuttl.location_pings.callbacks.LocationPingServiceCallback
import com.shuttl.location_pings.config.components.LocationRetrofit
import com.shuttl.location_pings.data.dao.GPSLocationsDao
import com.shuttl.location_pings.data.model.entity.GPSLocation
import com.shuttl.location_pings.data.model.request.SendLocationRequestBody
import kotlinx.coroutines.*

class LocationRepo(private val locationsDao: GPSLocationsDao?) {

    val TAG: String = javaClass.name

    fun addLocation(location: GPSLocation, bufferSize: Int) = GlobalScope.async(Dispatchers.IO) {
        val rowsCount = locationsDao?.getRowsCount() ?: 0
        if (rowsCount >= bufferSize) {
            locationsDao?.deleteOldestLocation()
        }
        locationsDao?.addLocation(location)
    }

    fun clearLocations() = GlobalScope.async(Dispatchers.IO) {
        locationsDao?.clearLocations()
    }

    fun syncLocations(apiKey: String = "",
                      url: String = "",
                      batchSize: Int,
                      callback: LocationPingServiceCallback?) {
        GlobalScope.launch(Dispatchers.IO) {
            val locations = locationsDao?.getLimitedLocations(batchSize)
            if (locations?.isNotEmpty() == true) {
                try {
                    if (TextUtils.isEmpty(url)) {
                        Log.e(TAG, "No Url Found")
                    }
                    val response = LocationRetrofit.locationAPI.syncLocation(
                        url,
                        apiKey,
                        "application/json",
                        SendLocationRequestBody.create(locations)
                    )
                    if (response.success == true) {
                        deleteEntries(locations.last().time)
                        callback?.afterSyncLocations(locations)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback?.errorWhileSyncLocations(e)
                }

            }
        }
    }

    fun deleteEntries(time: String) {
        locationsDao?.deleteEntries(time)
    }
}