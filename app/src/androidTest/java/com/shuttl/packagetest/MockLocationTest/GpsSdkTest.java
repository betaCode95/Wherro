package com.shuttl.packagetest.MockLocationTest;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.shuttl.packagetest.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


@RunWith(AndroidJUnit4.class)
public class statuGpsSdkTest {
    static String TAG = "MockLocationProvider";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void onSetUp() {
        activityTestRule.getActivity();
        Log.d(TAG,"Starting GPS TESTS ");
        MockLocationProvider.init(getInstrumentation().getTargetContext()); // get app under test context
        //MockLocationProvider.unregisterGPS_PROVIDER();
        MockLocationProvider.register();
    }

    @Test
    public void tinyMockGPSTest() {

        safeSleep(5);
        MockLocationProvider.setMockLocation(10, 20);

        safeSleep(5);
        MockLocationProvider.setMockLocation(30, 40);

        safeSleep(5);
        MockLocationProvider.setMockLocation(50, 60, 5000);

    }

    @Test
    public void sequentialChangeTest() {

        Log.d(TAG,"-----------------------------");
        Log.d(TAG,"BEGIN sequentialChangeTest()");
        for(int count =1; count<=10; count++) {
            Log.d(TAG,"The run count is: "+count);
            double latitude = count*11;
            double longitude = count*11;
            MockLocationProvider.setMockLocation(longitude, latitude);
            safeSleep(5);
        }
        Log.d(TAG,"END sequentialChangeTest()");
        Log.d(TAG,"-----------------------------");
    }

    @Test
    public void bigGPSTest() {

        Log.d(TAG,"-------- checkpoint 001 ");

        for (int i=0; i<=10; i++) {

            Log.d(TAG,"The run count is: "+i);
            double latitude = Math.random() * 49 + 1;
            double longitude = Math.random() * 49 + 1;
            double altitude = Math.random() * 600 + 1;

            MockLocationProvider.setMockLocation(longitude, latitude);

            safeSleep(5);

            MockLocationProvider.setMockLocation(longitude, latitude, altitude);
        }
    }

    public static void safeSleep(float seconds) {

        try {
            Thread.sleep((int) seconds * 1000);
        } catch (InterruptedException e) {
            Log.d(TAG,"Failed to sleep for " + seconds + " seconds !");
            e.printStackTrace();
        }
    }




}

