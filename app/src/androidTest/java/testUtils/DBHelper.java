package testUtils;

import android.app.Application;

import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.data.model.entity.GPSLocation;

import java.util.LinkedList;
import java.util.List;

import mockLocationUtils.MockLocationProvider;

public class DBHelper {

    public static boolean setLocationAndValidateDB(Location currentLocation , Application application) {
        MockLocationProvider.setMockLocation(currentLocation);
        BaseTestCase.mockLocationList.add(currentLocation);
        return validateLocationsDataInDatabase(BaseTestCase.mockLocationList , application);
    }


    public static boolean validateLocationsDataInDatabase(List<Location> listOfExpectedLocations , Application application) {

        List<GPSLocation> gpsLocationsFromDatabase = fetchGpsDataFromDatabase(application);

        if (!listOfExpectedLocations.isEmpty()) {
            if (listOfExpectedLocations.size() == gpsLocationsFromDatabase.size()) {

                for (int i = 0; i < listOfExpectedLocations.size(); i++) {
                    if (gpsLocationsFromDatabase.get(i).getLatitude() != listOfExpectedLocations.get(i).getLatitude() ||
                            gpsLocationsFromDatabase.get(i).getLongitude() != listOfExpectedLocations.get(i).getLongitude() ||
                            gpsLocationsFromDatabase.get(i).getAccuracy() != listOfExpectedLocations.get(i).getAccuracy() ||
                            !gpsLocationsFromDatabase.get(i).getProvider().equals(listOfExpectedLocations.get(i).getProvider())) {

                        LogUITest.debug("Failed to match all values in database");

                        LogUITest.debug(" --------------------     Expected Params : ---------------------");
                        LogUITest.debug(" Latitude : " + listOfExpectedLocations.get(i).getLatitude());
                        LogUITest.debug(" Longitude : " + listOfExpectedLocations.get(i).getLongitude());
                        LogUITest.debug(" Provider : " + listOfExpectedLocations.get(i).getProvider());
                        LogUITest.debug(" Accuracy : " + listOfExpectedLocations.get(i).getAccuracy());

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
                LogUITest.debug("Number of Locations mock location set : " + listOfExpectedLocations.size());
                LogUITest.debug("Number of Locations in database : " + gpsLocationsFromDatabase.size());
                return false;

            }
        }

        LogUITest.debug("Have not set any locations. Set Atleast One location using mockLocationServer to compare");
        return false;


    }


    public static List<GPSLocation> fetchGpsDataFromDatabase(Application application) {

        List<GPSLocation> gpsLocations = new LinkedList<>();
        try {
            gpsLocations = LocationsHelper.INSTANCE.getAllLocations1(application);
        } catch (Exception e) {
            LogUITest.debug(e.getMessage());
        }
        return gpsLocations;
    }
}
