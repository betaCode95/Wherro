package testUtils;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import com.shuttl.location_pings.callbacks.LocationPingServiceCallback;
import com.shuttl.location_pings.config.components.LocationConfigs;
import com.shuttl.location_pings.config.open_lib.LocationsHelper;
import com.shuttl.location_pings.data.model.entity.GPSLocation;
import com.shuttl.location_pings.service.LocationPingService;
import com.shuttl.location_pings.service.LocationSaveService;
import com.shuttl.packagetest.MainActivity;
import com.shuttl.packagetest.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.IOException;
import java.util.List;

import mockLocationUtils.MockLocationProvider;
import testUtils.mockWebServer.MockWebUtils;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class BaseTestCase {
    public static Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
    public static UiDevice mDevice = UiDevice.getInstance(getInstrumentation());

    // Set config
    public LocationConfigs locationConfigs =
            new LocationConfigs(10000, 100
                    , 10000, 3, 100, 10, 1800000
                    , "", TestConstants.GPS_PIPELINE_URL, TestConstants.USER_ID
                    , TestConstants.VEHICLE_NUMBER, R.drawable.ic_loc);


    public LocationPingServiceCallback locationPingServiceCallback = new LocationPingServiceCallback() {
        @Override
        public void errorWhileSyncLocations(@org.jetbrains.annotations.Nullable String error) {

        }

        @Override
        public void errorWhileSyncLocations(@org.jetbrains.annotations.Nullable Exception error) {

        }

        @Override
        public void afterSyncLocations(@Nullable List<GPSLocation> locations) {
            assert locations != null;

        }

        @Override
        public void serviceStarted() {

        }

        @Override
        public void serviceStopped() {

        }
    };


    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    );


    @Rule
    public TestName testName = new TestName();

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void mainSetUp() throws IOException {

        LogUITest.debug("\n***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****");
        LogUITest.info("\n***** \t\tBEGIN Test: " + testName.getMethodName());
        LogUITest.debug("***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****\n");

        MockWebUtils.callOnSetup();

        // TODO : Call init explicitly to start services for tests other than StartStopServiceTests

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
    public void tearDown() throws IOException {
        MockLocationProvider.unregister();

        UiUtils.stopSaveLocationServiceIfRunning(activityTestRule.getActivity().getApplication());
        UiUtils.stopPingLocationServiceIfRunning(activityTestRule.getActivity().getApplication());

        MockWebUtils.callOnTearDown();



        LogUITest.debug("\n***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****");
        LogUITest.info("\n***** \t\tEND Test: " + testName.getMethodName());
        LogUITest.debug("***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****\n");

    }
}
