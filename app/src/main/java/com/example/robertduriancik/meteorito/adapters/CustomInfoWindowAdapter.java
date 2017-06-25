package com.example.robertduriancik.meteorito.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by robert on 3.6.2017.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context mContext;

    @BindView(R.id.iw_meteorite_name)
    TextView mMeteorName;
    @BindView(R.id.iw_meteorite_name_type)
    TextView mMeteorNameType;
    @BindView(R.id.iw_meteorite_class)
    TextView mMeteorClass;
    @BindView(R.id.iw_meteorite_year)
    TextView mMeteorYear;
    @BindView(R.id.iw_meteorite_fall)
    TextView mMeteorFall;
    @BindView(R.id.iw_meteorite_mass)
    TextView mMeteorMass;

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
        ButterKnife.bind(this, view);

        bind((MeteoriteLanding) marker.getTag());

        return view;
    }

    private void bind(MeteoriteLanding landing) {
        if (landing != null) {
            mMeteorName.setText(landing.getName());
            mMeteorNameType.setText(landing.getNameType());
            mMeteorClass.setText(landing.getRecClass());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(landing.getYear());
            mMeteorYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            mMeteorFall.setText(landing.getFall());
            mMeteorMass.setText(String.valueOf(landing.getMass()));
        }
    }

}
