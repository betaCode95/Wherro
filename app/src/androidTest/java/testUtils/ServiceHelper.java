package testUtils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.shuttl.location_pings.callbacks.LocationPingServiceCallback;
import com.shuttl.location_pings.config.components.LocationConfigs;
import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.service.LocationPingService;
import com.shuttl.location_pings.service.LocationSaveService;

import java.util.List;

public class ServiceHelper {


    public static boolean startBothServicesIfNotRunning(Application application, LocationConfigs locationConfigs
            , LocationPingServiceCallback locationPingServiceCallback, Context appContext) {

        BaseTestCase.currentBatchSize = locationConfigs.getBatchSize();
        Intent intent = new Intent(appContext, LocationPingService.class);
        intent.setAction("STOP");
        // Start Both Services Via Init module .
        LogUITest.debug("Starting 'Save Location Service' & 'Ping Location Service Via Init Module'");
        LocationsHelper.INSTANCE.initLocationsModule(application, null, locationConfigs, locationPingServiceCallback, intent);


        boolean isLocationSaveServiceRunning = ServiceHelper.isServiceRunning(LocationSaveService.class.getName());
        if (!isLocationSaveServiceRunning) {
            LogUITest.debug("Failed to start 'Save Location Service'");
            return false;
        }


        boolean isLocationPingServiceRunning = ServiceHelper.isServiceRunning(LocationPingService.class.getName());
        if (!isLocationPingServiceRunning) {
            LogUITest.debug("Failed to start 'Ping Location Service'");
            return false;
        }

        LogUITest.debug("Successfully started 'Save Location Service' & 'Ping Location Service Via Init Module'");
        return true;
    }


    public static boolean stopBothServicesIfRunning(Application application) {

        LogUITest.debug("Stopping 'Save Location Service' & 'Ping Location Service Via Init Module'");
        LocationsHelper.INSTANCE.stopAndClearAll(application);

        boolean isLocationSaveServiceRunning = ServiceHelper.isServiceRunning(LocationSaveService.class.getName());
        if (isLocationSaveServiceRunning) {
            LogUITest.debug("Failed to stop 'Save Location Service'");
            return false;
        }


        boolean isLocationPingServiceRunning = ServiceHelper.isServiceRunning(LocationPingService.class.getName());
        if (isLocationPingServiceRunning) {
            LogUITest.debug("Failed to stop 'Ping Location Service'");
            return false;
        }

        LogUITest.debug("Successfully stopped 'Save Location Service' & 'Ping Location Service'");
        return true;

    }


    public static boolean startPingServiceIfNotRunning(Application application, LocationConfigs locationConfigs) {
        LogUITest.debug("Checking if 'Ping Location Service' is running");
        for (int tryCounter = 0; tryCounter < TestConstants.NUMBER_OF_RETRIES_FOR_STARTING_SERVICES; tryCounter++) {
            if (!isServiceRunning(LocationPingService.class.getName())) {
                LogUITest.debug("'Ping Location Service' is not running. Trying to start the service .........");
                LocationsHelper.INSTANCE.startLocationPingService(application, null, locationConfigs);
            } else {
                LogUITest.debug("'Ping Location Service' is running now.");
                BaseTestCase.currentBatchSize = locationConfigs.getBatchSize();
                return true;
            }
        }

        if (isServiceRunning(LocationPingService.class.getName())) {
            LogUITest.debug("Successfully started 'Ping Location Service'");
            BaseTestCase.currentBatchSize = locationConfigs.getBatchSize();
            return true;
        }

        LogUITest.debug("Failed to start 'Ping Location Service' even after " + TestConstants.NUMBER_OF_RETRIES_FOR_STARTING_SERVICES + " retries");
        return false;

    }

    public static boolean stopPingLocationServiceIfRunning(Application app) {

        LogUITest.debug("Checking if 'Ping Location Service' is running or not");
        for (int counter = 0; counter < TestConstants.NUMBER_OF_RETRIES_FOR_STOPPING_SERVICES; counter++) {
            if (isServiceRunning(LocationPingService.class.getName())) {
                LogUITest.debug("'Ping Location Service' is running. Trying to stop the service ......... ");
                LocationsHelper.INSTANCE.stopLocationPingService(app);
            } else {
                LogUITest.debug("'Ping Location Service' is not running.");
                return true;
            }

        }

        // After last retrial, Check if location service is still running or not
        if (isServiceRunning(LocationPingService.class.getName())) {
            LogUITest.debug("Failed to shut down 'Ping Location Service' even after " + TestConstants.NUMBER_OF_RETRIES_FOR_STOPPING_SERVICES + " retries");
            return false;
        }

        LogUITest.debug("Successfully stopped 'Ping Location Service'");
        return true;

    }

    public static boolean startSaveServiceIfNotRunning(Application application, LocationConfigs locationConfigs) {

        LogUITest.debug("Checking if 'Save Location Service' is running");
        for (int tryCounter = 0; tryCounter < TestConstants.NUMBER_OF_RETRIES_FOR_STARTING_SERVICES; tryCounter++) {
            if (!isServiceRunning(LocationSaveService.class.getName())) {
                LogUITest.debug("'Save Location Service' is not running. Trying to start the service .........");
                LocationsHelper.INSTANCE.startLocationSaveService(application, locationConfigs);
            }
            else
            {
                LogUITest.debug("'Save Location Service' is running now.");
                return true;
            }
        }

        if (isServiceRunning(LocationSaveService.class.getName())) {
            LogUITest.debug("Successfully started 'Save Location Service'");
            return true;
        }

        LogUITest.debug("Failed to start 'Save Location Service' even after " + TestConstants.NUMBER_OF_RETRIES_FOR_STARTING_SERVICES + " retries");
        return false;

    }


    public static boolean stopSaveLocationServiceIfRunning(Application app) {
        LogUITest.debug("Checking if 'Save Location Service' is running or not");
        for (int counter = 0; counter < TestConstants.NUMBER_OF_RETRIES_FOR_STOPPING_SERVICES; counter++) {
            if (isServiceRunning(LocationSaveService.class.getName())) {
                LogUITest.debug("'Save Location Service' is running. Trying to stop the service ......... ");
                LocationsHelper.INSTANCE.stopLocationSaveService(app);
            } else {
                LogUITest.debug("'Save Location Service' is not running.");
                return true;
            }

        }

        // After last retrial, Check if location service is still running or not
        if (isServiceRunning(LocationSaveService.class.getName())) {
            LogUITest.debug("Failed to shut down 'Save Location Service' even after " + TestConstants.NUMBER_OF_RETRIES_FOR_STOPPING_SERVICES + " retries");
            return false;
        }

        LogUITest.debug("Successfully stopped 'Save Location Service'");
        return true;
    }


    public static boolean isServiceRunning(String serviceClassName) {
        LogUITest.debug("Checking if " + serviceClassName + "  is running or not");
        final ActivityManager activityManager = (ActivityManager) BaseTestCase.appContext.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }
}
