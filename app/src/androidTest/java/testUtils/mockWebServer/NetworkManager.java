package testUtils.mockWebServer;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import testUtils.AssertUtils;
import testUtils.BaseTestCase;
import testUtils.LogUITest;
import testUtils.PingServiceResponse;
import testUtils.TestConstants;
import testUtils.UiUtils;

import static junit.framework.TestCase.fail;

public class NetworkManager extends Dispatcher {


    @Override
    public MockResponse dispatch(RecordedRequest request) {

        LogUITest.debug(" ******************** Current request: " + request.getPath() + " *********************");

        // Convert Actual Request Body to Json Object
        JSONObject actualRequestBodyAsJsonObject = UiUtils.convertStringJsonToJsonObject(request.getBody().readUtf8());
        // Convert Expected Request Body to Json Object
        JSONObject expectedRequestBodyAsJsonObject = DispatcherUtils.convertListOfLocationsIntoJsonObject(BaseTestCase.mockLocationList, BaseTestCase.currentBatchSize);

        try
        {
            AssertUtils.assertRequestInspectionTrueV((DispatcherUtils.compareLocationJsonObjects(actualRequestBodyAsJsonObject , expectedRequestBodyAsJsonObject)),
                    "Locations Data is as expected in current sync request");
        }catch (AssertionError e)
        {
            BaseTestCase.requestInspectionFailure = true;
            new BaseTestCase().wrapUpTestSetup();
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
