package com.shuttl.location_pings.mockLocation;

import android.content.Context;
import android.content.SharedPreferences;

public class MockLocationProviderManager {

    public static final String PREF_NAME_MOCK_LOCATION_PROVIDER = "mock_location_provider";
    public static final String PREF_KEY_MOCK_LOCATION_LAT = "mock_location_lat";
    public static final String PREF_KEY_MOCK_LOCATION_LNG = "mock_location_lng";


    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME_MOCK_LOCATION_PROVIDER, Context.MODE_PRIVATE);
    }

    public void setMockLocationProviderLatitude(Context context, double lat) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        sharedPreferences.edit()
                .putFloat(PREF_KEY_MOCK_LOCATION_LAT, (float) lat)
                .apply();
    }

    public void setMockLocationProviderLongitude(Context context, double lng) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        sharedPreferences.edit()
                .putFloat(PREF_KEY_MOCK_LOCATION_LNG, (float) lng)
                .apply();
    }

    public float getMockLocationProviderLatitude(Context context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getFloat(PREF_KEY_MOCK_LOCATION_LAT, (float) 11.77);
    }

    public float getMockLocationProviderLongitude(Context context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getFloat(PREF_KEY_MOCK_LOCATION_LNG, (float) 60.28);
    }
}
