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
import java.lang.Exception

class LocationRepo(private val locationsDao: GPSLocationsDao?) {

    fun addLocation(location: GPSLocation) = GlobalScope.async(Dispatchers.IO) {
        locationsDao?.addLocation(location)
    }

    fun clearLocations() = GlobalScope.async(Dispatchers.IO) {
        locationsDao?.clearLocations()
    }

    fun syncLocations(apiKey: String = "", url: String = "") {
        GlobalScope.launch(Dispatchers.IO) {
            val locations = locationsDao?.locations()
            if (locations != null && locations.isNotEmpty()) {
                try {
                    if (TextUtils.isEmpty(url)) {
                        Log.e("LocationsHelper", "No Url Found")
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

}