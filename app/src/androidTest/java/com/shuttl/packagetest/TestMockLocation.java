package com.shuttl.packagetest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.shuttl.location_pings.mockLocation.MockLocationProviderManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestMockLocation {

    private static final String TAG = "MockLocation";
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test01(){

        setMockLocation(28.99, 77.33);
        Log.d(TAG, "check001*************");

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setMockLocation(38.99, 87.33);
        Log.d(TAG, "check002*************");

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setMockLocation(48.99, 97.33);
        Log.d(TAG, "check003*************");

        try {
            Thread.sleep(3600000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void setMockLocation(double lat, double lng) {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        MockLocationProviderManager mockLocationProviderManager = new MockLocationProviderManager();
        SharedPreferences sharedPreferences = context.getSharedPreferences(mockLocationProviderManager.getPREF_NAME_MOCK_LOCATION_PROVIDER(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        mockLocationProviderManager.setMockLocationProviderLatitude(context, lat);
        mockLocationProviderManager.setMockLocationProviderLongitude(context, lng);
        editor.commit();
    }
}
