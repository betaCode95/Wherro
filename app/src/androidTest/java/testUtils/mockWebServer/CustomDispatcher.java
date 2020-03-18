package testUtils.mockWebServer;

import java.util.Map;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import testUtils.BaseTestCase;
import testUtils.LogUITest;

import static junit.framework.TestCase.fail;

public class CustomDispatcher extends Dispatcher {


    private MockResponseUtils mockResponseUtils = new MockResponseUtils();

    @Override
    public MockResponse dispatch(RecordedRequest request) {

        LogUITest.debug(" ******************** Current request: " + request.getPath() + " *********************");


        // Get Properties FilePath as per API request from Global Mapping properties file  'apiPropFileMapping'
        String apiSpecificPropFilePath = getPropertiesFilePathAsPerAPIRequest(request.getPath());


        // Get API Response  (Happy OR Edgecase)
        String apiResponse = getAPIResponse(request, apiSpecificPropFilePath);


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

    private String getAPIResponse(RecordedRequest request, String apiSpecificPropFilePath) {

        String apiResponse, requestPath;


        // Eliminate request parameters (If Available)
        if (request.getPath().contains("?"))
            requestPath = request.getPath().substring(0, request.getPath().indexOf('?'));
        else
            requestPath = request.getPath();


        // Check if any edgecase is registered against current API
        for (Map.Entry<String, String> e : BaseTestCase.edgeCaseResponses.entrySet()) {

            // Check If any edge case response is expected from the API
            if (e.getKey().equals(requestPath)) {

                // set edge case specific response against an API request as per requested EDGE_CASE_TAG
                apiResponse = mockResponseUtils.getValueOfKeyFromPropFile(BaseTestCase.edgeCaseResponses.get(e.getKey())
                        , apiSpecificPropFilePath);

                return apiResponse;
            }

        }

        // set happy case specific response against an API request HAPPY_TAG
        apiResponse = mockResponseUtils.getValueOfKeyFromPropFile("HAPPY_RESPONSE"
                , apiSpecificPropFilePath);

        return apiResponse;
    }

    private String getPropertiesFilePathAsPerAPIRequest(String requestPath) {
        return mockResponseUtils.getValueOfKeyFromPropFile(requestPath, "/apiPropFileMapping");
    }

}
