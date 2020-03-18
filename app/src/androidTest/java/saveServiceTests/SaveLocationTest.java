package saveServiceTests;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.service.LocationPingService;
import com.shuttl.location_pings.service.LocationSaveService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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

        setMockLocationInDeveloperOption();
        MockLocationProvider.init(targetContext); // get app under test context
        MockLocationProvider.register();
    }


    @Test
    public void bigGPSTest() {


        MyDao myDao = new MyDao();

        LogUITest.debug("-------- checkpoint 001 ");

        for (int i = 0; i <= 10; i++) {

            LogUITest.debug("The run count is: " + i);
            double latitude = UiUtils.randomGenerator(1, 90);
            double longitude = UiUtils.randomGenerator(1, 90);
            double altitude = UiUtils.randomGenerator(0, 5000);

            MockLocationProvider.setMockLocation(longitude, latitude);

            UiUtils.safeSleep(5);

            MockLocationProvider.setMockLocation(longitude, latitude, altitude);

//
//            LogUITest.debug("Waiting for 20 seconds Location Save Service to Save data in database");
//            UiUtils.safeSleep(20);
//
//           try
//           {
//               List<GPSLocation> gpsLocations = myDao.getLimitedLocations(10);
//               LogUITest.debug("Size of gpsLocations List : " + gpsLocations.size());
//               if (gpsLocations.size() > 0) {
//                   LogUITest.debug("gpsLocations Lat : " + gpsLocations.get(0).getLatitude());
//                   LogUITest.debug("gpsLocations Lng : " + gpsLocations.get(0).getLongitude());
//
//               } else
//                   LogUITest.debug("Gps Locations List is Empty Even After inserting Locations");
//           }catch (Exception e)
//           {
//               LogUITest.debug(e.getMessage());
//           }
        }


    }

}
