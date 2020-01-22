package com.shuttl.location_pings.data.repo

import android.location.Location
import android.text.TextUtils
import android.util.Log
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
        printLocations()
    }

    fun clearLocations() = GlobalScope.async(Dispatchers.IO) {
        locationsDao?.clearLocations()
    }

    fun syncLocations(apiKey: String = "", url: String = "", batchSize: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val locations = locationsDao?.getLimitedLocations(batchSize)
            if (locations?.isNotEmpty() == true) {
                try {
                    if (TextUtils.isEmpty(url)) {
                        Log.e(TAG, "No Url Found")
                        return@launch
                    } else {
                        Log.i(TAG, "url: $url")
                    }
                    val response = LocationRetrofit.locationAPI.syncLocation(
                            url,
                            apiKey,
                            "application/json",
                            SendLocationRequestBody.create(locations))
                    if (!response.SequenceNumber.isNullOrEmpty()) {
                        deleteEntries(locations.last().timestamp)

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun printLocations() {
        Log.i(TAG, locationsDao?.locations().toString())
    }

    fun deleteEntries(timeStamp: String) {
        locationsDao?.deleteEntries(timeStamp)
    }
}