package testUtils.mockWebServer;

import android.app.Instrumentation;
import android.content.Context;


import androidx.test.platform.app.InstrumentationRegistry;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;
import testUtils.LogUITest;

public class MockWebUtils {

    private static MockWebServer mockWebServer;
    private static String mockWebServerUrl;

    public static String getMockWebServerUrl() {
        return mockWebServerUrl;
    }

    public static void callOnSetup() throws IOException {
        if (mockWebServer == null) {
            mockWebServer = new MockWebServer();
            mockWebServer.start();
        }
        mockWebServerUrl = mockWebServer.url("/").url().toString();
    }


    public static void callOnTearDown() throws IOException {
        if (mockWebServer != null) {
            LogUITest.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            LogUITest.debug("@@@@@@@@@@@@@@@@@@@@@@@@ TEARDOWN : SHUTTING DOWN MOCK WEB SERVER IN MOCK WEB UTILS TEARDOWN @@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            LogUITest.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            mockWebServer.shutdown();
        }

    }

}
