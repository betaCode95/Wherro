package testUtils;

import com.shuttl.packagetest.R;

import testUtils.mockWebServer.MockWebUtils;

public class TestConstants {

    public static String GPS_PIPELINE_URL = MockWebUtils.getMockWebServerUrl() + "sendGps";
    public static int NUMBER_OF_RETRIES_FOR_STOPPING_SERVICES = 3;
    public static final int NOTIFICATION_ICON_ID = R.drawable.ic_loc;


    public enum RESPONSE_TYPE {
        SUCCESS,
        FAILURE
    }


    // ---------------------------------    LOCATION CONFIG FOR COMPLETE FLOW  -------------------------------------

    public static final int MIN_TIME_INTERVAL_BETWEEN_TWO_LOCATIONS = 100;  // in millis
    public static final int MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS = 100;  // in millis
    public static final int MIN_PING_SERVICE_SYNC_INTERVAL = 90000;  // in millis
    public static final int ACCURACY = 3;
    public static final int BUFFER_SIZE = 5;
    public static final int BATCH_SIZE_FOR_PING_SERVICE = 3;
    public static final int SERVICE_TIMEOUT = 1800000;  // in millis

    // ---------------------------------    LOCATION CONFIG FOR COMPLETE FLOW  -------------------------------------



    // ---------------------------------    GLOBAL LOCATION CONFIG   -------------------------------------

    public static final int MIN_TIME_INTERVAL_BETWEEN_TWO_LOCATIONS_GLOBAL = 1000;  // in millis
    public static final int MIN_DISTANCE_INTERVAL_BETWEEN_TWO_LOCATIONS_GLOBAL = 1000;  // in millis
    public static final int MIN_PING_SERVICE_SYNC_INTERVAL_GLOBAL = 10000;  // in millis
    public static final int ACCURACY_GLOBAL = 3;
    public static final int BUFFER_SIZE_GLOBAL = 50;
    public static final int BATCH_SIZE_FOR_PING_SERVICE_GLOBAL = 10;
    public static final int SERVICE_TIMEOUT_GLOBAL = 1800000;  // in millis

    // ---------------------------------    LOCATION CONFIG FOR COMPLETE FLOW  -------------------------------------
}
