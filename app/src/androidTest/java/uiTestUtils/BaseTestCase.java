package uiTestUtils;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

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
            Manifest.permission.ACCESS_COARSE_LOCATION
    );

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule(MainActivity.class);


    @Before
    public void setUp() {

        UiUtils.safeSleep(2);
        setMockLocationInDeveloperOption();
        UiUtils.safeSleep(5);


        LogUITest.debug("Starting GPS TESTS ");
        MockLocationProvider.init(appContext);
        MockLocationProvider.register();


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
    public void tearDown()
    {
        MockLocationProvider.unregister();

    }
}
