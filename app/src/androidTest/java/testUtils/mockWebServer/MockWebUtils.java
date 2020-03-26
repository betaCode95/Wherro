package testUtils.mockWebServer;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;
import testUtils.LogUITest;

public class MockWebUtils {

    private MockWebUtils() {
    }

    public static MockWebServer getMockWebServer() {
        return mockWebServer;
    }

    private static MockWebServer mockWebServer;
    private static String mockWebServerUrl;

    public static String getMockWebServerUrl() {
        return mockWebServerUrl;
    }

    public static void startServer() throws IOException {
        if (mockWebServer == null) {
            mockWebServer = new MockWebServer();
            mockWebServer.start();
        }
        mockWebServerUrl = mockWebServer.url("/").url().toString();
    }


    public static void stopServer() {
        if (mockWebServer != null) {
            LogUITest.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            LogUITest.debug("@@@@@@@@@@@@@@@@@@@@@@@@ TEARDOWN : SHUTTING DOWN MOCK WEB SERVER IN MOCK WEB UTILS TEARDOWN @@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            LogUITest.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            try {
                mockWebServer.shutdown();
            } catch (IOException e) {
                LogUITest.debug("IOException occurred while shutting down mock web server : " + e.getMessage());

            } catch (Exception e) {
                LogUITest.debug("Exception occurred while shutting down mock web server : " + e.getMessage());

            }
        }

    }

}
