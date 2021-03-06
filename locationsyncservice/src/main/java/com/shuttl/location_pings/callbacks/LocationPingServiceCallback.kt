package com.shuttl.location_pings.callbacks

import com.shuttl.location_pings.data.model.entity.GPSLocation

interface LocationPingServiceCallback {
    fun afterSyncLocations(locations: List<GPSLocation>?)
    fun errorWhileSyncLocations(error: String?)
    fun serviceStarted()
    fun serviceStopped()
}