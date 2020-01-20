package com.shuttl.location_pings.mockLocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import com.shuttl.location_pings.config.components.LocationConfigs

class MockLocationProvider(applicationContext: Context) {

    private val TAG: String? = "MockLocation"
    private var mockLocationProviderManager = MockLocationProviderManager()
    private var configs: LocationConfigs = LocationConfigs()

    fun addMockLocationProvider(
        mLocationManager: LocationManager,
        context: Context,
        locationListener: LocationListener
    ) {
        addTestProviderToMockAvailableProviders(
            mLocationManager,
            LocationManager.GPS_PROVIDER,
            locationListener
        )
        setMockProviderLocationData(LocationManager.GPS_PROVIDER, context)
        Log.d(TAG, "addMockLocationProvider : Add provider and set mock location latLng")
    }

    @Throws(SecurityException::class)
    private fun addTestProviderToMockAvailableProviders(
        mLocationManager: LocationManager,
        provider: String,
        locationListener: LocationListener
    ) {
        mLocationManager.addTestProvider(
            provider,
            true,
            false,
            false,
            false,
            true,
            false,
            false,
            Criteria.POWER_MEDIUM,
            Criteria.ACCURACY_MEDIUM
        )
        mLocationManager.setTestProviderEnabled(provider, true)
        subscribeMockLocationTracking(mLocationManager, provider, locationListener)
        Log.d(TAG, "addTestProviderToMockAvailableProviders : Add test provider = $provider"
        )
    }

    @SuppressLint("MissingPermission")
    private fun subscribeMockLocationTracking(
        mLocationManager: LocationManager,
        provider: String,
        locationListener: LocationListener
    ) {
        mLocationManager.requestLocationUpdates(provider, configs.minTimeInterval.toLong(), configs.minDistanceInterval.toFloat(), locationListener)
        Log.d(TAG, "subscribeMockLocationTracking : Request for location updates")
    }

    private fun setMockProviderLocationData(provider: String, context: Context) {
        val location = Location(provider)
        location.latitude = mockLocationProviderManager.getMockLocationProviderLatitude(context).toDouble()
        location.longitude = mockLocationProviderManager.getMockLocationProviderLongitude(context).toDouble()
        location.altitude = mockLocationProviderManager.getMockLocationProviderAltitude(context).toDouble()
        location.accuracy = mockLocationProviderManager.getMockLocationProviderAccuracy(context)
        location.bearing = mockLocationProviderManager.getMockLocationProviderBearing(context)
        location.time = System.currentTimeMillis()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            location.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.setTestProviderLocation(provider, location)
        Log.d(TAG, "setMockProviderLocationData : Mock location latLng from SharedPreferences $location"
        )
    }
}
