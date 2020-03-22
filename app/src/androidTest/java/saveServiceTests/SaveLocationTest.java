package saveServiceTests;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.data.model.entity.GPSLocation;
import com.shuttl.location_pings.service.LocationPingService;
import com.shuttl.location_pings.service.LocationSaveService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import mockLocationUtils.MockLocationProvider;
import testUtils.BaseTestCase;
import testUtils.LogUITest;
import testUtils.UiUtils;

import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class SaveLocationTest extends BaseTestCase {

    @Before
    public void setUp() {

        Intent intent = new Intent(appContext, LocationPingService.class);
        intent.setAction("STOP");
        // Start Both Services Via Init module .
        LogUITest.debug("Starting 'Save Location Service' & 'Ping Location Service Via Init Module'");
        LocationsHelper.INSTANCE.initLocationsModule(activityTestRule.getActivity().getApplication(), null, locationConfigs, locationPingServiceCallback, intent);

        if (!UiUtils.isServiceRunning(LocationSaveService.class.getName()) || !UiUtils.isServiceRunning(LocationPingService.class.getName()))
            fail();

        LogUITest.debug("Both Location Services are running");

    }


    @Test
    public void bigGPSTest() {

        for (int i = 0; i <= 4; i++) {

            LogUITest.debug("Setting Location Index : " + i);
            double latitude = UiUtils.randomGenerator(1, 90);
            double longitude = UiUtils.randomGenerator(1, 90);
            double altitude = UiUtils.randomGenerator(0, 5000);

            MockLocationProvider.setMockLocation(longitude, latitude, altitude);
            UiUtils.safeSleep(5);

            fetchDataFromDatabase();
        }


    }

    public void fetchDataFromDatabase() {
        try {
            LogUITest.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
            LogUITest.debug("Fetching Locations from database");
            List<GPSLocation> gpsLocations = LocationsHelper.INSTANCE.getAllLocations1(activityTestRule.getActivity().getApplication());

            UiUtils.safeSleep(5);

            LogUITest.debug("Number of entries in database : " + gpsLocations.size());
            LogUITest.debug("Latitude In Database : " + gpsLocations.get(0).getLatitude());
            LogUITest.debug("Longitude In Database : " + gpsLocations.get(0).getLongitude());
            LogUITest.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++");

        } catch (Exception e) {
            LogUITest.debug(e.getMessage());
        }
    }


}
