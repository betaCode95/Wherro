package ServiceTests;

import android.app.Application;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.shuttl.location_pings.config.components.LocationConfigs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockLocationUtils.MockLocationProvider;
import testUtils.AssertUtils;
import testUtils.BaseTestCase;
import testUtils.DBHelper;
import testUtils.Location;
import testUtils.ServiceHelper;
import testUtils.TestConstants;
import testUtils.customAnnotations.AutoTest_SaveLocationService;

@RunWith(AndroidJUnit4.class)
public class SaveService extends BaseTestCase {

    Application mainApplication;

    @Before
    public void setUp() {


        // Set config
        locationConfigs =
                new LocationConfigs(TestConstants.MIN_TIME_INTERVAL_FOR_LOCATION_FETCHING_SS
                        , TestConstants.MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS_SS
                        , TestConstants.MIN_PING_SERVICE_SYNC_INTERVAL_SS
                        , TestConstants.ACCURACY_SS
                        , TestConstants.BUFFER_SIZE_SS
                        , TestConstants.BATCH_SIZE_FOR_PING_SERVICE_SS
                        , TestConstants.SERVICE_TIMEOUT_GLOBAL
                        , TestConstants.XAPI_KEY_GLOBAL
                        , TestConstants.GPS_PIPELINE_URL
                        , TestConstants.WAKE_LOCK_ENABLED
                        , TestConstants.NOTIFICATION_ICON_ID);


        // Initiate Both Location Services
        ServiceHelper.startBothServicesIfNotRunning(activityTestRule.getActivity().getApplication()
                , locationConfigs, locationPingServiceCallback, appContext);

        mainApplication = activityTestRule.getActivity().getApplication();

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
        loc1 = ServiceHelper.getNewLocation();
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc1, mainApplication),
                "Failed to set and validate First Location with the Database",
                "Successfully set and validated First Location with the database ");


        // --------------------- Set and Validate Second Location ---------------------
        loc2 = new Location(TestConstants.BASE_LAT + .0001, TestConstants.BASE_LNG + .0001);
        MockLocationProvider.setMockLocation(loc2);
        AssertUtils.assertTrueV(DBHelper.validateLocationsDataInDatabase(mockLocationList, mainApplication),
                "Test Failed !! Database store locations having difference in distance less than the expected difference set in configuration",
                "Successfully validated that database store valid locations only");


        // --------------------- Set and Validate Third Location ---------------------
        loc3 = ServiceHelper.getNewLocation();
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc3, mainApplication),
                "Test Failed !! Database store locations having difference in distance greater than the expected difference set in configuration",
                "Successfully validated that database store valid locations only");


    }

    
    @AutoTest_SaveLocationService
    @Test
    public void verifyAddingDuplicateLocations() {

        // --------------------- Set and Validate First Location ---------------------
        loc1 =ServiceHelper.getNewLocation();
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc1, mainApplication),
                "Failed to set and validate First Location with the Database",
                "Successfully set and validated First Location with the database ");


        // --------------------- Set and Validate Duplicate Location ---------------------
        MockLocationProvider.setMockLocation(loc1);
        AssertUtils.assertTrueV(DBHelper.validateLocationsDataInDatabase(mockLocationList, mainApplication),
                "Test Failed !! Duplicate locations exists in database",
                "Test Passed !! Successfully validated that duplicate locations are not stored in database");

    }

    
    @AutoTest_SaveLocationService
    @Test
    public void verifyBufferSize() {

        // --------------------- Set and Validate First Location ---------------------
        loc1 =ServiceHelper.getNewLocation();
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc1, mainApplication),
                "Failed to set and validate First Location with the Database",
                "Successfully set and validated First Location with the database ");


        // --------------------- Set and Validate Second Location ---------------------
        loc2 =ServiceHelper.getNewLocation();
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc2, mainApplication),
                "Failed to set and validate Second Location with the Database",
                "Successfully set and validated Second Location with the database ");


        // --------------------- Set and Validate Third Location ---------------------
        loc3 =ServiceHelper.getNewLocation();
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc3, mainApplication),
                "Failed to set and validate Third Location with the Database",
                "Successfully set and validated Third Location with the database ");

        // --------------------- Set and Validate Fourth Location ---------------------
        loc4 =ServiceHelper.getNewLocation();
        MockLocationProvider.setMockLocation(loc4);
        mockLocationList.add(loc4);
        mockLocationList.remove(0);
        AssertUtils.assertTrueV(DBHelper.validateLocationsDataInDatabase(mockLocationList, mainApplication),
                "Test Failed !! Old locations are not getting replaced with the recent locations if buffer size has reached.",
                "Test Passed !! Old locations are getting replaced with the recent locations if buffer size has reached.");


    }

}
