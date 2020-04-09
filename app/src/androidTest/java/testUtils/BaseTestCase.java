package testUtils;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import com.shuttl.location_pings.callbacks.LocationPingServiceCallback;
import com.shuttl.location_pings.config.components.LocationConfigs;
import com.shuttl.location_pings.data.model.entity.GPSLocation;
import com.shuttl.packagetest.MainActivity;

import junit.framework.TestCase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mockLocationUtils.MockLocationProvider;
import testUtils.mockWebServer.DispatcherUtils;
import testUtils.mockWebServer.MockWebUtils;
import testUtils.mockWebServer.NetworkManager;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class BaseTestCase extends TestCase {

    public static boolean requestInspectionFailure = false ;
    public static Location loc1, loc2, loc3, loc4, loc5;
    public static List<Location> mockLocationList = new LinkedList<>();
    public List<GPSLocation> gpsLocationsListFromDatabase;
    public static Map<String, TestConstants.RESPONSE_TYPE> edgeCaseResponses = new HashMap<>();

    public static Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
    public static Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    public static UiDevice mDevice = UiDevice.getInstance(getInstrumentation());
    public LocationConfigs locationConfigs;
    public static int currentBatchSize;

    public static LocationPingServiceCallback locationPingServiceCallback = new LocationPingServiceCallback() {
        @NotNull
        @Override
        public List beforeSyncLocations(@Nullable List list) {
            return list;
        }

        @Override
        public void afterSyncLocations(@Nullable List list) {
        }

        @Override
        public void errorWhileSyncLocations(Exception e) {
            LogUITest.debug("errorWhileSyncLocations : " + e.getMessage());
        }

        @Override
        public void serviceStarted() {
        }

        @Override
        public void serviceStopped() {
        }

        @Override
        public void serviceStoppedManually() {
        }


    };


    @Rule
    public GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    );

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {

            LogUITest.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            LogUITest.info("@@@@@@@@@@@@@@@@@@@@@@@@    TEST SUCCESS: " + description.getMethodName() + "   @@@@@@@@@@@@@@@@@@@@@@@@");
            LogUITest.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");

            super.succeeded(description);
        }

        @Override
        protected void failed(Throwable e, Description description) {
            LogUITest.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            LogUITest.error("@@@@@@@@@@@@@@@@@@@@@@@@     TEST FAILED: " + description.getMethodName() + " BECAUSE : " + e.getMessage());
            LogUITest.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");

            super.failed(e, description);
        }

    };


    @Rule
    public TestName testName = new TestName();

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void mainSetUp() throws IOException {

        LogUITest.debug("\n***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****");
        LogUITest.info("\n***** \t\tBEGIN Test: " + testName.getMethodName());
        LogUITest.debug("***** ***** ***** ***** ***** ***** ***** ***** ***** ***** *****\n");

        LogUITest.debug("Starting MockWebServer ...........");
        MockWebUtils.startServer();

        LogUITest.debug("Setting Dispatcher ...........");
        DispatcherUtils.setDispacher(new NetworkManager());

        LogUITest.debug("Initiating MockLocationProvider ...........");
        MockLocationProvider.init(targetContext); // get app under test context

        LogUITest.debug("Setting MockLocationProvider in Developer Option ...........");
        setMockLocationInDeveloperOption();

        LogUITest.debug("Register MockLocationProvider ...........");
        MockLocationProvider.register();

        LogUITest.debug("Current GPS PipeLine URL : " + TestConstants.GPS_PIPELINE_URL);

    }


    protected static void setMockLocationInDeveloperOption() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mDevice.executeShellCommand("appops set " + "com.shuttl.packagetest" + " android:mock_location allow");
                //mDevice.executeShellCommand("appops set " + getInstrumentation().getTargetContext().getPackageName() + " android:mock_location allow");
            } catch (IOException e) {
                LogUITest.error("Failed to set Mock Location App in developer options");
                LogUITest.debug(e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                LogUITest.error("Failed to set Mock Location App in developer options");
                LogUITest.debug(e.getMessage());
                e.printStackTrace();

            }
        }
    }

    @After
    public void wrapUpTestSetup() {


        try {
            tearDown();
            MockWebUtils.stopServer();
        } catch (IOException e) {
            LogUITest.error("*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***");
            LogUITest.error("** FAILURE : UI Test Base Case failed to teardown MockWebUtils ! : " + e.getMessage());
            LogUITest.error("*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***");
            e.printStackTrace();
        } catch (Exception e) {
            LogUITest.error("*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***");
            LogUITest.error("** FAILURE : Failed to teardown super.tearDown " + e.getMessage());
            LogUITest.error("*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***");
            e.printStackTrace();
        }

        LogUITest.debug("Un-Register MockLocationProvider ...........");
        MockLocationProvider.unregister();

        ServiceHelper.stopSaveLocationServiceIfRunning(activityTestRule.getActivity().getApplication());
        ServiceHelper.stopPingLocationServiceIfRunning(activityTestRule.getActivity().getApplication());

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }



}
