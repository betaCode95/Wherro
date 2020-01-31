package com.shuttl.location_pings.data.model.request

import com.shuttl.location_pings.data.model.entity.GPSLocation

data class SendLocationRequestBody(val data: List<GPSLocation>? = listOf(), val userId: String? = "", val bookingId: String? = "") {

    companion object {
        fun create(gps: List<GPSLocation>?) = SendLocationRequestBody(data = gps)
    }
}