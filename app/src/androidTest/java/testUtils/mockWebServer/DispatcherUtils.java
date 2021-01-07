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


    public static boolean compareLocationJsonObjects(JSONObject actualRequestBodyOfSyncRequest, JSONObject expectedRequestBodyOfSyncRequest) {

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


        if (expectedJsonCurrentKey.equals(actualJsonCurrentKey))
        {
            for (int i = 0; i < expectedRequestBodyOfSyncRequest.optJSONArray(expectedJsonCurrentKey).length(); i++) {
                JSONObject currentLocationObjectOfExpectedBodyJson = UiUtils.convertStringJsonToJsonObject(expectedRequestBodyOfSyncRequest.optJSONArray(expectedJsonCurrentKey).optString(i));
                JSONObject currentLocationObjectOfActualBodyJson = UiUtils.convertStringJsonToJsonObject(actualRequestBodyOfSyncRequest.optJSONArray(actualJsonCurrentKey).optString(i));

                try {

                    if (((Double) currentLocationObjectOfActualBodyJson.get("latitude") - 13) > 1 || ((Double) currentLocationObjectOfActualBodyJson.get("latitude") - 13) < 0 )
                        continue;


                    if (!currentLocationObjectOfExpectedBodyJson.get("latitude").equals(currentLocationObjectOfActualBodyJson.get("latitude"))) {
                        LogUITest.error("'Latitude' of " + (i + 1) + " Location is different");
                        LogUITest.error("Expected 'Latitude' of " + (i + 1) + " Location is " + currentLocationObjectOfExpectedBodyJson.get("latitude"));
                        LogUITest.error("Actual 'Latitude' of " + (i + 1) + " Location is " + currentLocationObjectOfActualBodyJson.get("latitude"));
                        return false;

                    }
                    if (!currentLocationObjectOfExpectedBodyJson.get("longitude").equals(currentLocationObjectOfActualBodyJson.get("longitude"))) {
                        LogUITest.error("'Longitude' of " + (i + 1) + " Location is different");
                        LogUITest.error("Expected 'Longitude' of " + (i + 1) + " Location is " + currentLocationObjectOfExpectedBodyJson.get("longitude"));
                        LogUITest.error("Actual 'Longitude' of " + (i + 1) + " Location is " + currentLocationObjectOfActualBodyJson.get("longitude"));
                        return false;

                    }
                } catch (JSONException e) {
                    LogUITest.error("JSONException occurred while comparing compareLocationJsonObjects " + e.getMessage());
                    return false;
                } catch (Exception e) {
                    LogUITest.error("Exception occurred while comparing compareLocationJsonObjects " + e.getMessage());
                    return false;
                }
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
                    if ((i + 1) == batchSizeOfSyncService)
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
