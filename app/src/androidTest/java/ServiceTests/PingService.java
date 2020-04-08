package ServiceTests;

import android.app.Application;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.shuttl.location_pings.config.components.LocationConfigs;
import com.shuttl.location_pings.data.model.entity.GPSLocation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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
    List<GPSLocation> gpsLocationsCurrentlyInDatabase;

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
                , UiUtils.randomGenerator(TestConstants.minValue, TestConstants.maxValue), 3);
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc1, mainApplication),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Second Location ---------------------
        loc2 = new Location(UiUtils.randomGenerator(TestConstants.minValue, TestConstants.maxValue)
                , UiUtils.randomGenerator(TestConstants.minValue, TestConstants.maxValue), 3);
        AssertUtils.assertTrueV(
                DBHelper.setLocationAndValidateDB(loc2, mainApplication),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");

        edgeCaseResponses.put("/" + TestConstants.GPS_PIPELINE_URL_END_POINT, TestConstants.RESPONSE_TYPE.SUCCESS);
        UiUtils.safeSleep(5);
        mockLocationList.remove(0);
        AssertUtils.assertTrueV(DBHelper.validateLocationsDataInDatabase(mockLocationList , mainApplication),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        UiUtils.safeSleep(6);
        gpsLocationsCurrentlyInDatabase = DBHelper.fetchGpsDataFromDatabase(mainApplication);
        AssertUtils.assertTrueV(gpsLocationsCurrentlyInDatabase.isEmpty(),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


    }

}
