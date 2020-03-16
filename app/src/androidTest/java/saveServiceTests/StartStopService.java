package saveServiceTests;

import android.app.Application;
import android.util.Log;

import com.shuttl.location_pings.callbacks.LocationPingServiceCallback;
import com.shuttl.location_pings.config.components.LocationConfigs;
import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.data.model.entity.GPSLocation;
import com.shuttl.location_pings.service.LocationPingService;
import com.shuttl.location_pings.service.LocationSaveService;
import com.shuttl.packagetest.R;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import testUtils.AssertUtils;
import testUtils.BaseTestCase;
import testUtils.LogUITest;
import testUtils.TestConstants;
import testUtils.UiUtils;

public class StartStopService extends BaseTestCase {

    private LocationConfigs locationConfigs;
    private LocationPingServiceCallback locationPingServiceCallback = new LocationPingServiceCallback() {
        @Override
        public void afterSyncLocations(@Nullable List<GPSLocation> locations) {
            assert locations != null;
            Log.i("StartStopService", "afterSyncLocations, number of locations synced: " + locations.size());

        }

        @Override
        public void errorWhileSyncLocations(@Nullable String error) {
            Log.i("StartStopService", "errorWhileSyncLocations : " + error);

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
    public void verifyStartLocationServicesViaInitModule() {


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
        LocationsHelper.INSTANCE.startLocationSaveService(BaseTestCase.appContext, activityTestRule.getActivity().getApplication(), locationConfigs);

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
