package saveServiceTests;

import org.junit.Before;
import org.junit.Test;

import mockLocationUtils.MockLocationProvider;
import uiTestUtils.BaseTestCase;
import uiTestUtils.LogUITest;
import uiTestUtils.UiUtils;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class FirstTest extends BaseTestCase {

    @Before
    public void onSetUp() {

        LogUITest.debug("Starting GPS TESTS ");
        MockLocationProvider.init(getInstrumentation().getTargetContext()); // get app under test context
        //MockLocationProvider2.unregisterGPS_PROVIDER();
        MockLocationProvider.register();

    }


    @Test
    public void tinyMockGPSTest() {

        UiUtils.safeSleep(5);
        MockLocationProvider.setMockLocation(10, 20);
        //mLocationManager.getLastKnownLocation(locationProviderName);


        UiUtils.safeSleep(5);
        MockLocationProvider.setMockLocation(30, 40);

        UiUtils.safeSleep(5);
        MockLocationProvider.setMockLocation(50, 60, 5000);

    }

}
