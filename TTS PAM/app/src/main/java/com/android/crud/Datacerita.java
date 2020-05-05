package com.android.crud;

public class Datacerita {

    private String id, judul, lokasi, cerita, lat, lon;


//
//    public Datacerita(String id, String judul, String lokasi, String cerita) {
//        this.id = id;
//        this.judul = judul;
//        this.lokasi = lokasi;
//        this.cerita = cerita;
//
//    }


//

//
//    public Datacerita(String id, String judul, String lokasi, String cerita) {
//        this.id = id;
//        this.judul = judul;
//        this.lokasi = lokasi;
//        this.cerita = cerita;
//    }


    public Datacerita(String id, String judul, String lokasi, String cerita, String lat, String lon) {
        this.id = id;
        this.judul = judul;
        this.lokasi = lokasi;
        this.cerita = cerita;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getCerita() {
        return cerita;
    }

    public void setCerita(String cerita) {
        this.cerita = cerita;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}