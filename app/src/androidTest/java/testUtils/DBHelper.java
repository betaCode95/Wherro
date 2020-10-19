package testUtils;

import android.app.Application;

import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.data.model.entity.GPSLocation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import mockLocationUtils.MockLocationProvider;

public class DBHelper {

    public static boolean setLocationAndValidateDB(Location currentLocation, Application application) {
        MockLocationProvider.setMockLocation(currentLocation);
        BaseTestCase.mockLocationList.add(currentLocation);
        return validateLocationsDataInDatabase(BaseTestCase.mockLocationList, application);
    }


    public static boolean validateLocationsDataInDatabase(List<Location> listOfExpectedLocations, Application application) {

        List<GPSLocation> gpsLocationsFromDatabase = fetchGpsDataFromDatabase(application);

        if (!listOfExpectedLocations.isEmpty()) {


            LogUITest.info("Locations in Mock Locations Set ... !!  ");
            for (int i = 0; i < listOfExpectedLocations.size(); i++) {
                LogUITest.debug(listOfExpectedLocations.get(i).getLatitude() + "," + listOfExpectedLocations.get(i).getLongitude());
            }

            LogUITest.info("Locations in Database ... !!  ");
            for (int i = 0; i < gpsLocationsFromDatabase.size(); i++) {
                LogUITest.debug(gpsLocationsFromDatabase.get(i).getLatitude() + "," + gpsLocationsFromDatabase.get(i).getLongitude());
            }

            // ----------- CHECK IF LOCATIONS IN DATABASE ARE ONE OF OUR MOCKED LOCATIONS OR NOT    --------------
            for (int i = 0; i < listOfExpectedLocations.size(); i++)
            {
                boolean validLocation = false;
                for (int j = 0; j < gpsLocationsFromDatabase.size(); j++)
                {
                    if ((gpsLocationsFromDatabase.get(j).getLatitude() - 13) > 1 || (gpsLocationsFromDatabase.get(j).getLatitude() - 13) < 0 )
                        continue;

                    if (listOfExpectedLocations.get(i).getLatitude() == gpsLocationsFromDatabase.get(j).getLatitude()
                            && listOfExpectedLocations.get(i).getLongitude() == gpsLocationsFromDatabase.get(j).getLongitude()){
                        validLocation =  true;
                        break;
                    }
                }

                if (!validLocation){
                    LogUITest.error("Location : " +
                            listOfExpectedLocations.get(i).getLatitude() + "," + listOfExpectedLocations.get(i).getLongitude()
                            + " is does not exist in database");
                    return false;
                }


            }

            return true;

        }

        LogUITest.error("Have not set any locations. Set Atleast One location using mockLocationServer to compare");
        return false;


    }

    public static List<GPSLocation> fetchGpsDataFromDatabase(Application application) {

        List<GPSLocation> gpsLocations = new LinkedList<>();
        gpsLocations = LocationsHelper.INSTANCE.getAllLocations1(application);
        if (gpsLocations.size() == 0)
            return Collections.emptyList();


        return gpsLocations;

    }
}
