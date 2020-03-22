package testUtils.mockWebServer;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import testUtils.BaseTestCase;
import testUtils.LogUITest;
import testUtils.PingServiceResponse;
import testUtils.TestConstants;

import static junit.framework.TestCase.fail;

public class CustomDispatcher extends Dispatcher {


    @Override
    public MockResponse dispatch(RecordedRequest request) {

        LogUITest.debug(" ******************** Current request: " + request.getPath() + " *********************");

        LogUITest.debug("Body" + request.getBody().readUtf8());
        LogUITest.debug("Method" + request.getMethod());


        // Get API Response  (Success OR Failure)
        String apiResponse = getAPIResponse();


        if (apiResponse != null) {

            // set and return API RESPONSE
            return new MockResponse().setResponseCode(200)
                    .setBody(apiResponse);
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

    private String getAPIResponse() {

        if (!BaseTestCase.edgeCaseResponses.isEmpty()) {
            if (BaseTestCase.edgeCaseResponses.get(TestConstants.GPS_PIPELINE_URL).equals(TestConstants.RESPONSE_TYPE.FAILURE))
                return PingServiceResponse.FAILURE_RESPONSE;
        }

        return PingServiceResponse.SUCCESS_RESPONSE;

    }

}
