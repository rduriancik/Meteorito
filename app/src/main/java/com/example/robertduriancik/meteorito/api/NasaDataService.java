package com.example.robertduriancik.meteorito.api;

import com.example.robertduriancik.meteorito.model.MeteoriteLanding;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by robert-ntb on 5/19/17.
 */

public interface NasaDataService {

    @GET("resource/y77d-th95.json?$where=year>='2011-01-01T00:00:00'")
    Call<List<MeteoriteLanding>> getMeteoriteLandings();
}
