package testUtils;

import static org.junit.Assert.fail;

public class AssertUtils {

    /**
     * @param condition
     * @param failureMessage
     * @param successMessage
     * @return
     */
    public static boolean assertTrueV(boolean condition, String failureMessage, String successMessage) {
        if (condition) {
            LogUITest.debug(successMessage);
            return true;
        } else {
            LogUITest.error(failureMessage);
            fail(); //todo replace this with a custom Supreme fail that'll eventually have send feedback and other fail shenanigans
            return false;
        }
    }

    /**
     * @param condition
     * @param failureMessage
     * @return
     */
    public static boolean assertTrueV(boolean condition, String failureMessage) {
        if (!condition) {
            LogUITest.error(failureMessage);
            return false;
        } else {
            return true;
        }
    }
}
