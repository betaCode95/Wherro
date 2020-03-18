package testUtils.mockWebServer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import testUtils.LogUITest;

public class MockResponseUtils {

    /* Utils to load server response from .properties files*/

    private Properties loadPropertiesFile(String propsFile) {
        Properties properties = new Properties();
        try {

            InputStream inputStream = getClass().getResourceAsStream(propsFile);
            if (inputStream != null) {
                properties.load(inputStream);
            }

        } catch (FileNotFoundException e) {
            LogUITest.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LogUITest.error(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            LogUITest.error(e.getMessage());
            e.printStackTrace();
        }

        return properties;
    }

    /* get value against a key from properties file
     * Param : Key */
    public String getValueOfKeyFromPropFile(String key, String propFilePath) {

        String val;

//        LogUITest.debug("*************** SEARCHING IN  PROP FILE : " +
//                "/app/src/androidTest/resources/" + propFilePath + " FOR KEY : " + key);

        Properties propFile = loadPropertiesFile(propFilePath);

        if (propFile == null) {
            LogUITest.error("****************** UNABLE TO LOAD/LOCATE PROPERTIES FILE  **********************");
            LogUITest.error("UNABLE TO LOAD PROPERTIES FILE  : " + propFilePath);
            LogUITest.error("******************  UNABLE TO LOAD/LOCATE PROPERTIES FILE  **********************");
            return propFilePath;
        }

        // Handle GET API where request parameters are separated by '?'
        String apiEndPoint = getStringSeparatedByQuestionMark(key, 0);
        val = propFile.getProperty(apiEndPoint);
        if (val == null || val.isEmpty()) {
            LogUITest.error("****************** REQUESTED KEY NOT FOUND IN PROPERTIES FILE  **********************");
            LogUITest.error("KEY : " + key + " NOT FOUND IN PROPERTIES FILE  : " + propFilePath);
            LogUITest.error("******************  REQUESTED KEY NOT FOUND IN PROPERTIES FILE  **********************");
        }
        return val;
    }


    /**
     * @param stringToBeSeparated : Complete String containing '?' and needs to be separated on the basis of this special character
     * @param subStringIndex      : Index of expected string which will be generated after splitting main string with '?'
     * @return :
     * <p>
     * Example :
     * <p>
     * stringToBeSeparated : /v2/user/safetyFeatureDetail?safetyFeatureType=EMERGENCY_CONTACTS
     * Here, after separation main string by '?' , Main string is converted into 2 sub strings.
     * [0] : /v2/user/safetyFeatureDetail
     * [1] : safetyFeatureType=EMERGENCY_CONTACTS
     */

    public String getStringSeparatedByQuestionMark(String stringToBeSeparated, int subStringIndex) {
        // Handle GET API where request parameters are separated by '?'
        List<String> requestParams = Arrays.asList(stringToBeSeparated.split("\\?"));
        return requestParams.get(subStringIndex);
    }
}
