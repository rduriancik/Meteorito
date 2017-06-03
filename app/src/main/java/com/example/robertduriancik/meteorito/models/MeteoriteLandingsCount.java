package com.example.robertduriancik.meteorito.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by robert-ntb on 5/19/17.
 */

public class MeteoriteLandingsCount {
    @SerializedName("count_id")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
