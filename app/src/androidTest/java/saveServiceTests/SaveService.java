package saveServiceTests;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.shuttl.location_pings.config.components.LocationConfigs;

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
import testUtils.customAnnotations.AutoTest_SaveLocationService;

@RunWith(AndroidJUnit4.class)
public class SaveService extends BaseTestCase {


    Location loc1, loc2, loc3, loc4, loc5, loc6;
    List<Location> mockLocationList = new LinkedList<>();

    @Before
    public void setUp() {


        // Set config
        locationConfigs =
                new LocationConfigs(TestConstants.MIN_TIME_INTERVAL_BETWEEN_TWO_LOCATIONS_SS, TestConstants.MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS_SS
                        , TestConstants.MIN_PING_SERVICE_SYNC_INTERVAL_SS, TestConstants.ACCURACY_SS
                        , TestConstants.BUFFER_SIZE_SS, TestConstants.BATCH_SIZE_FOR_PING_SERVICE_SS
                        , TestConstants.SERVICE_TIMEOUT_GLOBAL, "", TestConstants.GPS_PIPELINE_URL
                        , TestConstants.NOTIFICATION_ICON_ID);


        if (testName.getMethodName().equals("verifyBufferSize") || testName.getMethodName().equals("verifyAddingDuplicateLocations")) {
            // Set config
            locationConfigs =
                    new LocationConfigs(100, TestConstants.MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS_SS
                            , TestConstants.MIN_PING_SERVICE_SYNC_INTERVAL_SS, TestConstants.ACCURACY_SS
                            , 3, TestConstants.BATCH_SIZE_FOR_PING_SERVICE_SS
                            , TestConstants.SERVICE_TIMEOUT_GLOBAL, "", TestConstants.GPS_PIPELINE_URL
                            , TestConstants.NOTIFICATION_ICON_ID);
        }


        initiateLocationServices(locationConfigs);

    }

    @AutoTest_SaveLocationService
    @Test
    public void verifyTimeCheck() {

        // --------------------- Set and Validate First Location ---------------------
        loc1 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        mockLocationList.add(loc1);
        MockLocationProvider.setMockLocation(loc1);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Second Location ---------------------
        loc2 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        MockLocationProvider.setMockLocation(loc2);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");

        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");

        UiUtils.safeSleep(15);

        // --------------------- Set and Validate Third Location ---------------------
        loc3 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        mockLocationList.add(loc3);
        MockLocationProvider.setMockLocation(loc3);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


    }


    @AutoTest_SaveLocationService
    @Test
    public void verifyDistanceCheck() {

        // --------------------- Set and Validate First Location ---------------------
        loc1 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        mockLocationList.add(loc1);
        MockLocationProvider.setMockLocation(loc1);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Second Location ---------------------
        loc2 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        mockLocationList.add(loc2);
        MockLocationProvider.setMockLocation(loc2);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");

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

        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


    }

    @AutoTest_SaveLocationService
    @Test
    public void verifyAddingDuplicateLocations() {

        // --------------------- Set and Validate First Location ---------------------
        loc1 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        mockLocationList.add(loc1);
        MockLocationProvider.setMockLocation(loc1);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Duplicate Location ---------------------
        MockLocationProvider.setMockLocation(loc1);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");

    }


    @AutoTest_SaveLocationService
    @Test
    public void verifyBufferSize() {

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
        mockLocationList.remove(0);
        MockLocationProvider.setMockLocation(loc4);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


    }

}
