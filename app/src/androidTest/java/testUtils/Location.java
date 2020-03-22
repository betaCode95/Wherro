package testUtils;

public class Location {
    private double latitude;
    private double longitude;
    private String timeStamp;
    private String provider = "gps";



    public String getTimeStamp() {
        return timeStamp;
    }

    public String getProvider() {
        return provider;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    double accuracy;


    public Location(double latitude, double longitude, int accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;

    }


    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }
}
