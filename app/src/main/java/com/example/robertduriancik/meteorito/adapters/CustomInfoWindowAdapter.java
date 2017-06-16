package com.example.robertduriancik.meteorito.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.robertduriancik.meteorito.FetchAddressIntentService;
import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by robert on 3.6.2017.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = "CustomInfoWindowAdapter";

    private Context mContext;
    private ResultReceiver mReceiver;

    public CustomInfoWindowAdapter(Context context) {
        this.mContext = context;
        mReceiver = new AddressResultReceiver(null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = View.inflate(mContext, R.layout.info_window, null);

        TextView title = (TextView) view.findViewById(R.id.iw_title);
        TextView meteorName = (TextView) view.findViewById(R.id.iw_meteorite_name);
        TextView meteorClass = (TextView) view.findViewById(R.id.iw_meteorite_class);
        TextView meteorAddress = (TextView) view.findViewById(R.id.iw_meteorite_address);

        MeteoriteLanding meteoriteLanding = (MeteoriteLanding) marker.getTag();
        if (meteoriteLanding != null) {
            Location location = new Location("MeteoriteLocation");
            location.setLatitude(meteoriteLanding.getLatitude());
            location.setLongitude(meteoriteLanding.getLongitude());
            fetchAddress(location);

            // TODO get country name from coordinates
            // TODO design layout

            title.setText(marker.getTitle());
            meteorName.setText(meteoriteLanding.getName());
            meteorClass.setText(meteoriteLanding.getRecClass());

        } else {
            title.setText("No item");
        }


        return view;
    }

    private void fetchAddress(Location location) {
        if (location != null && Geocoder.isPresent()) {
            Intent intent = new Intent(mContext, FetchAddressIntentService.class);
            intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mReceiver);
            intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA, location);
            mContext.startService(intent);
        }
    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(FetchAddressIntentService.Constants.RESULT_ADDRESS_KEY);
                Log.d(TAG, "onReceiveResult: address" + address);
            } else {
                Log.d(TAG, "onReceiveResult: failed");
            }
        }
    }
}
