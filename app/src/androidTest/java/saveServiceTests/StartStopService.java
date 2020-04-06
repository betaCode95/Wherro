package saveServiceTests;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.service.LocationPingService;
import com.shuttl.location_pings.service.LocationSaveService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import testUtils.AssertUtils;
import testUtils.BaseTestCase;
import testUtils.LogUITest;
import testUtils.ServiceHelper;
import testUtils.customAnnotations.AutoTest_StartStopServices;

@RunWith(AndroidJUnit4.class)
public class StartStopService extends BaseTestCase {

    @Before
    public void setUp() {

        ServiceHelper.stopSaveLocationServiceIfRunning(activityTestRule.getActivity().getApplication());
        ServiceHelper.stopPingLocationServiceIfRunning(activityTestRule.getActivity().getApplication());
    }


    @AutoTest_StartStopServices
    @Test
    public void verifyStartStopLocationServicesViaInitModule() {

        Intent intent = new Intent(appContext, LocationPingService.class);
        intent.setAction("STOP");
        // Start Both Services Via Init module .
        LogUITest.debug("Starting 'Save Location Service' & 'Ping Location Service Via Init Module'");
        LocationsHelper.INSTANCE.initLocationsModule(activityTestRule.getActivity().getApplication(), null, locationConfigs, locationPingServiceCallback, intent);


        boolean isLocationSaveServiceRunning = ServiceHelper.isServiceRunning(LocationSaveService.class.getName());

        AssertUtils.assertTrueV(isLocationSaveServiceRunning,
                "Failed to start 'Save Location Service'  Via Init Module",
                "Successfully started 'Save Location Service'  Via Init Module");

        boolean isLocationPingServiceRunning = ServiceHelper.isServiceRunning(LocationPingService.class.getName());

        AssertUtils.assertTrueV(isLocationPingServiceRunning,
                "Failed to start 'Ping Location Service' Via Init Module",
                "Successfully started 'Ping Location Service'  Via Init Module");


        // ----------------------------------------------------------------------------------------------------------------------------------


        LogUITest.debug("Stopping 'Save Location Service' & 'Ping Location Service Via Init Module'");
        LocationsHelper.INSTANCE.stopAndClearAll(activityTestRule.getActivity().getApplication());

        isLocationSaveServiceRunning = ServiceHelper.isServiceRunning(LocationSaveService.class.getName());

        AssertUtils.assertTrueV(!isLocationSaveServiceRunning,
                "Failed to stop 'Save Location Service'  Via Init Module",
                "Successfully stopped 'Save Location Service'  Via Init Module");

        isLocationPingServiceRunning = ServiceHelper.isServiceRunning(LocationPingService.class.getName());

        AssertUtils.assertTrueV(!isLocationPingServiceRunning,
                "Failed to stop 'Ping Location Service' Via Init Module",
                "Successfully stopped 'Ping Location Service'  Via Init Module");


    }


    @AutoTest_StartStopServices
    @Test
    public void verifyStartStopLocationPingService() {

        // Start Location Ping Service
        LogUITest.debug("Starting 'Ping Location Service'");
        LocationsHelper.INSTANCE.startLocationPingService(activityTestRule.getActivity().getApplication(), null, locationConfigs);

        boolean isLocationSaveServiceRunning = ServiceHelper.isServiceRunning(LocationPingService.class.getName());

        AssertUtils.assertTrueV(isLocationSaveServiceRunning,
                "Failed to start 'Ping Location Service'",
                "Successfully started 'Ping Location Service'");


        // Stop Location Ping Service
        LogUITest.debug("Stopping 'Ping Location Service'");
        LocationsHelper.INSTANCE.stopLocationPingService(activityTestRule.getActivity().getApplication());

        boolean isServiceRunning = ServiceHelper.isServiceRunning(LocationPingService.class.getName());

        AssertUtils.assertTrueV(!isServiceRunning,
                "Failed to stop 'Ping Location Service'",
                "Successfully stopped 'Ping Location Service'");


    }


    @AutoTest_StartStopServices
    @Test
    public void verifyStartStopLocationSaveService() {

        // Start Location Save Service
        LogUITest.debug("Starting 'Save Location Service'");
        LocationsHelper.INSTANCE.startLocationSaveService(activityTestRule.getActivity().getApplication(), locationConfigs);

        boolean isLocationSaveServiceRunning = ServiceHelper.isServiceRunning(LocationSaveService.class.getName());

        AssertUtils.assertTrueV(isLocationSaveServiceRunning,
                "Failed to start 'Save Location Service'",
                "Successfully started 'Save Location Service'");

        // Stop Location Save Service
        LogUITest.debug("Stopping 'Save Location Service'");
        LocationsHelper.INSTANCE.stopLocationSaveService(activityTestRule.getActivity().getApplication());

        boolean isServiceRunning = ServiceHelper.isServiceRunning(LocationSaveService.class.getName());

        AssertUtils.assertTrueV(!isServiceRunning,
                "Failed to stop 'Save Location Service'",
                "Successfully stopped 'Save Location Service'");
    }


}
