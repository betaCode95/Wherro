package testUtils;

import testUtils.mockWebServer.MockWebUtils;

public class TestConstants
{

    public static String VEHICLE_NUMBER = "";
    public static String USER_ID = "";
    public static String GPS_PIPELINE_URL = MockWebUtils.getMockWebServerUrl() + "";
    public static int NUMBER_OF_RETRIES_FOR_STOPPING_SERVICES = 3;
    public static int NUMBER_OF_RETRIES_FOR_STARTING_SERVICES = 3;

}
