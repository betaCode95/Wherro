package com.shuttl.location_pings.config.components

import android.text.TextUtils
import com.shuttl.location_pings.data.api.LocationApi
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object LocationRetrofit {

    private var baseUrl: String? = ""

    private var retrofit: Retrofit? = null

    private var networkDebug: Interceptor? = null

    private fun getOkHttpClient(interceptor: Interceptor? = networkDebug): OkHttpClient {
        networkDebug = interceptor
        val b = OkHttpClient.Builder()
        if (interceptor != null)
            b.addInterceptor(interceptor)
        return b.build()
    }

    fun resetRetrofit(baseUrlR: String?, httpClient: Interceptor?) {
        baseUrl = computeBaseUrl(baseUrlR)
        if (baseUrl.isNullOrEmpty()) return
        retrofit = Retrofit.Builder()
            .client(getOkHttpClient(httpClient))
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    fun getRetrofitObj(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .client(getOkHttpClient(networkDebug))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
        }
        return retrofit
    }

    fun getRetrofitObj(baseUrlR: String?): Retrofit? {
        baseUrl = computeBaseUrl(baseUrlR)
        if (baseUrl.isNullOrEmpty()) return retrofit
        retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
        return retrofit
    }

    fun getLocationApi(baseUrlR: String? = null): LocationApi? {
        baseUrl = computeBaseUrl(baseUrlR)
        return getRetrofitObj(baseUrl)?.create(LocationApi::class.java)
    }

    private fun computeBaseUrl(baseUrl: String?): String? {
        if (baseUrl.isNullOrEmpty()) return baseUrl
        else if (baseUrl.endsWith("/")) return baseUrl
        else return "$baseUrl/"
    }

}