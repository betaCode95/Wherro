package testUtils;

import android.util.Log;

public class LogUITest {

    private static String UiTestTag = "Shuttl_UITest";

    public static void verbose(String logStr) {
        Log.v(UiTestTag, "Verbose: " + logStr);
    }

    public static void debug(String logStr) {
        Log.d(UiTestTag, logStr);
    }

    public static void info(String logStr) {
        Log.i(UiTestTag, logStr);
    }

    public static void warn(String logStr) {
        Log.w(UiTestTag, "Warn: " + logStr);
    }

    public static void error(String logStr) {
        Log.e(UiTestTag, "Error: " + logStr);
    }

    public static String getCallerMethodName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTraceElements[4];

        for (int i = 5; stackTraceElement.toString().contains("assert"); ++i) {
            stackTraceElement = stackTraceElements[i];
        }
        return stackTraceElement.getMethodName();
    }
}
