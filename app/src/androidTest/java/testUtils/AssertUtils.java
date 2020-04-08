package testUtils;

import testUtils.mockWebServer.NetworkManager;

import static org.junit.Assert.fail;

public class AssertUtils {

    /**
     * @param condition
     * @param failureMessage
     * @param successMessage
     * @return
     */
    public static boolean assertTrueV(boolean condition, String failureMessage, String successMessage) {
        if (NetworkManager.requestInspectionFailed){
            LogUITest.error("*********************************************************************************************");
            LogUITest.error("*********************************************************************************************");
            LogUITest.error("****************************** REQUEST VALIDATION FAILURE  ******************************");
            LogUITest.error("****************************** CLOSE MOCK WEB SERVER       ******************************");
            LogUITest.error("*********************************************************************************************");
            LogUITest.error("*********************************************************************************************");
            fail("************************ REQUEST INSPECTION FAILURE  ***********************");
            return false;
        }
        if (condition) {
            LogUITest.debug(successMessage);
            return true;
        } else {
            LogUITest.error(failureMessage);
            fail(failureMessage);
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
