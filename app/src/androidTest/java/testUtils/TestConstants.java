package testUtils;

import com.shuttl.packagetest.R;

import testUtils.mockWebServer.MockWebUtils;

public class TestConstants {


    /*
    When we call startForgroundSerivce() method, Service do not started immediately.
    When we try to stop the service immediatelystarting it, It may fire the exception.
    Because Service is not started due to processing speed and low memory in RAM.
    This was causing Instrumentation Crash in tests.
     */
    public static int WAIT_FOR_SERVICE_TO_GET_STARTED = 3;

    // Inputs for random number generator
    public static int minValue = 1;
    public static int maxValue = 90;

    public static class DelayInSeconds {

        public static long THREE_SEC = 3000;
        public static long FIVE_SEC = 5000;
        public static long TEN_SEC = 10000;
        public static long TWENTY_SEC = 20000;

    }

    public static double startLatitude = 28.3992;
    public static double startLongitude = 77.0187;

    public static String GPS_PIPELINE_URL_END_POINT = "sendGps/";
    public static String GPS_PIPELINE_URL = MockWebUtils.getMockWebServerUrl() + GPS_PIPELINE_URL_END_POINT;
    public static int NUMBER_OF_RETRIES_FOR_STOPPING_SERVICES = 3;
    public static int NUMBER_OF_RETRIES_FOR_STARTING_SERVICES = 3;
    public static final int NOTIFICATION_ICON_ID = R.drawable.ic_loc;


    public enum RESPONSE_TYPE {
        SUCCESS,
        FAILURE,
        DELAYED
    }


    // ---------------------------------    LOCATION CONFIG FOR PING SERVICE TESTS  -------------------------------------

    public static final int MIN_TIME_INTERVAL_BETWEEN_TWO_LOCATIONS_PS = 100;  // in millis
    public static final int MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS_PS = 1000;
    public static final int MIN_PING_SERVICE_SYNC_INTERVAL_PS = 7000;  // in millis
    public static final int ACCURACY_PS = 3;
    public static final int BUFFER_SIZE_PS = 3;
    public static final int BATCH_SIZE_FOR_PING_SERVICE_PS = 1;


    // ---------------------------------    LOCATION CONFIG FOR SAVE SERVICE TESTS  -------------------------------------

    public static final int MIN_TIME_INTERVAL_BETWEEN_TWO_LOCATIONS_SS = 100;  // in millis
    public static final int MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS_SS = 1000;
    public static final int MIN_PING_SERVICE_SYNC_INTERVAL_SS = 90000;  // in millis
    public static final int ACCURACY_SS = 3;
    public static final int BUFFER_SIZE_SS = 3;
    public static final int BATCH_SIZE_FOR_PING_SERVICE_SS = 1;


    // ---------------------------------    LOCATION CONFIG FOR END TO END FLOW  -------------------------------------

    public static final int MIN_TIME_INTERVAL_BETWEEN_TWO_LOCATIONS = 100;  // in millis
    public static final int MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS = 100;
    public static final int MIN_PING_SERVICE_SYNC_INTERVAL = 7000;  // in millis
    public static final int ACCURACY = 3;
    public static final int BUFFER_SIZE = 5;
    public static final int BATCH_SIZE_FOR_PING_SERVICE = 3;


    // ---------------------------------    GLOBAL LOCATION CONFIG   -------------------------------------

    public static final int MIN_TIME_INTERVAL_BETWEEN_TWO_LOCATIONS_GLOBAL = 1000;  // in millis
    public static final int MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS_GLOBAL = 1000;
    public static final int MIN_PING_SERVICE_SYNC_INTERVAL_GLOBAL = 10000;  // in millis
    public static final int ACCURACY_GLOBAL = 3;
    public static final int BUFFER_SIZE_GLOBAL = 50;
    public static final int BATCH_SIZE_FOR_PING_SERVICE_GLOBAL = 10;
    public static final String XAPI_KEY_GLOBAL = "";
    public static final int SERVICE_TIMEOUT_GLOBAL = 1800000;  // in millis


}
