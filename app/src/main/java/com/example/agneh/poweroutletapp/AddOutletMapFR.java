package com.example.agneh.poweroutletapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
    private double latitude = 60.153;
    private double longitude = 10.261;


    //Required empty public constructor
    public AddOutletMapFR() {}
    //Listener interface for opening add outlet fragment
    interface Map_AddOutlet_Listener{
        void addOutletClicked();
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
        //Current location zooming
        LatLng current = new LatLng(latitude, longitude);

        //ToDO: current location marker has to be reconfigured
        Marker myLocationMarker = mMap.addMarker(new MarkerOptions().position(current).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                MarkerOptions marker;

                if(!mChosen){
                    marker = new MarkerOptions().position(
                            new LatLng(point.latitude, point.longitude)).title("New Marker");
                    m = mMap.addMarker(marker);
                    mChosen = true;
                } else {
                    LatLng newLatlng = new LatLng(point.latitude,point.longitude);
                    m.setPosition(newLatlng);
                }

                // Context context = getApplicationContext();
                String text = point.latitude+"   "+point.longitude;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getActivity(), text, duration);
                toast.show();

                Double lat = point.latitude;
                Double lon = point.longitude;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_add_outlet_map, container, false);
        //add map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //set listener for add outlet location
        Button btnAddOutletLocation = (Button) thisView.findViewById(R.id.btn_add_location);

        btnAddOutletLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOutletListener.addOutletClicked();
            }
        });

        return thisView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.addOutletListener = (Map_AddOutlet_Listener) context;
    }

}
