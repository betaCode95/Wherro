package saveServiceTests;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.shuttl.location_pings.callbacks.LocationPingServiceCallback;
import com.shuttl.location_pings.config.components.LocationConfigs;
import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.data.model.entity.GPSLocation;
import com.shuttl.location_pings.service.LocationPingService;
import com.shuttl.location_pings.service.LocationSaveService;
import com.shuttl.packagetest.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import testUtils.AssertUtils;
import testUtils.BaseTestCase;
import testUtils.LogUITest;
import testUtils.TestConstants;
import testUtils.UiUtils;

@RunWith(AndroidJUnit4.class)
public class StartStopService extends BaseTestCase {

    private LocationConfigs locationConfigs;
    private LocationPingServiceCallback locationPingServiceCallback = new LocationPingServiceCallback() {
        @Override
        public void errorWhileSyncLocations(@org.jetbrains.annotations.Nullable String error) {

        }

        @Override
        public void errorWhileSyncLocations(@org.jetbrains.annotations.Nullable Exception error) {

        }

        @Override
        public void afterSyncLocations(@Nullable List<GPSLocation> locations) {
            assert locations != null;
            Log.i("StartStopService", "afterSyncLocations, number of locations synced: " + locations.size());

        }

        @Override
        public void serviceStarted() {
            Log.i("StartStopService", "serviceStarted");

        }

        @Override
        public void serviceStopped() {
            Log.i("StartStopService", "serviceStopped");

        }
    };

    @Before
    public void setUp() {


        // Set config
        locationConfigs =
                new LocationConfigs(10000, 100
                        , 10000, 3, 100, 10, 1800000
                        , "", TestConstants.GPS_PIPELINE_URL, TestConstants.USER_ID
                        , TestConstants.VEHICLE_NUMBER, R.drawable.ic_loc);

    }


    @Test
    public void verifyStartStopLocationServicesViaInitModule() {

        // Start Both Services Via Init module .
        LogUITest.debug("Starting 'Save Location Service' & 'Ping Location Service Via Init Module'");
        LocationsHelper.INSTANCE.initLocationsModule(activityTestRule.getActivity().getApplication(), null, locationConfigs, locationPingServiceCallback);


        boolean isLocationSaveServiceRunning = UiUtils.isServiceRunning(LocationSaveService.class.getName());

        AssertUtils.assertTrueV(isLocationSaveServiceRunning,
                "Failed to start 'Save Location Service'  Via Init Module",
                "Successfully started 'Save Location Service'  Via Init Module");

        boolean isLocationPingServiceRunning = UiUtils.isServiceRunning(LocationPingService.class.getName());

        AssertUtils.assertTrueV(isLocationPingServiceRunning,
                "Failed to start 'Ping Location Service' Via Init Module",
                "Successfully started 'Ping Location Service'  Via Init Module");


        // ----------------------------------------------------------------------------------------------------------------------------------


        LogUITest.debug("Stopping 'Save Location Service' & 'Ping Location Service Via Init Module'");
        LocationsHelper.INSTANCE.stop(activityTestRule.getActivity().getApplication());

        isLocationSaveServiceRunning = UiUtils.isServiceRunning(LocationSaveService.class.getName());

        AssertUtils.assertTrueV(!isLocationSaveServiceRunning,
                "Failed to stop 'Save Location Service'  Via Init Module",
                "Successfully stopped 'Save Location Service'  Via Init Module");

        isLocationPingServiceRunning = UiUtils.isServiceRunning(LocationPingService.class.getName());

        AssertUtils.assertTrueV(!isLocationPingServiceRunning,
                "Failed to stop 'Ping Location Service' Via Init Module",
                "Successfully stopped 'Ping Location Service'  Via Init Module");


    }


    @Test
    public void verifyStartStopLocationPingService() {

        // Start Location Ping Service
        LogUITest.debug("Starting 'Ping Location Service'");
        LocationsHelper.INSTANCE.startLocationPingService(activityTestRule.getActivity().getApplication(), null, locationConfigs);

        boolean isLocationSaveServiceRunning = UiUtils.isServiceRunning(LocationPingService.class.getName());

        AssertUtils.assertTrueV(isLocationSaveServiceRunning,
                "Failed to start 'Ping Location Service'",
                "Successfully started 'Ping Location Service'");


        // Stop Location Ping Service
        LogUITest.debug("Stopping 'Ping Location Service'");
        LocationsHelper.INSTANCE.unBindLocationPingService(activityTestRule.getActivity().getApplication());
        LocationsHelper.INSTANCE.stopLocationPingService(activityTestRule.getActivity().getApplication());

        boolean isServiceRunning = UiUtils.isServiceRunning(LocationPingService.class.getName());

        AssertUtils.assertTrueV(!isServiceRunning,
                "Failed to stop 'Ping Location Service'",
                "Successfully stopped 'Ping Location Service'");


    }


    @Test
    public void verifyStartStopLocationSaveService() {

        // Start Location Save Service
        LogUITest.debug("Starting 'Save Location Service'");
        LocationsHelper.INSTANCE.startLocationSaveService(activityTestRule.getActivity().getApplication(), locationConfigs);

        boolean isLocationSaveServiceRunning = UiUtils.isServiceRunning(LocationSaveService.class.getName());

        AssertUtils.assertTrueV(isLocationSaveServiceRunning,
                "Failed to start 'Save Location Service'",
                "Successfully started 'Save Location Service'");

        // Stop Location Save Service
        LogUITest.debug("Stopping 'Save Location Service'");
        LocationsHelper.INSTANCE.stopLocationSaveService(activityTestRule.getActivity().getApplication());

        boolean isServiceRunning = UiUtils.isServiceRunning(LocationSaveService.class.getName());

        AssertUtils.assertTrueV(!isServiceRunning,
                "Failed to stop 'Save Location Service'",
                "Successfully stopped 'Save Location Service'");
    }


}
