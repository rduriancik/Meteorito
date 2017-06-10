package com.example.robertduriancik.meteorito.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.adapters.CustomInfoWindowAdapter;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private MeteoriteLanding mMeteoriteLanding;

    @BindView(R.id.mapView)
    MapView mMapView;

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
        if (getArguments() != null) {
            mMeteoriteLanding = getArguments().getParcelable(MeteoriteLanding.class.getSimpleName());
        } else {
            throw new IllegalStateException("This fragment must be create using the newInstance(MeteoriteLanding meteoriteLanding) method.");
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
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

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
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(mMeteoriteLanding.getLatitude(), mMeteoriteLanding.getLongitude());

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Meteorite Landing"));
        marker.setTag(mMeteoriteLanding);
        marker.showInfoWindow();
//        TODO hide marker on click
    }

}
