package com.example.robertduriancik.meteorito.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.adapters.CustomInfoWindowAdapter;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;
import com.example.robertduriancik.meteorito.services.FetchAddressIntentService;
import com.example.robertduriancik.meteorito.utils.NetworkUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapFragment";

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private MeteoriteLanding mMeteoriteLanding;
    private Address mMeteoriteAddress;
    private ResultReceiver mReceiver;
    private Context mContext;

    @BindView(R.id.mapView)
    MapView mMapView;

    @BindView(R.id.location_lat)
    TextView mLat;
    @BindView(R.id.location_lng)
    TextView mLng;
    @BindView(R.id.location_country)
    TextView mCountry;
    @BindView(R.id.location_country_label)
    TextView mCountryLabel;
    @BindView(R.id.location_region)
    TextView mRegion;
    @BindView(R.id.location_region_label)
    TextView mRegionLabel;
    @BindView(R.id.location_progressBar)
    ProgressBar mProgressBar;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param meteoriteLanding meteorite landing to show.
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance(MeteoriteLanding meteoriteLanding) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(MeteoriteLanding.class.getSimpleName(), meteoriteLanding);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReceiver = new AddressResultReceiver(null);
        mContext = getContext();

        if (getArguments() != null) {
            mMeteoriteLanding = getArguments().getParcelable(MeteoriteLanding.class.getSimpleName());
        } else {
            throw new IllegalStateException("This fragment must be created using the " +
                    "newInstance(MeteoriteLanding meteoriteLanding) method.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
            mMeteoriteAddress = savedInstanceState.getParcelable(Address.class.getSimpleName());
        }

        String[] coords = formatLatLng(mMeteoriteLanding.getLatitude(), mMeteoriteLanding.getLongitude());
        mLat.setText(coords[0]);
        mLng.setText(coords[1]);
        if (mMeteoriteAddress != null) {
            setAddressFields();
        } else if (!NetworkUtils.isNetworkAvailable(mContext) || !Geocoder.isPresent()) {
            Log.d(TAG, "onCreateView: no internet");
            showFields(false);
        } else {
            showLoading();
            fetchAddress(mMeteoriteLanding);
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        return view;
    }

    private void setAddressFields() {
        if (mMeteoriteAddress != null) {
            if (mMeteoriteAddress.getCountryName() != null) {
                mCountry.setTypeface(null, Typeface.NORMAL);
                mCountry.setText(mMeteoriteAddress.getCountryName());
            }

            if (mMeteoriteAddress.getAdminArea() != null) {
                mRegion.setTypeface(null, Typeface.NORMAL);
                mRegion.setText(mMeteoriteAddress.getAdminArea());
            }
        }
    }

    private void showLoading() {
        mLat.setVisibility(View.GONE);
        mLng.setVisibility(View.GONE);
        mCountry.setVisibility(View.GONE);
        mCountryLabel.setVisibility(View.GONE);
        mRegion.setVisibility(View.GONE);
        mRegionLabel.setVisibility(View.GONE);

        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void showFields(boolean showAddressFields) {
        mProgressBar.setVisibility(View.GONE);

        mLat.setVisibility(View.VISIBLE);
        mLng.setVisibility(View.VISIBLE);
        if (showAddressFields) {
            mCountry.setVisibility(View.VISIBLE);
            mCountryLabel.setVisibility(View.VISIBLE);
            mRegion.setVisibility(View.VISIBLE);
            mRegionLabel.setVisibility(View.VISIBLE);
        } else {
            mCountry.setVisibility(View.GONE);
            mCountryLabel.setVisibility(View.GONE);
            mRegion.setVisibility(View.GONE);
            mRegionLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Address.class.getSimpleName(), mMeteoriteAddress);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng latLng = new LatLng(mMeteoriteLanding.getLatitude(), mMeteoriteLanding.getLongitude());

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(mContext));
//        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                if (marker.isInfoWindowShown()) {
//                    marker.hideInfoWindow();
//                } else {
//                    marker.showInfoWindow();
//                }
//            }
//        });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                } else {
                    marker.showInfoWindow();
                }

                return true;
            }
        });

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(false));
        marker.setTag(mMeteoriteLanding);
    }

    private String[] formatLatLng(double latitude, double longitude) {
        String[] lat = Location.convert(latitude, Location.FORMAT_SECONDS).replace(',', '.').split(":");
        String latStr = String.format(Locale.getDefault(), "%s°%s'%.1f\"%c", lat[0], lat[1], Float.valueOf(lat[2]),
                (latitude > 0 ? 'N' : 'S'));

        String[] lon = Location.convert(longitude, Location.FORMAT_SECONDS).replace(',', '.').split(":");
        String lngStr = String.format(Locale.getDefault(), "%s°%s'%.1f\"%c", lon[0], lon[1], Float.valueOf(lon[2]),
                (longitude > 0 ? 'E' : 'W'));

        return new String[]{latStr, lngStr};
    }


    private void fetchAddress(MeteoriteLanding meteoriteLanding) {
        if (meteoriteLanding != null && Geocoder.isPresent()) {
            Location location = new Location("MeteoriteLocation");
            location.setLatitude(meteoriteLanding.getLatitude());
            location.setLongitude(meteoriteLanding.getLongitude());

            Intent intent = new Intent(mContext, FetchAddressIntentService.class);
            intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mReceiver);
            intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA, location);
            mContext.startService(intent);
        }
    }

    private class AddressResultReceiver extends ResultReceiver {
        private static final String TAG = "AddressResultReceiver";

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Activity activity = getActivity();
            if (activity != null) {
                if (resultCode == FetchAddressIntentService.Constants.RESULT_ERROR) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showFields(false);
                        }
                    });

                    return;
                }

                if (resultCode == FetchAddressIntentService.Constants.RESULT_SUCCESS) {
                    mMeteoriteAddress = resultData.getParcelable(FetchAddressIntentService.Constants.RESULT_ADDRESS_KEY);
                    Log.d(TAG, "onReceiveResult: address" + mMeteoriteAddress);
                    setAddressFields();
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showFields(true);
                    }
                });
            }
        }
    }

}
