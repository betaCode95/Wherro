package com.shuttl.location_pings.config.components

import android.text.TextUtils
import com.shuttl.location_pings.data.api.LocationApi
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object LocationRetrofit {

    private var baseUrl = ""

    private var retrofit: Retrofit? = null

    private val okHttpClient by lazy {
        val b = OkHttpClient.Builder()
        if (networkDebug != null)
            b.addInterceptor(networkDebug)
        b.build()
    }

    var networkDebug: Interceptor? = null

    fun resetRetrofit(baseUrl: String?) {
        if (baseUrl.isNullOrEmpty()) return
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    fun getRetrofitObj(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
        }
        return retrofit
    }

    fun getRetrofitObj(baseUrl: String?): Retrofit? {
        if (baseUrl.isNullOrEmpty()) return retrofit
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
        return retrofit
    }

    fun getLocationApi(baseUrl: String? = null): LocationApi? {
        return getRetrofitObj(baseUrl)?.create(LocationApi::class.java)
    }

}