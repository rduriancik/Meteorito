package com.example.robertduriancik.meteorito.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.robertduriancik.meteorito.BuildConfig;
import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.fragments.MapFragment;
import com.example.robertduriancik.meteorito.fragments.MeteoriteListFragment;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MeteoriteListFragment.OnMeteoriteListFragmentInteractionListener {

    private FragmentManager mFragmentManager;

    @Nullable
    @BindView(R.id.fragment_container_large)
    FrameLayout mFragmentContainerLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            showAboutDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showAboutDialog() {
        @SuppressLint("InflateParams") View aboutView = getLayoutInflater().inflate(R.layout.about_dialog, null, false);

        TextView textView = (TextView) aboutView.findViewById(R.id.about_version);
        textView.setText("v" + BuildConfig.VERSION_NAME);

        final AlertDialog aboutDialog = new AlertDialog.Builder(this)
                .setView(aboutView)
                .setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dialog closed
                    }
                })
                .show();
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

        transaction.addToBackStack(null)
                .commit();
    }

    @Override
    public void onDataLoaded(MeteoriteLanding meteoriteLanding) {
        if (mFragmentContainerLarge != null) {
            MapFragment mapFragment = MapFragment.newInstance(meteoriteLanding);

            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_large, mapFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
