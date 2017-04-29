package com.example.agneh.poweroutletapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by agneh on 2017-03-28.
 */

public class AddOutletMapFR extends Fragment implements OnMapReadyCallback{
    //Listener for opening add outlet fragment
    private Map_AddOutlet_Listener addOutletListener;
    private GoogleMap mMap;
    //Map point chosen
    boolean mChosen = false;
    Marker m;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private double latChosen = 0.0;
    private double longChosen = 0.0;



    //Required empty public constructor
    public AddOutletMapFR() {}
    //Listener interface for opening add outlet fragment
    interface Map_AddOutlet_Listener{
        void addOutletClicked(double latitude, double longitude);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //Map load functions go here
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        zoomToLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                MarkerOptions marker;
                if(!mChosen){
                    marker = new MarkerOptions().position(
                            new LatLng(point.latitude, point.longitude)).title("Your power outlet").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                    latChosen=point.latitude;
                    longChosen=point.longitude;
                    m = mMap.addMarker(marker);
                    m.showInfoWindow();
                    mChosen = true;
                } else {
                    LatLng newLatlng = new LatLng(point.latitude,point.longitude);
                    latChosen=point.latitude;
                    longChosen=point.longitude;
                    m.setPosition(newLatlng);
                    m.showInfoWindow();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_add_outlet_map, container, false);
        //add map fragment
        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.addoutletmap, mapFragment);
        transaction.commit();
        mapFragment.getMapAsync(this);
        //set listener for add outlet location
        Button btnAddOutletLocation = (Button) thisView.findViewById(R.id.btn_add_location);

        btnAddOutletLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latChosen != 0 && longChosen!=0) {
                    addOutletListener.addOutletClicked(latChosen, longChosen);
                    mChosen=false;
                }else{
                    Toast.makeText(getContext(),"Choose a location by clicking on the map",Toast.LENGTH_SHORT).show();
                }
            }
        });
        getActivity().invalidateOptionsMenu();
        return thisView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.addOutletListener = (Map_AddOutlet_Listener) context;
    }


    public void zoomToLocation(){
        LatLng current = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16));
    }

    public void setCoordinates(double latid, double longit) {
        latitude = latid;
        longitude = longit;
    }


}
