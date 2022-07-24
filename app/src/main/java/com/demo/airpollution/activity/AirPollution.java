package com.demo.airpollution.activity;

public class AirPollution {
    String SiteName;
    String Country;
    //String AQI;
    //String Pollutant;
    String Status;
    //String SO2;
    //String CO;
    //String CO_8hr;
    //String O3;
    //String O3_8hr;
    //String PM10;
    String PM25;
    //String NO2;
    //String NOx;
    //String NO;
    //String WIND_SPEED;
    //String WIND_DIREC;
    //String PublishTime;
    //String PM25_AVG;
    //String PM10_AVG;
    //String SO2_AVG;
    //String Longitude;
    //String Latitude;
    String SiteId;
    //String ImportDate;
    //偷懶...只拿需要的資料
    public void setSiteId(String siteId) {
        SiteId = siteId;
    }

    public String getSiteId() {
        return SiteId;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCountry() {
        return Country;
    }

    public void setPM25(String PM25) {
        this.PM25 = PM25;
    }

    public String getPM25() {
        return PM25;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatus() {
        return Status;
    }

}
