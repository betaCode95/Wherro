package com.shuttl.location_pings.mockLocation

import android.content.Context
import android.content.SharedPreferences

class MockLocationProviderManager {

    val PREF_NAME_MOCK_LOCATION_PROVIDER = "mock_location_provider"
    val PREF_KEY_MOCK_LOCATION_LAT = "mock_location_lat"
    val PREF_KEY_MOCK_LOCATION_LNG = "mock_location_lng"
    val PREF_KEY_MOCK_LOCATION_ALT = "mock_location_altitude"
    val PREF_KEY_MOCK_LOCATION_ACCURACY = "mock_location_accuracy"
    val PREF_KEY_MOCK_LOCATION_BEARING = "mock_location_bearing"
    val DEFAULT_LAT: Double = 0.0
    val DEFAULT_LNG: Double = 0.0
    val DEFAULT_ALT: Double = 0.0
    val DEFAULT_ACCURACY: Float = 0.0F
    val DEFAULT_BEARING: Float = 0.0F

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME_MOCK_LOCATION_PROVIDER, Context.MODE_PRIVATE)
    }

    fun getMockLocationProviderLatitude(context: Context?): Float {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getFloat(PREF_KEY_MOCK_LOCATION_LAT, DEFAULT_LAT.toFloat())
    }

    fun getMockLocationProviderLongitude(context: Context?): Float {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getFloat(PREF_KEY_MOCK_LOCATION_LNG, DEFAULT_LNG.toFloat())
    }

    fun getMockLocationProviderAltitude(context: Context?): Float {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getFloat(PREF_KEY_MOCK_LOCATION_ALT, DEFAULT_ALT.toFloat())
    }

    fun getMockLocationProviderAccuracy(context: Context?): Float {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getFloat(PREF_KEY_MOCK_LOCATION_ACCURACY, DEFAULT_ACCURACY)
    }

    fun getMockLocationProviderBearing(context: Context?): Float {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getFloat(PREF_KEY_MOCK_LOCATION_BEARING, DEFAULT_BEARING)
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

    fun setMockLocationProviderAltitude(context: Context?, alt: Double) {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit()
            .putFloat(PREF_KEY_MOCK_LOCATION_ALT, alt.toFloat())
            .apply()
    }

    fun setMockLocationProviderAccuracy(context: Context?, accuracy: Float) {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit()
            .putFloat(PREF_KEY_MOCK_LOCATION_ACCURACY, accuracy)
            .apply()
    }

    fun setMockLocationProviderBearing(context: Context?, bearing: Float) {
        requireNotNull(context)
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit()
            .putFloat(PREF_KEY_MOCK_LOCATION_BEARING, bearing)
            .apply()
    }
}