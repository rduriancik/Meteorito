package com.example.robertduriancik.meteorito.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.fragments.MapFragment;
import com.example.robertduriancik.meteorito.fragments.MeteoriteListFragment;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;

public class MainActivity extends AppCompatActivity
        implements MeteoriteListFragment.OnMeteoriteListFragmentInteractionListener {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        MeteoriteListFragment meteoriteListFragment = new MeteoriteListFragment();
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, meteoriteListFragment)
                .commit();
    }

    @Override
    public void onMeteoriteListItemClick(MeteoriteLanding meteoriteLanding) {
        MapFragment mapFragment = MapFragment.newInstance(meteoriteLanding);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, mapFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
