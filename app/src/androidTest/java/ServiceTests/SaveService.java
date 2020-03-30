package ServiceTests;

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
import testUtils.ServiceHelper;
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


        // Initiate Both Location Services
        ServiceHelper.startBothServicesIfNotRunning(activityTestRule.getActivity().getApplication()
                , locationConfigs, locationPingServiceCallback, appContext);

    }

    @AutoTest_SaveLocationService
    @Test
    public void verifyTimeCheck() {

        // --------------------- Set and Validate First Location ---------------------
        long currentTimeStamp = System.currentTimeMillis();
        loc1 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3, currentTimeStamp);
        MockLocationProvider.setMockLocation(loc1);
        mockLocationList.add(loc1);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Second Location ---------------------
        currentTimeStamp = currentTimeStamp + 50; // Added 50 milliseconds
        loc2 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3, currentTimeStamp);
        MockLocationProvider.setMockLocation(loc2);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");

        // --------------------- Set and Validate Third Location ---------------------
        currentTimeStamp = System.currentTimeMillis();
        loc3 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3, currentTimeStamp);
        MockLocationProvider.setMockLocation(loc3);
        mockLocationList.add(loc3);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


    }


    //decimal
    //places   degrees          distance
    //-------  -------          --------
    //0        1                111  km
    //1        0.1              11.1 km
    //2        0.01             1.11 km
    //3        0.001            111  m
    //4        0.0001           11.1 m
    //5        0.00001          1.11 m
    //6        0.000001         11.1 cm
    //7        0.0000001        1.11 cm
    //8        0.00000001       1.11 mm
    //If we were to extend this chart all the way to 13 decimal places:
    //
    //decimal
    //places   degrees          distance
    //-------  -------          --------
    //9        0.000000001      111  μm
    //10       0.0000000001     11.1 μm
    //11       0.00000000001    1.11 μm
    //12       0.000000000001   111  nm
    //13       0.0000000000001  11.1 nm

    @AutoTest_SaveLocationService
    @Test
    public void verifyDistanceCheck() {

        // --------------------- Set and Validate First Location ---------------------
        loc1 = new Location(TestConstants.startLatitude, TestConstants.startLongitude, 3);
        MockLocationProvider.setMockLocation(loc1);
        mockLocationList.add(loc1);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Second Location ---------------------
        loc2 = new Location(TestConstants.startLatitude + .0001, TestConstants.startLongitude + .0001, 3);
        MockLocationProvider.setMockLocation(loc2);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Third Location ---------------------
        loc3 = new Location(TestConstants.startLatitude + .01, TestConstants.startLongitude + .01, 3);
        MockLocationProvider.setMockLocation(loc3);
        mockLocationList.add(loc3);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


    }

    @AutoTest_SaveLocationService
    @Test
    public void verifyAddingDuplicateLocations() {

        // --------------------- Set and Validate First Location ---------------------
        loc1 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        MockLocationProvider.setMockLocation(loc1);
        mockLocationList.add(loc1);
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
        MockLocationProvider.setMockLocation(loc1);
        mockLocationList.add(loc1);
        AssertUtils.assertTrueV(
                validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Second Location ---------------------
        loc2 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        MockLocationProvider.setMockLocation(loc2);
        mockLocationList.add(loc2);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");

        // --------------------- Set and Validate Third Location ---------------------
        loc3 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        MockLocationProvider.setMockLocation(loc3);
        mockLocationList.add(loc3);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");

        // --------------------- Set and Validate Fourth Location ---------------------
        loc4 = new Location(UiUtils.randomGenerator(1, 90), UiUtils.randomGenerator(1, 90), 3);
        MockLocationProvider.setMockLocation(loc4);
        mockLocationList.add(loc4);
        mockLocationList.remove(0);
        AssertUtils.assertTrueV(validateDatabaseByComparingLocations(mockLocationList),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


    }

}
