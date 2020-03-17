package testUtils;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.service.LocationPingService;
import com.shuttl.location_pings.service.LocationSaveService;
import com.shuttl.packagetest.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;

import mockLocationUtils.MockLocationProvider;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class BaseTestCase {
    public static Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
    public static UiDevice mDevice = UiDevice.getInstance(getInstrumentation());

    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.FOREGROUND_SERVICE
    );

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void mainSetUp() {

        UiUtils.safeSleep(2);

        LogUITest.debug("Checking if 'Save Location Service' is Already running");
        if (UiUtils.isServiceRunning(LocationSaveService.class.getName())) {
            LogUITest.debug("'Save Location Service' is Already running");
            LocationsHelper.INSTANCE.stopLocationSaveService(activityTestRule.getActivity().getApplication());
            LogUITest.debug("'Save Location Service' has been Stopped");
            LogUITest.debug("Current status of 'Location Save Service : '" + UiUtils.isServiceRunning(LocationSaveService.class.getName()));
        }


        LogUITest.debug("Checking if 'Ping Location Service' is Already running");
        if (UiUtils.isServiceRunning(LocationPingService.class.getName())) {
            LogUITest.debug("'Ping Location Service' is Already running");
            LocationsHelper.INSTANCE.stopLocationPingService(activityTestRule.getActivity().getApplication());
            LogUITest.debug("'Ping Location Service' has been stopped");
            LogUITest.debug("Current status of 'Location Ping Service : '" + UiUtils.isServiceRunning(LocationPingService.class.getName()));
        }


    }


    private static void setMockLocationInDeveloperOption() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mDevice.executeShellCommand("appops set " + "com.shuttl.packagetest" + " android:mock_location allow");
                //mDevice.executeShellCommand("appops set " + getInstrumentation().getTargetContext().getPackageName() + " android:mock_location allow");
                UiUtils.safeSleep(3);
            } catch (IOException e) {
                LogUITest.error("Failed to set Mock Location App in developer options");
                e.printStackTrace();
            }
        }
    }

    @After
    public void tearDown() {
        MockLocationProvider.unregister();

        // Stop Location Save Service
        LogUITest.debug("Stopping 'Save Location Service'");
        LocationsHelper.INSTANCE.stopLocationSaveService(activityTestRule.getActivity().getApplication());

        // Stop Location Save Service
        LogUITest.debug("Stopping 'Ping Location Service'");
        LocationsHelper.INSTANCE.stopLocationPingService(activityTestRule.getActivity().getApplication());

    }
}
