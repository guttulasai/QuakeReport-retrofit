package com.example.android.quakereport.api.model;

public class Earthquake {

    private double mMagnitude;
    private long mTimeInMillis;
    private String mLocation;
    private String mUrl;



    /**
     * Constructs a new {@link Earthquake} object.
     *
     * @param magnitude is the magnitude (size) of the earthquake
     * @param location is the location where the earthquake happened
     * @param timeInMilliseconds is the time in milliseconds (from the Epoch) when the
     *                           earthquake happened
     */
    public Earthquake(double magnitude, String location, long timeInMilliseconds, String url) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMillis = timeInMilliseconds;
        mUrl=url;

    }


    public Double getmMagnitude() {
        return mMagnitude;
    }

   public long getTimeInMillis() {
        return mTimeInMillis;
    }



    public String getmLocation() {
        return mLocation;
    }

    public String getmUrl() {
        return mUrl;
    }


}
