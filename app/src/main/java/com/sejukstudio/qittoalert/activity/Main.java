package com.sejukstudio.qittoalert.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.sejukstudio.qittoalert.R;

import fragment.QittoMap;
import helper.ParseHelper;

public class Main extends AppCompatActivity implements QittoMap.OnFragmentInteractionListener{


    private GoogleMap mMap;
    private Marker marker;
    //GPS
    private LocationManager locationManager;
    private String provider;
    private Location location;
    //HeatMap
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    //Others
    private Button btnRequest;
    private FloatingActionButton fab;
    private Location myLocation;

    //fragment
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeElements();

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, new QittoMap(), "QittoMap");
        fragmentTransaction.commit();


    }

    private void initializeElements() {
        new ParseHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}