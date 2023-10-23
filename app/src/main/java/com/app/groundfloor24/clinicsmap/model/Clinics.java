package com.app.groundfloor24.clinicsmap.model;

public class Clinics {

    //Model class

    private String name;
    private String lat;
    private String lng;
    private String icon;
    private String distance;



    public Clinics(String name, String lat, String lng, String icon, String distance) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.icon = icon;
        this.distance = distance;

    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
