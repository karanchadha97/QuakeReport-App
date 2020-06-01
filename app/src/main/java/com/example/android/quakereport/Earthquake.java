package com.example.android.quakereport;

public class Earthquake {
    private Double magnitude;
    private String location;
    private Long date;
    private String url;

    public Earthquake(Double mag,String loc,Long dt,String url1)
    {
        magnitude=mag;
        location=loc;
        date=dt;
        url=url1;
    }
    public Double getMagnitude()
    {
        return magnitude;
    }
    public String getLocation()
    {
        return location;
    }
    public Long getDate()
    {
        return date;
    }
    public String getUrl()
    {
        return url;
    }
}
