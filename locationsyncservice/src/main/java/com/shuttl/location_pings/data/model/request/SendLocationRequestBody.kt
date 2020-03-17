package com.shuttl.location_pings.data.model.request


data class SendLocationRequestBody<T>(val data: List<T>? = listOf()) {

    companion object {
        fun<T> create(gps: List<T>?) = SendLocationRequestBody(data = gps)
    }
}