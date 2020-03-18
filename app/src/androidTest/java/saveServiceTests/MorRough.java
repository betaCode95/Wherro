package saveServiceTests;

import android.location.LocationManager;
import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import mockLocationUtils.MockLocationProvider;
import testUtils.BaseTestCase;
import testUtils.LogUITest;
import testUtils.UiUtils;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


@RunWith(AndroidJUnit4.class)
public class MorRough extends BaseTestCase {

    protected static UiDevice mDevice = UiDevice.getInstance(getInstrumentation());
    static String locationProviderName = LocationManager.GPS_PROVIDER;

    LocationManager mLocationManager;

    protected static void setMockLocationInDeveloperOption() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mDevice.executeShellCommand("appops set " + "app.goplus.in.myapplication.debug" + " android:mock_location allow");
                //mDevice.executeShellCommand("appops set " + getInstrumentation().getTargetContext().getPackageName() + " android:mock_location allow");
                safeSleep(3);
            } catch (IOException e) {
                LogUITest.error("Failed to set Mock Location App in developer options");
                e.printStackTrace();
            }
        }
    }

//    @BeforeClass
//    public void setupBeforeClass() {
//
//        safeSleep(2);
//        setMockLocationInDeveloperOption();
//        safeSleep(5);
//
//    }



    @Before
    public void onSetUp() {

        safeSleep(2);
        setMockLocationInDeveloperOption();
        safeSleep(5);

        LogUITest.debug("Starting GPS TESTS ");
        MockLocationProvider.init(getInstrumentation().getTargetContext()); // get app under test context
        //MockLocationProvider.unregisterGPS_PROVIDER();
        MockLocationProvider.register();





        //Instrumentation instrumentation = getInstrumentation();
        //Context context;
        //context = instrumentation.getTargetContext();
        //mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    @Ignore
    @Test
    public void verifyAddingEmergencyContact() {

        LogUITest.debug("-------- checkpoint 000 ");
    }

    @Ignore
    @Test
    public void tinyMockGPSTest() {

        safeSleep(5);
        MockLocationProvider.setMockLocation(10, 20);
        //mLocationManager.getLastKnownLocation(locationProviderName);


        safeSleep(5);
        MockLocationProvider.setMockLocation(30, 40);

        safeSleep(5);
        MockLocationProvider.setMockLocation(50, 60, 5000);

    }

    @Ignore
    @Test
    public void sequentialChangeTest() {

        LogUITest.info("-----------------------------");
        LogUITest.info("BEGIN sequentialChangeTest()");
        for(int count =1; count<=10; count++) {
            LogUITest.debug("The run count is: "+count);
            double latitude = count*11;
            double longitude = count*11;
            MockLocationProvider.setMockLocation(longitude, latitude);
            safeSleep(5);
        }
        LogUITest.info("END sequentialChangeTest()");
        LogUITest.info("-----------------------------");
    }

    @Test
    public void bigGPSTest() {

        LogUITest.debug("-------- checkpoint 001 ");

        for (int i=0; i<=10; i++) {

            LogUITest.debug("The run count is: "+i);
            double latitude = UiUtils.randomGenerator(1,90);
            double longitude = UiUtils.randomGenerator(1,90);
            double altitude = UiUtils.randomGenerator(0,5000);

            MockLocationProvider.setMockLocation(longitude, latitude);

            safeSleep(5);

            MockLocationProvider.setMockLocation(longitude, latitude, altitude);
        }


    }


    public static void safeSleep(float seconds) {

        try {
            Thread.sleep((int) seconds * 1000);
        } catch (InterruptedException e) {
            LogUITest.error("Failed to sleep for " + seconds + " seconds !");
            e.printStackTrace();
        }
    }




}