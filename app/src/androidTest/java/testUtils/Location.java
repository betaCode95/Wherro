package testUtils;

public class Location {
    private double latitude;
    private double longitude;
    private String timeStamp;
    private String provider = "gps";
    float accuracy;


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

    public float getAccuracy() {
        return accuracy;
    }

    public Location(double latitude, double longitude, float accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;

    }


    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }
}
