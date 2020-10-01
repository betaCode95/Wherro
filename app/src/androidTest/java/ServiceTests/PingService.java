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
import testUtils.Location;
import testUtils.ServiceHelper;
import testUtils.TestConstants;
import testUtils.UiUtils;
import testUtils.customAnnotations.AutoTest_PingLocationService;


@RunWith(AndroidJUnit4.class)
public class PingService extends BaseTestCase {

    Application mainApplication;

    @Before
    public void setUp() {


        edgeCaseResponses.put("/" + TestConstants.GPS_PIPELINE_URL_END_POINT, TestConstants.RESPONSE_TYPE.FAILURE);

        // Set config
        locationConfigs =
                new LocationConfigs(TestConstants.MIN_TIME_INTERVAL_BETWEEN_TWO_LOCATIONS_PS
                        , TestConstants.MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS_PS
                        , TestConstants.MIN_PING_SERVICE_SYNC_INTERVAL_PS, TestConstants.ACCURACY_PS
                        , TestConstants.BUFFER_SIZE_PS, TestConstants.BATCH_SIZE_FOR_PING_SERVICE_PS
                        , TestConstants.SERVICE_TIMEOUT_GLOBAL, TestConstants.XAPI_KEY_GLOBAL, TestConstants.GPS_PIPELINE_URL
                        , TestConstants.NOTIFICATION_ICON_ID);


        // Initiate Both Location Services
        ServiceHelper.startBothServicesIfNotRunning(activityTestRule.getActivity().getApplication()
                , locationConfigs, locationPingServiceCallback, appContext);

        mainApplication = activityTestRule.getActivity().getApplication();

    }


    @AutoTest_PingLocationService
    @Test
    public void verifyFailedResponseOfPingService() {

        // --------------------- Set and Validate First Location ---------------------
        loc1 = new Location(UiUtils.randomGenerator(TestConstants.minValue, TestConstants.maxValue)
                , UiUtils.randomGenerator(TestConstants.minValue, TestConstants.maxValue));
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc1, mainApplication),
                "Failed to set and validate First Location with the Database",
                "Successfully set and validated First Location with the database ");


        // --------------------- Set and Validate Second Location ---------------------
        loc2 = new Location(UiUtils.randomGenerator(TestConstants.minValue, TestConstants.maxValue)
                , UiUtils.randomGenerator(TestConstants.minValue, TestConstants.maxValue));
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc2, mainApplication),
                "Failed to set and validate Second Location with the Database",
                "Successfully set and validated Second Location with the database ");

        edgeCaseResponses.put("/" + TestConstants.GPS_PIPELINE_URL_END_POINT, TestConstants.RESPONSE_TYPE.SUCCESS);
        // Wait for the Sync service to be called.
        // Sync interval is taken as 7 seconds.
        // There is already some wait in setMockLocation() Therefore, not waiting for 7 or more seconds.
        UiUtils.safeSleep(5);
        mockLocationList.remove(0);
        AssertUtils.assertTrueV(DBHelper.validateLocationsDataInDatabase(mockLocationList , mainApplication),
                "There are more than 1 locations data available in database",
                "Successfully validated that database has only 1 location left ");

        // Sync interval is taken as 7 seconds.
        UiUtils.safeSleep(6);
        AssertUtils.assertTrueV(DBHelper.fetchGpsDataFromDatabase(mainApplication).size() == 0,
                "Test Failed !! There are still some Locations in Database. DB should have been empty. ",
                "Test Passed !! Successfully validated that no locations data exists in database ");

    }

}
