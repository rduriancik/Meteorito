package com.example.robertduriancik.meteorito.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
    private static final int CACHE_SIZE = 10 * 1024 * 1024;

    private final NasaDataService mService;

    public NasaDataApi(final Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NASA_DATA_API_ENDPOINT)
                .addConverterFactory(createGsonConverterFactory())
                .client(createHttpClient(context))
                .build();

        mService = retrofit.create(NasaDataService.class);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    private GsonConverterFactory createGsonConverterFactory() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();

        return GsonConverterFactory.create(gson);
    }

    private OkHttpClient createHttpClient(final Context context) {
        Cache cache = new Cache(new File(context.getCacheDir(), "MeteoritoCache"), CACHE_SIZE);

//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        // set your desired log level
//        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response origResponse = chain.proceed(chain.request());
                        String cacheControl = origResponse.header("Cache-Control");
                        if (cacheControl == null ||
                                cacheControl.contains("no-store") ||
                                cacheControl.contains("no-cache") ||
                                cacheControl.contains("must-revalidate") ||
                                cacheControl.contains("max-age=0")) {
                            return origResponse.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", "public, max-age=" + (60 * 60 * 24))
                                    .build();
                        } else {
                            return origResponse;
                        }
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (!isNetworkAvailable(context)) {
                            request = request.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", "public, only-if-cached," +
                                            "max-stale=" + (60 * 60 * 24 * 14)) // tolerate 2-weeks stale
                                    .build();
                        }
                        return chain.proceed(request);
                    }
                })
//                .addInterceptor(logging)
                ;

        return httpClientBuilder.build();
    }

    public NasaDataService getService() {
        return mService;
    }
}
