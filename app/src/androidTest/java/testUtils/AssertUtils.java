package testUtils;

import testUtils.mockWebServer.DispatcherUtils;
import testUtils.mockWebServer.NetworkManager;

import static org.junit.Assert.fail;

public class AssertUtils {

    /**
     * @param condition
     * @param failureMessage
     * @param successMessage
     * @return
     */
    public static void assertTrueV(boolean condition, String failureMessage, String successMessage) {
        if (NetworkManager.requestInspectionFailure) {
            LogUITest.error("*********************************************************************************************");
            LogUITest.error("*********************************************************************************************");
            LogUITest.error("****************************** REQUEST VALIDATION FAILURE  ******************************");
            LogUITest.error("****************************** CLOSE MOCK WEB SERVER       ******************************");
            LogUITest.error("*********************************************************************************************");
            LogUITest.error("*********************************************************************************************");
            fail("***************** REQUEST INSPECTION FAILURE *****************");
            new BaseTestCase().wrapUpTestSetup();
        }
        if (NetworkManager.apiResponseNotFoundFailure) {
            LogUITest.error("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            LogUITest.error("** ** ** IN " + DispatcherUtils.currentDispatcher.getClass().getSimpleName() + " : NO APPROPRIATE RESPONSE FOUND !!! ** ** **");
            LogUITest.error(failureMessage);
            LogUITest.error("** ** ** IN " + DispatcherUtils.currentDispatcher.getClass().getSimpleName() + " : NO APPROPRIATE RESPONSE FOUND !!! ** ** **");
            LogUITest.error("++++++++++x`++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            fail("***************** NO APPROPRIATE RESPONSE FOUND !!! *****************");
            new BaseTestCase().wrapUpTestSetup();
        }
        if (condition) {
            LogUITest.debug(successMessage);
        } else {
            LogUITest.error(failureMessage);
            fail(failureMessage);
        }
    }

    /**
     * @param condition
     * @param failureMessage
     */
    public static void assertNetworkManagerTrueV(boolean condition, String failureMessage) throws AssertionError {
        if (!condition) {
            LogUITest.error(failureMessage);
            throw new AssertionError(failureMessage);
        }

    }

    /**
     * @param condition
     * @param failureMessage
     * @return
     */
    public static void assertTrueV(boolean condition, String failureMessage) {
        if (!condition) {
            LogUITest.error(failureMessage);
        }
    }
}
