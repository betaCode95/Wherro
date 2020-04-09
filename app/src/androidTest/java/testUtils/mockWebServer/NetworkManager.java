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

public class NetworkManager extends Dispatcher {


    public static boolean apiResponseNotFoundFailure = false;
    public static boolean requestInspectionFailure = false;

    @Override
    public MockResponse dispatch(RecordedRequest request) {

        LogUITest.debug(" ******************** Current request: " + request.getPath() + " *********************");

        // Convert Actual Request Body to Json Object
        JSONObject actualRequestBodyAsJsonObject = UiUtils.convertStringJsonToJsonObject(request.getBody().readUtf8());
        // Convert Expected Request Body to Json Object
        JSONObject expectedRequestBodyAsJsonObject = DispatcherUtils.convertListOfLocationsIntoJsonObject(BaseTestCase.mockLocationList, BaseTestCase.currentBatchSize);

        try {
            AssertUtils.assertNetworkManagerTrueV((DispatcherUtils.compareLocationJsonObjects(actualRequestBodyAsJsonObject, expectedRequestBodyAsJsonObject)),
                    "Locations Data is different than expected");
        } catch (AssertionError e) {
            requestInspectionFailure = true;
        }

        // Just in case GPS SDK introduces new API , Test Will start failing and automation team will be notified
        if (request.getPath().contains(TestConstants.GPS_PIPELINE_URL)) {


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


        try {
            AssertUtils.assertNetworkManagerTrueV(request.getPath().contains(TestConstants.GPS_PIPELINE_URL),
                    "***************** NO APPROPRIATE RESPONSE FOUND for " + request.getPath() + " !!! *****************");
            return new MockResponse().setResponseCode(404);
        } catch (AssertionError e) {
            apiResponseNotFoundFailure = true ;
            return new MockResponse().setResponseCode(404);

        }


    }


}
