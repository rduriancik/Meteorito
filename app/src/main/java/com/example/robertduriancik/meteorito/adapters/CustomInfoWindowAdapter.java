package com.example.robertduriancik.meteorito.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

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

    public CustomInfoWindowAdapter(Context context) {
        this.mContext = context;
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
//        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
//        Log.d(TAG, "getInfoContents: geocoder " + geocoder.isPresent());
//        List<Address> addresses = null;
//        try {
//            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
//        } catch (IOException ex) {
//            Log.e(TAG, "getInfoContents: ", ex);
//        }

        // TODO get country name from coordinates
        // TODO design layout

        if (meteoriteLanding != null) {
            title.setText(marker.getTitle());
            meteorName.setText(meteoriteLanding.getName());
            meteorClass.setText(meteoriteLanding.getRecClass());

        } else {
            title.setText("No item");
        }


        return view;
    }
}
