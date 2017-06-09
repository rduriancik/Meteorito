package com.example.robertduriancik.meteorito.api;

import com.example.robertduriancik.meteorito.models.MeteoriteLanding;
import com.example.robertduriancik.meteorito.models.MeteoriteLandingsCount;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by robert-ntb on 5/19/17.
 */

public interface NasaDataService {

    @Headers("X-App-Token: aE0kbCh6mv9mBa50y3ifuyy3i")
    @GET("resource/y77d-th95.json?$where=year>='2011-01-01T00:00:00'&$order=mass DESC")
//    Call<List<MeteoriteLanding>> getMeteoriteLandings();
    Call<List<MeteoriteLanding>> getMeteoriteLandings(@Query("$limit") int limit, @Query("$offset") int offset);

    @GET("resource/y77d-th95.json?$select=count(id)&$where=year>='2011-01-01T00:00:00'")
    Call<List<MeteoriteLandingsCount>> getMeteoriteLandingsCount();
}
