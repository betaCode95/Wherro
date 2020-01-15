package com.shuttl.location_pings.mockLocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.shuttl.location_pings.service.LocationSaveService;

public class MockLocationProvider {

    private static final String TAG = "MockLocationProvider";

    private LocationListener mockLocationListener;
    MockLocationProviderManager mockLocationProviderManager = new MockLocationProviderManager();
    private Context context;


    public MockLocationProvider(Context context) {
        Log.d(TAG, "Inside MockLocationProvider");
        this.context = context;
    }

    public void addMockLocationProvider(LocationManager mLocationManager, Context context, final LocationListener onLocationListener) {
        addTestProviderToMockAvailableProviders(mLocationManager, LocationManager.GPS_PROVIDER, onLocationListener);
        setMockProviderLocationData(LocationManager.GPS_PROVIDER, context);
    }

    private void addTestProviderToMockAvailableProviders(LocationManager mLocationManager, String provider, final LocationListener onReceiveLocationListener) throws SecurityException {
        mLocationManager.addTestProvider(provider,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                Criteria.POWER_MEDIUM,
                Criteria.ACCURACY_MEDIUM);
        mLocationManager.setTestProviderEnabled(provider, true);

        subscribeMockLocationTracking(mLocationManager, onReceiveLocationListener, provider);
    }

    @SuppressLint("MissingPermission")
    public void subscribeMockLocationTracking(LocationManager mLocationManager, final LocationListener onReceiveLocationListener, String provider) {
        mockLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

//                new LocationSaveService().saveLocation(location);
                Log.d(TAG, "Mock location onLocationChanged");

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        mLocationManager.requestLocationUpdates(provider, 100, 0, mockLocationListener);
    }

    private void setMockProviderLocationData(String provider, Context context) {
        Location location = new Location(provider);
        location.setLatitude(mockLocationProviderManager.getMockLocationProviderLatitude(context));
        location.setLongitude(mockLocationProviderManager.getMockLocationProviderLongitude(context));
        location.setAltitude(634.61);
        location.setAccuracy(20);
        location.setBearing(0F); // route updates will be accurate

        location.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        Log.d(TAG, location.toString());
        //provide the new location
        LocationManager locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.setTestProviderLocation(provider, location);
    }

    public void RemovesMockLocationProvider(Context context, LocationManager mLocationManager) {
        if (mockLocationListener != null) {
            mLocationManager.removeUpdates(mockLocationListener);
        }
    }
}
