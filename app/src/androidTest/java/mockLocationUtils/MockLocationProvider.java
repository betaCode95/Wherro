package mockLocationUtils;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import testUtils.LogUITest;
import testUtils.UiUtils;


public class MockLocationProvider implements SharedPreferences.OnSharedPreferenceChangeListener {
    static String TAG = "MockLocationProvider";
    static String locationProviderName = LocationManager.GPS_PROVIDER;

    static private MockLocationProvider instance = new MockLocationProvider();
    static public MockLocationProvider getInstance() { return instance; }
    private MockLocationProvider() {}

    protected Context mContext;
    protected static LocationManager mLocationManager;
    protected SharedPreferences mPref = null;
    protected int accuracy = 10;

    static public void init(Context context) {
        getInstance()._init(context);
    }
    static public void register() { getInstance()._register(); } //Call these in base test case setup

    static public void unregister() { getInstance()._unregister(); } //Call this in BaseTestCase tear down

    static public void unregisterGPS_PROVIDER() { getInstance()._unregister(); }

    static public Location getLocation() {
        return new Location(locationProviderName);
    }

    protected void _init(Context context) {
        if (mContext != null) {
            LogUITest.error("Inti called twice !");
            throw new AssertionError(TAG+".init called twice!");
        }
        mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        mPref.registerOnSharedPreferenceChangeListener(this);
        accuracy = parseAccuracy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) { //this is how we should pass location data from the test to MockLocationProvider
        if (key.equals("location")) {
            accuracy = parseAccuracy();
        }
    }

    protected void _register() {
        // if the test provider already exists, android handles this fine
        try {
            mLocationManager.addTestProvider(locationProviderName, false, false, false,
                    false, true, true, true, 0, accuracy);
            mLocationManager.setTestProviderEnabled(locationProviderName, true);
        } catch (IllegalArgumentException ex) {
           LogUITest.debug("IllegalArgumentException thrown in _register : " + ex.getMessage());
        }catch (Exception e)
        {
            LogUITest.debug(e.getMessage());
        }
    }

    protected void _unregister() {
        try {
            mLocationManager.removeTestProvider(locationProviderName);
        } catch(Exception ignored) {}
    }



    public static void setMockLocation(testUtils.Location location) {

        UiUtils.safeSleep(3);
        Location mockLocation = new Location(locationProviderName); // a string
        mockLocation.setLatitude(location.getLatitude());  // double
        mockLocation.setLongitude(location.getLongitude());
        mockLocation.setAltitude(location.getAltitude());
        if (location.getSatellite() != -1) {
            Bundle bundle = new Bundle();
            bundle.putInt("satellites", location.getSatellite());
            mockLocation.setExtras(bundle);
        }
        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setAccuracy(location.getAccuracy());

        mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        LogUITest.info("****************************************");
        LogUITest.debug("Provider: "+mockLocation.getProvider());
        //LogUITest.debug("Accuracy is: "+mockLocation.getAccuracy());
        LogUITest.debug("Altitude is: "+mockLocation.getAltitude());
        //LogUITest.debug("Bearing is: "+mockLocation.getBearing());
        //LogUITest.debug("Bearing is: "+mockLocation.getBearingAccuracyDegrees());
        LogUITest.debug("Longitude: "+mockLocation.getLongitude());
        LogUITest.debug("Latitude: "+mockLocation.getLatitude());
        LogUITest.info("****************************************\n");

        mLocationManager.setTestProviderLocation(locationProviderName, mockLocation);

        UiUtils.safeSleep(3);

    }




    protected void _verifyInitiated() {
        if (mContext == null) {
            LogUITest.error("APP CONTEXT IS NULL !");
            throw new AssertionError("CommandDispatcher.init has not been called!");
        }
    }

    private int parseAccuracy() {
        String str = mPref.getString("accuracy", "1");
        int ret;
        try {
            ret = Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            Log.e(TAG, String.format("Invalid accuracy %s. Defaulting to 1", str));
            ret = 1;
        }
        return ret;
    }

}