package ServiceTests;

import android.app.Application;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.shuttl.location_pings.config.components.LocationConfigs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import testUtils.AssertUtils;
import testUtils.BaseTestCase;
import testUtils.DBHelper;
import testUtils.ServiceHelper;
import testUtils.TestConstants;
import testUtils.UiUtils;
import testUtils.customAnnotations.AutoTest_Critical;

@RunWith(AndroidJUnit4.class)
public class GpsSdkEndToEnd extends BaseTestCase {

    Application mainApplication;

    @Before
    public void setUp() {

        edgeCaseResponses.put("/" + TestConstants.GPS_PIPELINE_URL_END_POINT, TestConstants.RESPONSE_TYPE.DELAYED);

        // Set config
        locationConfigs =
                new LocationConfigs(TestConstants.MIN_TIME_INTERVAL_FOR_LOCATION_FETCHING
                        , TestConstants.MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS
                        , TestConstants.MIN_PING_SERVICE_SYNC_INTERVAL
                        , TestConstants.ACCURACY
                        , TestConstants.BUFFER_SIZE
                        , TestConstants.BATCH_SIZE_FOR_PING_SERVICE
                        , TestConstants.SERVICE_TIMEOUT_GLOBAL
                        , TestConstants.XAPI_KEY_GLOBAL
                        , TestConstants.GPS_PIPELINE_URL
                        , TestConstants.WAKE_LOCK_ENABLED
                        , TestConstants.ALARM_LOCK_ENABLED
                        , TestConstants.NOTIFICATION_ICON_ID);

        // Initiate Both Location Services
        ServiceHelper.startBothServicesIfNotRunning(activityTestRule.getActivity().getApplication()
                , locationConfigs, locationPingServiceCallback, appContext);

        mainApplication = activityTestRule.getActivity().getApplication();

    }

    @AutoTest_Critical
    @Test
    public void verifySdkEndToEnd() {

        // --------------------- Set and Validate First Location ---------------------
        loc1 = ServiceHelper.getNewLocation();
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc1, mainApplication),
                "Failed to set and validate First Location with the Database",
                "Successfully set and validated First Location with the database ");


        // --------------------- Set and Validate Second Location ---------------------
        loc2 = ServiceHelper.getNewLocation();
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc2, mainApplication),
                "Failed to set and validate Second Location with the Database",
                "Successfully set and validated Second Location with the database ");


        // --------------------- Set and Validate Third Location ---------------------
        loc3 = ServiceHelper.getNewLocation();
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc3, mainApplication),
                "Failed to set and validate Third Location with the Database",
                "Successfully set and validated Third Location with the database ");



        // --------------------- Set and Validate Fourth Location ---------------------
        loc4 = ServiceHelper.getNewLocation();
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc4, mainApplication),
                "Failed to set and validate Fourth Location with the Database",
                "Successfully set and validated Fourth Location with the database ");



        // --------------------- Set and Validate Fifth Location ---------------------
        loc5 = ServiceHelper.getNewLocation();
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc5, mainApplication),
                "Failed to set and validate Fifth Location with the Database",
                "Successfully set and validated Fifth Location with the database ");



        // Dispatch Success Response and Validate Database
        // Wait for all other API to get served with the delayed response.
        UiUtils.safeSleep(8);
        edgeCaseResponses.put("/" + TestConstants.GPS_PIPELINE_URL_END_POINT, TestConstants.RESPONSE_TYPE.SUCCESS);
        // Wait for the Sync service to be called.
        // Sync interval is taken as 7 seconds.
        UiUtils.safeSleep(5);
        mockLocationList.remove(0);
        mockLocationList.remove(0);
        mockLocationList.remove(0);
        AssertUtils.assertTrueV(DBHelper.validateLocationsDataInDatabase(mockLocationList, mainApplication),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // Assuming tightest case that last sync request was made just after validating state of database in previous statement.
        // Therefore wait for another call to happen.
        // Sync interval is taken as 7 seconds. So we have for atleast 7 seconds . But taking some buffer of 3 seconds.
        UiUtils.safeSleep(10);
        // There were total 5 locations we had set using mockLocation .
        // 3 were Dispatched in last call
        // Remaining will be now.
        // Therefore, There should not be any locations left in database
        gpsLocationsListFromDatabase = DBHelper.fetchGpsDataFromDatabase(mainApplication);
        AssertUtils.assertTrueV(gpsLocationsListFromDatabase.isEmpty(),
                "Test Failed !! There are still some Locations in Database. DB should have been empty. ",
                "Test Passed !! Successfully validated that no locations data exists in database ");

    }


}
