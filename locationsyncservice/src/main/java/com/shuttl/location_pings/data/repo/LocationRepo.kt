package com.shuttl.location_pings.data.repo

import android.text.TextUtils
import android.util.Log
import com.shuttl.location_pings.config.components.LocationRetrofit
import com.shuttl.location_pings.data.dao.GPSLocationsDao
import com.shuttl.location_pings.data.model.entity.GPSLocation
import com.shuttl.location_pings.data.model.request.SendLocationRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class LocationRepo(private val locationsDao: GPSLocationsDao?) {

    val TAG: String = javaClass.name
    val timeStamps: LinkedList<String> = LinkedList()

    fun addLocation(location: GPSLocation, bufferSize: Int) = GlobalScope.async(Dispatchers.IO) {

        val rowsCount = locationsDao?.getRowsCount()?: 0
        printLocations()

        if(rowsCount >= bufferSize) {
            var index = rowsCount - bufferSize
            val timeStamp = timeStamps[index]

            while(index > 0) {
                timeStamps.removeFirst()
                index--
            }

            locationsDao?.deleteEntries(timeStamp)
        }
        if(!timeStamps.contains(location.timestamp)) timeStamps.addLast(location.timestamp)
        locationsDao?.addLocation(location)
    }

    fun clearLocations() = GlobalScope.async(Dispatchers.IO) {
        locationsDao?.clearLocations()
    }

    fun syncLocations(apiKey: String = "", url: String = "", entires: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val locations = locationsDao?.locations()
            if (locations != null && locations.isNotEmpty()) {
                try {
                    if (TextUtils.isEmpty(url)) {
                        Log.e(TAG, "No Url Found")
                        return@launch
                    }
                    val response = LocationRetrofit.locationAPI.syncLocation(
                            url,
                            apiKey,
                            "application/json",
                            SendLocationRequestBody.create(locations))
                    if (!response.SequenceNumber.isNullOrEmpty()) {
                        clearLocations()
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

    fun refreshList() = GlobalScope.async(Dispatchers.IO) {
        timeStamps.clear()
        val savedLocations: List<GPSLocation>? = locationsDao?.locations()
        if(savedLocations != null){
            for(gpsLocation in savedLocations){
                timeStamps.addLast(gpsLocation.timestamp)
            }
        }
    }
}