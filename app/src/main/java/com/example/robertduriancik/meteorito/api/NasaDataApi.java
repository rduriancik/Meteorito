package com.example.robertduriancik.meteorito.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.robertduriancik.meteorito.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by robert-ntb on 5/19/17.
 */

public class NasaDataApi {
    private static final String TAG = "NasaDataApi";

    private static final String NASA_DATA_API_ENDPOINT = "https://data.nasa.gov";
    private static final int CACHE_SIZE = 10 * 1024 * 1024; // 10 MB

    private static NasaDataService mService;

    private NasaDataApi() {
        if (mService != null) {
            throw new RuntimeException("Use getService(Context context) method to get the single instance of this class.");
        }
    }

    public static NasaDataService getService(final Context context) {
        if (mService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NASA_DATA_API_ENDPOINT)
                    .addConverterFactory(createGsonConverterFactory())
                    .client(createHttpClient(context))
                    .build();

            mService = retrofit.create(NasaDataService.class);
        }

        return mService;
    }

    private static GsonConverterFactory createGsonConverterFactory() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();

        return GsonConverterFactory.create(gson);
    }

    private static OkHttpClient createHttpClient(final Context context) {
        Cache cache = new Cache(new File(context.getCacheDir(), "MeteoritoCache"), CACHE_SIZE);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Response origResponse = chain.proceed(chain.request());
                        String cacheControl = origResponse.header("Cache-Control");
                        if (cacheControl == null ||
                                cacheControl.contains("no-store") ||
                                cacheControl.contains("no-cache") ||
                                cacheControl.contains("must-revalidate") ||
                                cacheControl.contains("max-age=0")) {
                            return origResponse.newBuilder()
                                    .header("Cache-Control", "public, max-age=" + (60 * 60 * 24))
                                    .build();
                        } else {
                            return origResponse;
                        }
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request();
                        if (!NetworkUtils.isNetworkAvailable(context)) {
                            request = request.newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale")
                                    .build();
                        }
                        return chain.proceed(request);
                    }
                })
//                .addInterceptor(logging)
                ;

        return httpClientBuilder.build();
    }

}
