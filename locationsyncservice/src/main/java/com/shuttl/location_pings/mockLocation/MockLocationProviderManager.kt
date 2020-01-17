package com.shuttl.location_pings.mockLocation

import android.content.Context
import android.content.SharedPreferences

class MockLocationProviderManager {

    val PREF_NAME_MOCK_LOCATION_PROVIDER = "mock_location_provider"
    val PREF_KEY_MOCK_LOCATION_LAT = "mock_location_lat"
    val PREF_KEY_MOCK_LOCATION_LNG = "mock_location_lng"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME_MOCK_LOCATION_PROVIDER, Context.MODE_PRIVATE)
    }

    fun getMockLocationProviderLatitude(context: Context?): Float {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getFloat(PREF_KEY_MOCK_LOCATION_LAT, 29.77.toFloat())
    }

    fun getMockLocationProviderLongitude(context: Context?): Float {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getFloat(PREF_KEY_MOCK_LOCATION_LNG, 77.28.toFloat())
    }

    fun setMockLocationProviderLatitude(context: Context?, lat: Double) {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit()
            .putFloat(PREF_KEY_MOCK_LOCATION_LAT, lat.toFloat())
            .apply()
    }

    fun setMockLocationProviderLongitude(context: Context?, lng: Double) {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit()
            .putFloat(PREF_KEY_MOCK_LOCATION_LNG, lng.toFloat())
            .apply()
    }
}