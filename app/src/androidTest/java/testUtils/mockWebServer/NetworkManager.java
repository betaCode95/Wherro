package testUtils.mockWebServer;

import junit.framework.AssertionFailedError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import testUtils.BaseTestCase;
import testUtils.LogUITest;
import testUtils.PingServiceResponse;
import testUtils.TestConstants;
import testUtils.UiUtils;

import static junit.framework.TestCase.fail;

public class NetworkManager extends Dispatcher {

    public static boolean requestInspectionFailed = false;
    boolean exceptionOccurredWhileComparingRequestObjects = false;
    boolean requestObjectsAreDifferent = false;


    @Override
    public MockResponse dispatch(RecordedRequest request) {

        LogUITest.debug(" ******************** Current request: " + request.getPath() + " *********************");

        // Convert Actual Request Body to Json Object
        JSONObject actualRequestBodyAsJsonObject = UiUtils.convertStringJsonToJsonObject(request.getBody().readUtf8());
        // Convert Expected Request Body to Json Object
        JSONObject expectedRequestBodyAsJsonObject = DispatcherUtils.convertListOfLocationsIntoJsonObject(BaseTestCase.mockLocationList, BaseTestCase.currentBatchSize);

        try {
            if (!DispatcherUtils.compareLocationJsonObjects(actualRequestBodyAsJsonObject, expectedRequestBodyAsJsonObject)) {
                requestObjectsAreDifferent = true;
            }
        } catch (JSONException e) {
            exceptionOccurredWhileComparingRequestObjects = true;
            LogUITest.debug("JSONException occurred while comparing compareLocationJsonObjects " + e.getMessage());
        } catch (Exception e) {
            exceptionOccurredWhileComparingRequestObjects = true;
            LogUITest.debug("Exception occurred while comparing compareLocationJsonObjects " + e.getMessage());
        }

        try {
            if (exceptionOccurredWhileComparingRequestObjects || requestObjectsAreDifferent)
                fail();
        } catch (AssertionFailedError e) {
            requestInspectionFailed = true;
            new BaseTestCase().tearDown();
            LogUITest.debug("Actual & Expected Request body are different : " + e.getMessage());
        }

        // Just in case GPS SDK introduces new API , Test Will start failing and automation team will be notified
        if (request.getPath().contains(TestConstants.GPS_PIPELINE_URL_END_POINT)) {


            if (!BaseTestCase.edgeCaseResponses.isEmpty()) {
                if (BaseTestCase.edgeCaseResponses.get("/" + TestConstants.GPS_PIPELINE_URL_END_POINT).equals(TestConstants.RESPONSE_TYPE.DELAYED)) {
                    LogUITest.debug("Dispatching Delayed Response");
                    return new MockResponse().setBodyDelay(TestConstants.DelayInSeconds.TEN_SEC
                            , TimeUnit.SECONDS).setHeadersDelay(TestConstants.DelayInSeconds.TEN_SEC, TimeUnit.SECONDS);
                }


                if (BaseTestCase.edgeCaseResponses.get("/" + TestConstants.GPS_PIPELINE_URL_END_POINT).equals(TestConstants.RESPONSE_TYPE.FAILURE)) {
                    LogUITest.debug("Dispatching Failure Response");
                    return new MockResponse().setResponseCode(200)
                            .setBody(PingServiceResponse.FAILURE_RESPONSE);
                }

            }

            LogUITest.debug("Dispatching Success Response");
            return new MockResponse().setResponseCode(200)
                    .setBody(PingServiceResponse.SUCCESS_RESPONSE);


        }


        LogUITest.error("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        LogUITest.error("** ** ** IN " + DispatcherUtils.currentDispatcher.getClass().getSimpleName() + " : NO APPROPRIATE RESPONSE FOUND !!! ** ** **");
        LogUITest.error("PATH is: " + request.getPath());
        LogUITest.error("URL is : " + request.getRequestLine());
        LogUITest.error("** ** ** IN " + DispatcherUtils.currentDispatcher.getClass().getSimpleName() + " : NO APPROPRIATE RESPONSE FOUND !!! ** ** **");
        LogUITest.error("++++++++++x`++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        fail();
        return new MockResponse().setResponseCode(404);

    }


}
