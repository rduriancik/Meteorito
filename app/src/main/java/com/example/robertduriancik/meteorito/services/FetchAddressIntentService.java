package com.example.robertduriancik.meteorito.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class FetchAddressIntentService extends IntentService {
    private static final String TAG = "FetchAddressIntentServi";

    protected ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        boolean error = false;

        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA);
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
        } catch (IOException e) {
            Log.e(TAG, "onHandleIntent: Service not available", e);
            error = true;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "onHandleIntent: Invalid latitude or longitude - lat " +
                    location.getLatitude() + ", lng " + location.getLongitude(), e);
            error = true;
        } catch (NullPointerException e) {
            Log.e(TAG, "onHandleIntent: Location is null", e);
            error = true;
        }

        if (error) {
            deliverResultToReceiver(Constants.RESULT_ERROR, null);
        } else if (addresses == null || addresses.size() == 0) {
            Log.e(TAG, "onHandleIntent: No address found");
            deliverResultToReceiver(Constants.RESULT_FAILURE, null);
        } else {
            Address address = addresses.get(0);
            Log.i(TAG, "onHandleIntent: Address found");
            deliverResultToReceiver(Constants.RESULT_SUCCESS, address);
        }
    }

    private void deliverResultToReceiver(int resultCode, Address address) {
        if (mReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.RESULT_ADDRESS_KEY, address);
            mReceiver.send(resultCode, bundle);
        }
    }

    public static final class Constants {
        public static final int RESULT_SUCCESS = 0;
        public static final int RESULT_FAILURE = 1;
        public static final int RESULT_ERROR = 2;
        static final String PACKAGE_NAME = "com.example.robertduriancik.meteorito";
        public static final String LOCATION_DATA = PACKAGE_NAME + ".LOCATION_DATA";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_ADDRESS_KEY = PACKAGE_NAME + "RESULT_ADDRESS_KEY";
    }
}
