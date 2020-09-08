package com.shuttl.location_pings.config.components

import com.shuttl.location_pings.data.api.LocationApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object LocationRetrofit {

    var baseUrl = "https://gps.shuttltech.com/"

    private var retrofit: Retrofit? = null

    fun reset(url: String?) {
        retrofit = if (url.isNullOrEmpty()) {
            Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
        } else {
            Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .build()
        }
    }

    val locationAPI by lazy {
        retrofit?.create(LocationApi::class.java)
    }

    private val okHttpClient by lazy {
        val b = OkHttpClient.Builder()
        if (networkDebug != null)
            b.addInterceptor(networkDebug)
        b.build()
    }

    var networkDebug: Interceptor? = null

}