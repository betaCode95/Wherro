package com.shuttl.location_pings.callbacks

import com.shuttl.location_pings.data.model.entity.GPSLocation

interface LocationPingServiceCallback<T> {
    fun afterSyncLocations(locations: List<GPSLocation>?)
    fun beforeSyncLocations(locations: List<GPSLocation>?, reused: Boolean): List<T>
    fun errorWhileSyncLocations(error: Exception?)
    fun serviceStarted()
    fun serviceStopped()
    fun serviceStoppedManually()
}