package testUtils.mockWebServer;

import junit.framework.AssertionFailedError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import testUtils.BaseTestCase;
import testUtils.Location;
import testUtils.LogUITest;
import testUtils.PingServiceResponse;
import testUtils.TestConstants;
import testUtils.UiUtils;

import static junit.framework.TestCase.fail;

public class CustomDispatcher extends Dispatcher {

    boolean exceptionOccurredWhileComparingRequestObjects = false;
    boolean requestObjectsAreDifferent = false;


    @Override
    public MockResponse dispatch(RecordedRequest request) {

        LogUITest.debug(" ******************** Current request: " + request.getPath() + " *********************");

        // Convert Actual Request Body to Json Object
        JSONObject actualRequestBodyAsJsonObject = UiUtils.convertStringObjectToJsonObject(request.getBody().readUtf8());
        // Convert Expected Request Body to Json Object
        JSONObject expectedRequestBodyAsJsonObject = convertListOfLocationsIntoJsonObject(BaseTestCase.mockLocationList, BaseTestCase.currentBatchSize);

        try {
            if (!compareLocationJsonObjects(actualRequestBodyAsJsonObject, expectedRequestBodyAsJsonObject)) {
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
            LogUITest.error("*********************************************************************************************");
            LogUITest.error("*********************************************************************************************");
            LogUITest.error("****************************** REQUEST VALIDATION FAILURE  ******************************" + e.getMessage());
            LogUITest.error("****************************** CLOSE MOCK WEB SERVER       ******************************");
            LogUITest.error("*********************************************************************************************");
            LogUITest.error("*********************************************************************************************");
            new BaseTestCase().tearDown();
            LogUITest.debug("Actual & Expected Request body are different : " + e.getMessage());
        }

        // Just in case GPS SDK introduces new API , Test Will start failing and automation team will be notified
        if (request.getPath().contains(TestConstants.GPS_PIPELINE_URL_END_POINT)) {


            if (!BaseTestCase.edgeCaseResponses.isEmpty()) {
                if (BaseTestCase.edgeCaseResponses.get("/" + TestConstants.GPS_PIPELINE_URL_END_POINT).equals(TestConstants.RESPONSE_TYPE.DELAYED)) {
                    LogUITest.debug("Dispatching Delayed Response");
                    return new MockResponse().setBodyDelay(10, TimeUnit.SECONDS).setHeadersDelay(10, TimeUnit.SECONDS);
                }


                if (BaseTestCase.edgeCaseResponses.get("/" + TestConstants.GPS_PIPELINE_URL_END_POINT).equals(TestConstants.RESPONSE_TYPE.FAILURE)) {
                    LogUITest.debug("Dispatching Failure Response");
                    return new MockResponse().setResponseCode(200)
                            .setBody(PingServiceResponse.FAILURE_RESPONSE);
                }

            }

            LogUITest.debug("Dispatching Successful Response");
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

    public static boolean compareLocationJsonObjects(JSONObject actualRequestBodyOfSyncRequest, JSONObject expectedRequestBodyOfSyncRequest) throws JSONException {

        String expectedJsonCurrentKey, actualJsonCurrentKey;

        // When no location is set by our mock location provider , Then expected request will have no keys and nothing to compare.
        if (!expectedRequestBodyOfSyncRequest.keys().hasNext())
            return true;

        // There is atleast one location set by our mock location provider , Actual request must not be null.
        if (actualRequestBodyOfSyncRequest.keys().hasNext()) {
            actualJsonCurrentKey = actualRequestBodyOfSyncRequest.keys().next();
            expectedJsonCurrentKey = expectedRequestBodyOfSyncRequest.keys().next();

        } else {
            LogUITest.debug("Request body of actual request is empty");
            return false;
        }


        if (expectedJsonCurrentKey.equals(actualJsonCurrentKey)) {

            if (expectedRequestBodyOfSyncRequest.optJSONArray(expectedJsonCurrentKey).length() > 0) {

                // Validate if number of location objects are same in expected and actual request body
                if (expectedRequestBodyOfSyncRequest.optJSONArray(expectedJsonCurrentKey).length() == actualRequestBodyOfSyncRequest.optJSONArray(actualJsonCurrentKey).length()) {
                    for (int i = 0; i < expectedRequestBodyOfSyncRequest.optJSONArray(expectedJsonCurrentKey).length(); i++) {
                        JSONObject currentLocationObjectOfExpectedBodyJson = UiUtils.convertStringObjectToJsonObject(expectedRequestBodyOfSyncRequest.optJSONArray(expectedJsonCurrentKey).optString(i));
                        JSONObject currentLocationObjectOfActualBodyJson = UiUtils.convertStringObjectToJsonObject(actualRequestBodyOfSyncRequest.optJSONArray(actualJsonCurrentKey).optString(i));

                        if (!currentLocationObjectOfExpectedBodyJson.get("accuracy").equals(currentLocationObjectOfActualBodyJson.get("accuracy"))) {
                            LogUITest.debug("'Accuracy' of " + i + 1 + " Location is different");
                            LogUITest.debug("Expected 'Accuracy' of " + i + 1 + " Location is " + currentLocationObjectOfExpectedBodyJson.get("accuracy"));
                            LogUITest.debug("Actual 'Accuracy' of " + i + 1 + " Location is " + currentLocationObjectOfActualBodyJson.get("accuracy"));
                            return false;

                        }
                        if (!currentLocationObjectOfExpectedBodyJson.get("latitude").equals(currentLocationObjectOfActualBodyJson.get("latitude"))) {
                            LogUITest.debug("'Accuracy' of " + i + 1 + " Location is different");
                            LogUITest.debug("Expected 'Accuracy' of " + i + 1 + " Location is " + currentLocationObjectOfExpectedBodyJson.get("accuracy"));
                            LogUITest.debug("Actual 'Accuracy' of " + i + 1 + " Location is " + currentLocationObjectOfActualBodyJson.get("accuracy"));
                            return false;

                        }
                        if (!currentLocationObjectOfExpectedBodyJson.get("longitude").equals(currentLocationObjectOfActualBodyJson.get("longitude"))) {
                            LogUITest.debug("'Accuracy' of " + i + 1 + " Location is different");
                            LogUITest.debug("Expected 'Accuracy' of " + i + 1 + " Location is " + currentLocationObjectOfExpectedBodyJson.get("accuracy"));
                            LogUITest.debug("Actual 'Accuracy' of " + i + 1 + " Location is " + currentLocationObjectOfActualBodyJson.get("accuracy"));
                            return false;

                        }
                        if (!currentLocationObjectOfExpectedBodyJson.get("provider").equals(currentLocationObjectOfActualBodyJson.get("provider"))) {
                            LogUITest.debug("'Accuracy' of " + i + 1 + " Location is different");
                            LogUITest.debug("Expected 'Accuracy' of " + i + 1 + " Location is " + currentLocationObjectOfExpectedBodyJson.get("accuracy"));
                            LogUITest.debug("Actual 'Accuracy' of " + i + 1 + " Location is " + currentLocationObjectOfActualBodyJson.get("accuracy"));
                            return false;

                        }
                    }

                    return true;

                }

                LogUITest.debug("Number of location Objects in Actual Request is different from Expected Number of Location Objects in Request Body");
                LogUITest.debug("Expected Number of Location Objects : " + expectedRequestBodyOfSyncRequest.optJSONArray(expectedJsonCurrentKey).length());
                LogUITest.debug("Actual Number of Location Objects : " + actualRequestBodyOfSyncRequest.optJSONArray(actualJsonCurrentKey).length());
                return false;
            }

            return true;

        }

        LogUITest.debug("Data Object is not found either in Actual Request Or in Expected Request Body");
        LogUITest.debug("Data Object is present in Actual Request : " + actualJsonCurrentKey.equals("data"));
        LogUITest.debug("Data Object is present in Expected Request : " + expectedJsonCurrentKey.equals("data"));
        return false;

    }


    public static JSONObject convertListOfLocationsIntoJsonObject(List<Location> locationsList, int batchSizeOfSyncService) {

        JSONObject finalResponseJsonObject = new JSONObject();
        JSONArray jsonObjectArray = new JSONArray();

        if (locationsList.size() > 0) {
            try {
                for (int i = 0; i < locationsList.size(); i++) {
                    JSONObject formDetailsJson = new JSONObject();
                    formDetailsJson.put("accuracy", locationsList.get(i).getAccuracy());
                    formDetailsJson.put("provider", locationsList.get(i).getProvider());
                    formDetailsJson.put("latitude", locationsList.get(i).getLatitude());
                    formDetailsJson.put("longitude", locationsList.get(i).getLongitude());
                    formDetailsJson.put("time", locationsList.get(i).getTimeStamp());
                    jsonObjectArray.put(formDetailsJson);

                    // Create Expected Location Request Body containing maximum number of locations equal or lesser than sync service batch size.
                    // Batch Size : Maximum Number of locations sync service can take to backend at a time
                    if (i + 1 == batchSizeOfSyncService)
                        break;
                }
                // "data" is the main object key in sync API implementation in GPS SDK
                finalResponseJsonObject.put("data", jsonObjectArray);

            } catch (Exception e) {
                LogUITest.debug("Error Occurred while converting list of locations into JSON Object ");
                LogUITest.debug(e.getMessage());
            }
        }


        return finalResponseJsonObject;
    }


}
