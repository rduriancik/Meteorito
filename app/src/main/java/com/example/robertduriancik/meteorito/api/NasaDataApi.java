package com.example.robertduriancik.meteorito.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by robert-ntb on 5/19/17.
 */

public class NasaDataApi {
    private static final String NASA_DATA_API_ENDPOINT = "https://data.nasa.gov/";
    private final NasaDataService mService;

    public NasaDataApi() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NASA_DATA_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mService = retrofit.create(NasaDataService.class);
    }

    public NasaDataService getService() {
        return mService;
    }
}
