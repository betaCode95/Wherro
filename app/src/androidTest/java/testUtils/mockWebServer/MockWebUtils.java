package testUtils.mockWebServer;

import android.app.Instrumentation;
import android.content.Context;


import androidx.test.platform.app.InstrumentationRegistry;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;
import testUtils.LogUITest;

public class MockWebUtils {

    private static MockWebServer mockWebServer;

    public static void callOnSetup() throws IOException {
        if (mockWebServer != null){
            LogUITest.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            LogUITest.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ SETUP :MOCK WEB SERVER IS STILL RUNNING @@@@@@@@@@@@@@@@@@@@@@@@@@");
            LogUITest.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ CLOSE SERVER BEFORE RUNNING SETUP FOR NEW TEST @@@@@@@@@@@@@@@@@@@");
            LogUITest.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            mockWebServer.shutdown();
        }
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    public static MockWebServer getMockWebServer() {
        return mockWebServer;
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
