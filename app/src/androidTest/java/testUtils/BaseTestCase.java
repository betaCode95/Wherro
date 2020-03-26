package testUtils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import com.shuttl.location_pings.callbacks.LocationPingServiceCallback;
import com.shuttl.location_pings.config.components.LocationConfigs;
import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.data.model.entity.GPSLocation;
import com.shuttl.location_pings.service.LocationPingService;
import com.shuttl.location_pings.service.LocationSaveService;
import com.shuttl.packagetest.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mockLocationUtils.MockLocationProvider;
import testUtils.mockWebServer.CustomDispatcher;
import testUtils.mockWebServer.DispatcherUtils;
import testUtils.mockWebServer.MockWebUtils;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.fail;

public class BaseTestCase {

    public Location loc1, loc2, loc3, loc4, loc5;
    public static List<Location> mockLocationList = new LinkedList<>();
    public List<GPSLocation> gpsLocationsListFromDatabase;
    public static Map<String, TestConstants.RESPONSE_TYPE> edgeCaseResponses = new HashMap<>();
    public static Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
    public static Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    public static UiDevice mDevice = UiDevice.getInstance(getInstrumentation());
    public LocationConfigs locationConfigs;
    public static int currentBatchSize;

    public static LocationPingServiceCallback locationPingServiceCallback = new LocationPingServiceCallback() {
        @NotNull
        @Override
        public List beforeSyncLocations(@Nullable List list) {
            return list;
        }

        @Override
        public void afterSyncLocations(@Nullable List list) {
        }

        @Override
        public void errorWhileSyncLocations(Exception e) {
            LogUITest.debug("errorWhileSyncLocations : " + e.getMessage());
        }

        @Override
        public void serviceStarted() {
        }

        @Override
        public void serviceStopped() {
        }

        @Override
        public void serviceStoppedManually() {
        }


    };


    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    );


    @Rule
    public TestName testName = new TestName();

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void mainSetUp() throws IOException {

        LogUITest.debug("\n***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****");
        LogUITest.info("\n***** \t\tBEGIN Test: " + testName.getMethodName());
        LogUITest.debug("***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****\n");

        LogUITest.debug("Starting MockWebServer ...........");
        MockWebUtils.startServer();

        LogUITest.debug("Setting Dispatcher ...........");
        DispatcherUtils.setDispacher(new CustomDispatcher());

        LogUITest.debug("Initiating MockLocationProvider ...........");
        MockLocationProvider.init(targetContext); // get app under test context

        LogUITest.debug("Setting MockLocationProvider in Developer Option ...........");
        setMockLocationInDeveloperOption();

        LogUITest.debug("Register MockLocationProvider ...........");
        MockLocationProvider.register();

        LogUITest.debug("Current GPS PipeLine URL : " + TestConstants.GPS_PIPELINE_URL);

    }


    protected static void setMockLocationInDeveloperOption() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mDevice.executeShellCommand("appops set " + "com.shuttl.packagetest" + " android:mock_location allow");
                //mDevice.executeShellCommand("appops set " + getInstrumentation().getTargetContext().getPackageName() + " android:mock_location allow");
            } catch (IOException e) {
                LogUITest.error("Failed to set Mock Location App in developer options");
                LogUITest.debug(e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                LogUITest.error("Failed to set Mock Location App in developer options");
                LogUITest.debug(e.getMessage());
                e.printStackTrace();

            }
        }
    }

    @After
    public void tearDown() {

        LogUITest.debug("Un-Register MockLocationProvider ...........");
        MockLocationProvider.unregister();

        // Stop Services Individually
        UiUtils.stopSaveLocationServiceIfRunning(activityTestRule.getActivity().getApplication());
        UiUtils.stopPingLocationServiceIfRunning(activityTestRule.getActivity().getApplication());

        MockWebUtils.stopServer();

        LogUITest.debug("\n***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****");
        LogUITest.info("\n***** \t\tEND Test: " + testName.getMethodName());
        LogUITest.debug("***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****\n");

    }


    public List<GPSLocation> fetchDataFromDatabase() {

        List<GPSLocation> gpsLocations = new LinkedList<>();
        try {
            gpsLocations = LocationsHelper.INSTANCE.getAllLocations1(activityTestRule.getActivity().getApplication());
        } catch (Exception e) {
            LogUITest.debug(e.getMessage());
        }
        return gpsLocations;
    }


    public void initiateLocationServices(LocationConfigs locationConfigs) {
        currentBatchSize = locationConfigs.getBatchSize();
        Intent intent = new Intent(BaseTestCase.appContext, LocationPingService.class);
        intent.setAction("STOP");
        // Start Both Services Via Init module .
        LogUITest.debug("Starting 'Save Location Service' & 'Ping Location Service Via Init Module'");
        LocationsHelper.INSTANCE.initLocationsModule(activityTestRule.getActivity().getApplication(), null, locationConfigs, BaseTestCase.locationPingServiceCallback, intent);

        if (!UiUtils.isServiceRunning(LocationSaveService.class.getName()) || !UiUtils.isServiceRunning(LocationPingService.class.getName()))
            fail();

        LogUITest.debug("Both Location Services are running");
    }


    public boolean validateDatabaseByComparingLocations(List<Location> mockLocationList) {

        List<GPSLocation> gpsLocationsFromDatabase = fetchDataFromDatabase();

        if (!mockLocationList.isEmpty()) {
            if (mockLocationList.size() == gpsLocationsFromDatabase.size()) {

                for (int i = 0; i < mockLocationList.size(); i++) {
                    if (gpsLocationsFromDatabase.get(i).getLatitude() != mockLocationList.get(i).getLatitude() ||
                            gpsLocationsFromDatabase.get(i).getLongitude() != mockLocationList.get(i).getLongitude() ||
                            gpsLocationsFromDatabase.get(i).getAccuracy() != mockLocationList.get(i).getAccuracy() ||
                            !gpsLocationsFromDatabase.get(i).getProvider().equals(mockLocationList.get(i).getProvider())) {

                        LogUITest.debug("Failed to match all values in database");

                        LogUITest.debug(" --------------------     Expected Params : ---------------------");
                        LogUITest.debug(" Latitude : " + mockLocationList.get(i).getLatitude());
                        LogUITest.debug(" Longitude : " + mockLocationList.get(i).getLongitude());
                        LogUITest.debug(" Provider : " + mockLocationList.get(i).getProvider());
                        LogUITest.debug(" Accuracy : " + mockLocationList.get(i).getAccuracy());

                        LogUITest.debug(" --------------------     Actual Params : ---------------------");
                        LogUITest.debug(" Latitude : " + gpsLocationsFromDatabase.get(i).getLatitude());
                        LogUITest.debug(" Longitude : " + gpsLocationsFromDatabase.get(i).getLongitude());
                        LogUITest.debug(" Provider : " + gpsLocationsFromDatabase.get(i).getProvider());
                        LogUITest.debug(" Accuracy : " + gpsLocationsFromDatabase.get(i).getAccuracy());
                        return false;
                    }
                }

                return true;


            } else {
                LogUITest.debug("Number of locations in database vs locations set by Mockwebserver are different");
                LogUITest.debug("Number of Locations mock location set : " + mockLocationList.size());
                LogUITest.debug("Number of Locations in database : " + gpsLocationsFromDatabase.size());
                return false;

            }
        }

        LogUITest.debug("Have not set any locations. Set Atleast One location using mockLocationServer to compare");
        return false;


    }
}
