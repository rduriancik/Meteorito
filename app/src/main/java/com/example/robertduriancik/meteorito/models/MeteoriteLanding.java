package com.example.robertduriancik.meteorito.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by robert-ntb on 5/19/17.
 */

public class MeteoriteLanding implements Parcelable {

    private long id;
    private String fall;
    private double mass;
    private String name;
    private String nameType;
    @SerializedName("recclass")
    private String recClass;
    @SerializedName("reclat")
    private double latitude;
    @SerializedName("reclong")
    private double longitude;
    @SerializedName("year")
    private Date year;

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

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
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

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
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
                ", year=" + year +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.fall);
        dest.writeDouble(this.mass);
        dest.writeString(this.name);
        dest.writeString(this.nameType);
        dest.writeString(this.recClass);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeLong(this.year != null ? this.year.getTime() : -1);
    }

    public MeteoriteLanding() {
    }

    protected MeteoriteLanding(Parcel in) {
        this.id = in.readLong();
        this.fall = in.readString();
        this.mass = in.readDouble();
        this.name = in.readString();
        this.nameType = in.readString();
        this.recClass = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        long tmpDate = in.readLong();
        this.year = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Parcelable.Creator<MeteoriteLanding> CREATOR = new Parcelable.Creator<MeteoriteLanding>() {
        @Override
        public MeteoriteLanding createFromParcel(Parcel source) {
            return new MeteoriteLanding(source);
        }

        @Override
        public MeteoriteLanding[] newArray(int size) {
            return new MeteoriteLanding[size];
        }
    };
}
