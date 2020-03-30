package testUtils;

import java.util.Random;

public class UiUtils {


    /**
     * @param seconds
     * @use to safely sleep
     */
    public static void safeSleep(float seconds) {

        try {
            Thread.sleep((int) seconds * 1000);
        } catch (InterruptedException e) {
            LogUITest.error("Failed to sleep for " + seconds + " seconds !");
            e.printStackTrace();
        }
    }

    /**
     * @param min
     * @param max
     * @return: a random number between min and max
     */
    public static int randomGenerator(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }


}
