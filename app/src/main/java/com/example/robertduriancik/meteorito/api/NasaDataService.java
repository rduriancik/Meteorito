package com.example.robertduriancik.meteorito.api;

import com.example.robertduriancik.meteorito.models.MeteoriteLanding;
import com.example.robertduriancik.meteorito.models.MeteoriteLandingsCount;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by robert-ntb on 5/19/17.
 */

public interface NasaDataService {

    @GET("resource/y77d-th95.json?$where=year>='2011-01-01T00:00:00'&$order=mass DESC")
    Call<List<MeteoriteLanding>> getMeteoriteLandings();

    @GET("resource/y77d-th95.json?$select=count(id)&$where=year>='2011-01-01T00:00:00'")
    Call<List<MeteoriteLandingsCount>> getMeteoriteLandingsCount();
}
