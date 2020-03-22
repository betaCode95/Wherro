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

import java.util.LinkedList;
import java.util.List;

import mockLocationUtils.MockLocationProvider;
import testUtils.AssertUtils;
import testUtils.BaseTestCase;
import testUtils.Location;
import testUtils.LogUITest;
import testUtils.UiUtils;

import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class SaveLocationTest extends BaseTestCase {

    Location loc1, loc2, loc3, loc4, loc5, loc6;
    List<Location> mockLocationList = new LinkedList<>();
    List<GPSLocation> gpsLocationsFromDatabase;

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


        double latitude, longitude;

        // --------------------- Set and Validate First Location ---------------------
        latitude = UiUtils.randomGenerator(1, 90);
        longitude = UiUtils.randomGenerator(1, 90);

        loc1 = new Location(latitude, longitude, 3);
        mockLocationList.add(loc1);
        MockLocationProvider.setMockLocation(loc1.getLongitude(), loc1.getLatitude(), loc1.getAccuracy());
        AssertUtils.assertTrueV(validateDatabase(),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Second Location ---------------------
        latitude = UiUtils.randomGenerator(1, 90);
        longitude = UiUtils.randomGenerator(1, 90);

        loc2 = new Location(latitude, longitude, 3);
        mockLocationList.add(loc2);
        MockLocationProvider.setMockLocation(loc2.getLongitude(), loc2.getLatitude(), loc2.getAccuracy());
        AssertUtils.assertTrueV(validateDatabase(),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Third Location ---------------------
        latitude = UiUtils.randomGenerator(1, 90);
        longitude = UiUtils.randomGenerator(1, 90);

        loc3 = new Location(latitude, longitude, 3);
        mockLocationList.add(loc3);
        MockLocationProvider.setMockLocation(loc3.getLongitude(), loc3.getLatitude(), loc3.getAccuracy());
        AssertUtils.assertTrueV(validateDatabase(),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Fourth Location ---------------------
        loc4 = loc3;
        mockLocationList.add(loc4);
        MockLocationProvider.setMockLocation(loc4.getLongitude(), loc4.getLatitude(), loc4.getAccuracy());
        fetchDataFromDatabase();
        AssertUtils.assertTrueV(mockLocationList.size() != gpsLocationsFromDatabase.size(),
                "Failed : Save Location Service is storing duplicate values in database",
                "Successfully validated that Save Location Service is not storing duplicate locations ");


        // --------------------- Set and Validate Fourth Location ---------------------
        latitude = UiUtils.randomGenerator(1, 90);
        longitude = UiUtils.randomGenerator(1, 90);
        loc4 = new Location(latitude, longitude, 3);
        mockLocationList.add(loc4);
        MockLocationProvider.setMockLocation(loc4.getLongitude(), loc4.getLatitude(), loc4.getAccuracy());
        AssertUtils.assertTrueV(validateDatabase(),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Fifth Location ---------------------
        latitude = UiUtils.randomGenerator(1, 90);
        longitude = UiUtils.randomGenerator(1, 90);
        loc5 = new Location(latitude, longitude, 3);
        mockLocationList.add(loc5);
        MockLocationProvider.setMockLocation(loc5.getLongitude(), loc5.getLatitude(), loc5.getAccuracy());
        AssertUtils.assertTrueV(validateDatabase(),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


        // --------------------- Set and Validate Sixth Location ---------------------
        latitude = UiUtils.randomGenerator(1, 90);
        longitude = UiUtils.randomGenerator(1, 90);
        loc6 = new Location(latitude, longitude, 3);
        mockLocationList.add(loc6);
        MockLocationProvider.setMockLocation(loc6.getLongitude(), loc6.getLatitude(), loc6.getAccuracy());
        AssertUtils.assertTrueV(validateDatabase(),
                "Database state does not match with the desired state",
                "Successfully validated expected database state ");


    }


    public boolean validateDatabase() {

        List<GPSLocation> gpsLocationsFromDatabase = fetchDataFromDatabase();

        if (!mockLocationList.isEmpty()) {
            if (mockLocationList.size() == gpsLocationsFromDatabase.size()) {

                for (int i = 0; i < mockLocationList.size(); i++) {
                    if (gpsLocationsFromDatabase.get(i).getLatitude() != mockLocationList.get(i).getLatitude() ||
                            gpsLocationsFromDatabase.get(i).getLongitude() != mockLocationList.get(i).getLongitude() ||
                            gpsLocationsFromDatabase.get(i).getAccuracy() != mockLocationList.get(i).getAccuracy() ||
                            !gpsLocationsFromDatabase.get(i).getProvider().equals(mockLocationList.get(i).getProvider())) {

                        LogUITest.debug("Failed to match all values in database");

                        LogUITest.debug(" --------------------     Expected Params : ---------------------");
                        LogUITest.debug(" Latitude : " + mockLocationList.get(i).getLatitude());
                        LogUITest.debug(" Longitude : " + mockLocationList.get(i).getLongitude());
                        LogUITest.debug(" Provider : " + mockLocationList.get(i).getProvider());
                        LogUITest.debug(" Accuracy : " + mockLocationList.get(i).getAccuracy());

                        LogUITest.debug(" --------------------     Actual Params : ---------------------");
                        LogUITest.debug(" Latitude : " + gpsLocationsFromDatabase.get(i).getLatitude());
                        LogUITest.debug(" Longitude : " + gpsLocationsFromDatabase.get(i).getLongitude());
                        LogUITest.debug(" Provider : " + gpsLocationsFromDatabase.get(i).getProvider());
                        LogUITest.debug(" Accuracy : " + gpsLocationsFromDatabase.get(i).getAccuracy());
                        return false;
                    }
                }

                return true;


            } else {
                LogUITest.debug("Number of locations in database vs locations set by Mockwebserver are different");
                LogUITest.debug("Number of Locations mock location set : " + mockLocationList.size());
                LogUITest.debug("Number of Locations in database : " + gpsLocationsFromDatabase.size());
                return false;

            }
        }


        LogUITest.debug("Have not set any locations. Set Atleast One location using mockLocationServer to compare");
        return false;


    }


    public List<GPSLocation> fetchDataFromDatabase() {

        List<GPSLocation> gpsLocations = new LinkedList<>();
        try {
            LogUITest.debug("+++++++++++++++++++   Fetching Locations from database   +++++++++++++++++++");
            gpsLocations = LocationsHelper.INSTANCE.getAllLocations1(activityTestRule.getActivity().getApplication());
            LogUITest.debug("+++++++++++++++++++   Fetched Locations from database   +++++++++++++++++++");
        } catch (Exception e) {
            LogUITest.debug(e.getMessage());
        }
        return gpsLocations;
    }


}
