package com.example.robertduriancik.meteorito.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.fragments.MapFragment;
import com.example.robertduriancik.meteorito.fragments.MeteoriteListFragment;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity
        implements MeteoriteListFragment.OnMeteoriteListFragmentInteractionListener {

    private FragmentManager mFragmentManager;

    @BindView(R.id.fragment_container_large)
    FrameLayout mFragmentContainerLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        if (mFragmentContainerLarge == null) {
            // Protection against fragment overlapping
            if (savedInstanceState != null) {
                return;
            }

            MeteoriteListFragment meteoriteListFragment = new MeteoriteListFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, meteoriteListFragment)
                    .commit();
        }
    }

    @Override
    public void onMeteoriteListItemClick(MeteoriteLanding meteoriteLanding) {
        MapFragment mapFragment = MapFragment.newInstance(meteoriteLanding);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mFragmentContainerLarge == null) {
            transaction.replace(R.id.fragment_container, mapFragment);

        } else {
            transaction.replace(R.id.fragment_container_large, mapFragment);
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }
}
