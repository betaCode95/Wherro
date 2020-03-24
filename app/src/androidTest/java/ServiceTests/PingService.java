package ServiceTests;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.shuttl.location_pings.config.components.LocationConfigs;
import com.shuttl.location_pings.data.model.entity.GPSLocation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;

import mockLocationUtils.MockLocationProvider;
import testUtils.AssertUtils;
import testUtils.BaseTestCase;
import testUtils.Location;
import testUtils.TestConstants;
import testUtils.UiUtils;
import testUtils.customAnnotations.AutoTest_PingLocationService;


@RunWith(AndroidJUnit4.class)
public class PingService extends BaseTestCase {
    List<GPSLocation> gpsLocationsFromDatabase;
    Location loc1, loc2;
    List<Location> mockLocationList = new LinkedList<>();

    @Before
    public void setUp() {


        edgeCaseResponses.put("/" + TestConstants.GPS_PIPELINE_URL_END_POINT, TestConstants.RESPONSE_TYPE.FAILURE);

        // Set config
        locationConfigs =
                new LocationConfigs(100, TestConstants.MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS_SS
                        , 7000, TestConstants.ACCURACY_SS
                        , 3, 1
                        , TestConstants.SERVICE_TIMEOUT_GLOBAL, "", TestConstants.GPS_PIPELINE_URL
                        , TestConstants.NOTIFICATION_ICON_ID);


        initiateLocationServices(locationConfigs);

    }


    @AutoTest_PingLocationService
    @Test
    public void verifyFailedResponseOfPingService() {

        // --------------------- Set and Validate First Location ---------------------
        loc1 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        mockLocationList.add(loc1);
        MockLocationProvider.setMockLocation(loc1);
        AssertUtils.assertTrueV(
                validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Second Location ---------------------
        loc2 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        mockLocationList.add(loc2);
        MockLocationProvider.setMockLocation(loc2);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");

        UiUtils.safeSleep(2);
        edgeCaseResponses.put("/" + TestConstants.GPS_PIPELINE_URL_END_POINT, TestConstants.RESPONSE_TYPE.SUCCESS);
        UiUtils.safeSleep(3);
        mockLocationList.remove(0);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        UiUtils.safeSleep(6);
        gpsLocationsFromDatabase = fetchDataFromDatabase();
        AssertUtils.assertTrueV(gpsLocationsFromDatabase.isEmpty(),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


    }

}
