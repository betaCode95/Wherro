package testUtils;

import android.util.Log;

public class LogUITest {

    private static String TestTag = "Shuttl_GPSTest";

    public static void verbose(String logStr) {
        Log.v(TestTag, "Verbose: " + logStr);
    }

    public static void debug(String logStr) {

        Log.d(TestTag, logStr);
    }

    public static void info(String logStr) {

        Log.i(TestTag, logStr);
    }

    public static void warn(String logStr) {
        Log.w(TestTag, logStr);
    }

    public static void error(String logStr) {
        Log.e(TestTag, logStr);
    }

}
