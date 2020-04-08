package testUtils.mockWebServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.mockwebserver.Dispatcher;
import testUtils.Location;
import testUtils.LogUITest;
import testUtils.UiUtils;

public class DispatcherUtils {

    public static Dispatcher currentDispatcher;
    public static void setDispacher(Dispatcher mDispacher) {
        LogUITest.debug("Setting up Response_Dispatcher : " + mDispacher.getClass().getSimpleName());
        MockWebUtils.getMockWebServer().setDispatcher(mDispacher);
        currentDispatcher = mDispacher;
        LogUITest.debug("Response_Dispatcher is set !");
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
                        JSONObject currentLocationObjectOfExpectedBodyJson = UiUtils.convertStringJsonToJsonObject(expectedRequestBodyOfSyncRequest.optJSONArray(expectedJsonCurrentKey).optString(i));
                        JSONObject currentLocationObjectOfActualBodyJson = UiUtils.convertStringJsonToJsonObject(actualRequestBodyOfSyncRequest.optJSONArray(actualJsonCurrentKey).optString(i));

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
