package testUtils.mockWebServer;

import okhttp3.mockwebserver.Dispatcher;
import testUtils.LogUITest;

public class DispatcherUtils {

    public static Dispatcher currentDispatcher;
    public static void setDispacher(Dispatcher mDispacher) {
        LogUITest.debug("Setting up Response_Dispatcher : " + mDispacher.getClass().getSimpleName());
        MockWebUtils.getMockWebServer().setDispatcher(mDispacher);
        currentDispatcher = mDispacher;
        LogUITest.debug("Response_Dispatcher is set !");
    }
}
