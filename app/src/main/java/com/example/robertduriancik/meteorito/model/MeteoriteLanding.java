package com.example.robertduriancik.meteorito.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by robert-ntb on 5/19/17.
 */

public class MeteoriteLanding {

    private long id;
    private String fall;
    private int mass;
    private String name;
    private String nameType;
    @SerializedName("recclass")
    private String recClass;
    @SerializedName("reclat")
    private double latitude;
    @SerializedName("reclong")
    private double longitude;
    @SerializedName("year")
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFall() {
        return fall;
    }

    public void setFall(String fall) {
        this.fall = fall;
    }

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getRecClass() {
        return recClass;
    }

    public void setRecClass(String recClass) {
        this.recClass = recClass;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MeteoriteLanding{" +
                "id=" + id +
                ", fall='" + fall + '\'' +
                ", mass=" + mass +
                ", name='" + name + '\'' +
                ", nameType='" + nameType + '\'' +
                ", recClass='" + recClass + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", date=" + date +
                '}';
    }
}
