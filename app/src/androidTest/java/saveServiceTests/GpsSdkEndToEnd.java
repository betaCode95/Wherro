package saveServiceTests;

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

@RunWith(AndroidJUnit4.class)
public class GpsSdkEndToEnd extends BaseTestCase {

    Location loc1, loc2, loc3, loc4, loc5;
    List<Location> mockLocationList = new LinkedList<>();
    List<GPSLocation> gpsLocationsFromDatabase;

    @Before
    public void setUp() {


        edgeCaseResponses.put("/" + TestConstants.GPS_PIPELINE_URL_END_POINT, TestConstants.RESPONSE_TYPE.DELAYED);

        // Set config
        locationConfigs =
                new LocationConfigs(TestConstants.MIN_TIME_INTERVAL_BETWEEN_TWO_LOCATIONS, TestConstants.MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS
                        , TestConstants.MIN_PING_SERVICE_SYNC_INTERVAL, TestConstants.ACCURACY
                        , TestConstants.BUFFER_SIZE, TestConstants.BATCH_SIZE_FOR_PING_SERVICE
                        , TestConstants.SERVICE_TIMEOUT_GLOBAL, "", TestConstants.GPS_PIPELINE_URL
                        , TestConstants.NOTIFICATION_ICON_ID);

        // Initiate Both Location Services
        initiateLocationServices(locationConfigs);

    }


    @Test
    public void verifySdkEndToEnd() {

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


        // --------------------- Set and Validate Third Location ---------------------
        loc3 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        mockLocationList.add(loc3);
        MockLocationProvider.setMockLocation(loc3);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Fourth Location ---------------------
        loc4 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        mockLocationList.add(loc4);
        MockLocationProvider.setMockLocation(loc4);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Fifth Location ---------------------
        loc5 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        mockLocationList.add(loc5);
        MockLocationProvider.setMockLocation(loc5);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // Dispatch Success Response and Validate Database
        UiUtils.safeSleep(15);
        edgeCaseResponses.put("/sendGps", TestConstants.RESPONSE_TYPE.SUCCESS);
        UiUtils.safeSleep(5);
        mockLocationList.remove(0);
        mockLocationList.remove(0);
        mockLocationList.remove(0);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // There were total 5 locations we had set using mockLocation .
        // 3 were Dispatched in last call
        // Remaining will be now.
        mockLocationList.clear();
        UiUtils.safeSleep(10);
        gpsLocationsFromDatabase = fetchDataFromDatabase();
        AssertUtils.assertTrueV(gpsLocationsFromDatabase.isEmpty(),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");

    }


}
