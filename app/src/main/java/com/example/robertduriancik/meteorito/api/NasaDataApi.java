package com.example.robertduriancik.meteorito.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by robert-ntb on 5/19/17.
 */

public class NasaDataApi {
    private static final String TAG = "NasaDataApi";

    private static final String NASA_DATA_API_ENDPOINT = "https://data.nasa.gov";
    private final NasaDataService mService;
    private static final int CACHE_SIZE = 10 * 1024 * 1024;

    public NasaDataApi(final Context context) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();

//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        // set your desired log level
//        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        Cache cache = new Cache(new File(context.getCacheDir(), "MeteoritoCache"), CACHE_SIZE);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        try {
                            return chain.proceed(chain.request());
                        } catch (Exception e) {
                            Request offlineRequest = chain.request().newBuilder()
                                    .header("Cache-Control", "public, only-if-cached," +
                                            "max-stale=" + 60 * 60 * 24 * 7) // tolerate 1-week stale
                                    .build();
                            return chain.proceed(offlineRequest);
                        }
                    }
                })
//                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NASA_DATA_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();

        mService = retrofit.create(NasaDataService.class);
    }

    public NasaDataService getService() {
        return mService;
    }
}
