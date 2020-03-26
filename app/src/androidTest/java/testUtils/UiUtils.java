package testUtils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.service.LocationPingService;
import com.shuttl.location_pings.service.LocationSaveService;

import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class UiUtils {


    /**
     * @param seconds
     * @use to safely sleep
     */
    public static void safeSleep(float seconds) {

        try {
            Thread.sleep((int) seconds * 1000);
        } catch (InterruptedException e) {
            LogUITest.error("Failed to sleep for " + seconds + " seconds !");
            e.printStackTrace();
        }
    }

    /**
     * @param min
     * @param max
     * @return: a random number between min and max
     */
    public static int randomGenerator(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }

    public static boolean isServiceRunning(String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) BaseTestCase.appContext.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }


    public static boolean stopSaveLocationServiceIfRunning(Application app) {
        LogUITest.debug("Checking if 'Save Location Service' is running");
        for (int i = 0; i < TestConstants.NUMBER_OF_RETRIES_FOR_STOPPING_SERVICES; i++) {
            if (UiUtils.isServiceRunning(LocationSaveService.class.getName())) {
                LogUITest.debug("'Save Location Service' is running . Stopping Now");
                LocationsHelper.INSTANCE.stopLocationSaveService(app);
            } else {
                LogUITest.debug("'Save Location Service' is not running now");
                return true;
            }

        }

        // After last retrial, Check if location service is still running or not
        if (UiUtils.isServiceRunning(LocationSaveService.class.getName())) {
            LogUITest.debug("Failed to shut down 'Save Location Service' even after " + TestConstants.NUMBER_OF_RETRIES_FOR_STOPPING_SERVICES + " retries");
            return false;
        }

        return true;
    }


    public static boolean stopPingLocationServiceIfRunning(Application app) {

        LogUITest.debug("Checking if 'Ping Location Service' is running");
        for (int i = 0; i < TestConstants.NUMBER_OF_RETRIES_FOR_STOPPING_SERVICES; i++) {
            if (UiUtils.isServiceRunning(LocationPingService.class.getName())) {
                LogUITest.debug("'Ping Location Service' is running . Stopping Now");
                LocationsHelper.INSTANCE.unBindLocationPingService(app);
                LocationsHelper.INSTANCE.stopLocationPingService(app);
            } else {
                LogUITest.debug("'Ping Location Service' is not running now");
                return true;
            }

        }

        // After last retrial, Check if location service is still running or not
        if (UiUtils.isServiceRunning(LocationPingService.class.getName())) {
            LogUITest.debug("Failed to shut down 'Ping Location Service' even after " + TestConstants.NUMBER_OF_RETRIES_FOR_STOPPING_SERVICES + " retries");
            return false;
        }

        return true;

    }

    public static JSONObject convertStringObjectToJsonObject(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        } catch (Exception e) {
            LogUITest.debug("Exception Occurred While String to Json Conversion: " + e.getMessage());
            e.printStackTrace();

        }

        return null;
    }

}
