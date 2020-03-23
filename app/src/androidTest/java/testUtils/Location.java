package testUtils;

public class Location {
    private double latitude;
    private double longitude;
    private String timeStamp;
    private String provider = "gps";
    float accuracy = 10f;
    int satellite = -1;
    double altitude;


    public int getSatellite() {
        return satellite;
    }

    public double getAltitude() {
        return altitude;
    }

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

    public Location(double latitude, double longitude , double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;

    }
}
