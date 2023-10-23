package com.app.groundfloor24.clinicsmap.model;

public class ClinicFilter {



    String lat,lng,busy,diesel,superl,unleaded,name;


    public ClinicFilter() {
    }

    public ClinicFilter(String lat, String lng, String busy, String diesel, String superl, String unleaded) {
        this.lat = lat;
        this.lng = lng;
        this.busy = busy;
        this.diesel = diesel;
        this.superl = superl;
        this.unleaded = unleaded;
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

    public String getBusy() {
        return busy;
    }

    public void setBusy(String busy) {
        this.busy = busy;
    }

    public String getDiesel() {
        return diesel;
    }

    public void setDiesel(String diesel) {
        this.diesel = diesel;
    }

    public String getSuperl() {
        return superl;
    }

    public void setSuperl(String superl) {
        this.superl = superl;
    }

    public String getUnleaded() {
        return unleaded;
    }

    public void setUnleaded(String unleaded) {
        this.unleaded = unleaded;
    }
}
