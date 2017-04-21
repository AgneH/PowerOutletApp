package com.example.agneh.poweroutletapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import java.util.ArrayList;


/**
 * Created by agneh on 2017-03-28.
 */

public class HomeFR extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    //Listener for opening add outlet map fragment
    private Home_AddOutletMap_Listener addOutletMapListener;
    private GoogleMap mMap;
    private double latitude = 0.0;
    private double longitude = 0.0;

    //required empty public constructor
    public HomeFR() {}

    //Listener interface for opening add outlet map fragment
    interface Home_AddOutletMap_Listener {
        void addOutletMapClicked();
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
        //set map variable
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        getAllOutlets();
        zoomToLocation();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

            String tag = (String)marker.getTag();
            if(tag!=null) {
                showOutletDialog(tag); //tag inneholder outlet ID
            } else {
                marker.showInfoWindow();
            }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_home, container, false);
        //add map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //set Listener for Open new outlet map
        FloatingActionButton btnAddOutletMap = (FloatingActionButton) thisView.findViewById(R.id.abtn_add_outlet_map);
        btnAddOutletMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOutletMapListener.addOutletMapClicked();
            }
        });


        return thisView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.addOutletMapListener = (Home_AddOutletMap_Listener)context;
    }

    //opens a dialog to display a specific outlet
    //TODO: implement choosing a specific dialog
    private void showOutletDialog(String outletId){
        FragmentManager fm = getFragmentManager();
        DisplayOutletFR outletDialog = DisplayOutletFR.newInstance(outletId);
        outletDialog.show(fm, "Outlet Dialog");
    }

    // Here is a bunch of Torkel kode that i do not entirelly understand - comment please

    public void getAllOutlets(){
        new GetAllOutlets().execute();
    }

    private class GetAllOutlets extends AsyncTask<Void,Void,ArrayList<Outlet>> {
        @Override
        protected ArrayList<Outlet> doInBackground(Void...params) {
            String url = "http://lekrot.no/poapi/getoutlets.php";
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.POSTRequest);
            Log.d("Response: ", "> " + jsonStr);
            ArrayList<Outlet> outlets = parseOutlets(jsonStr);
            return outlets;
        }
        @Override
        protected void onPostExecute(ArrayList<Outlet> outlets) {
            super.onPostExecute(outlets);
            uiGetAllOutlets(outlets);
        }
    }

    public ArrayList<Outlet> parseOutlets(String json){
        if(json!=null){
            try{
                ArrayList<Outlet> outlets = new ArrayList<>();
                JSONArray jOutlets = new JSONArray(json);
                for (int i = 0; i < jOutlets.length(); i++) {
                    Outlet outlet = Functions.parseOutlet(jOutlets.getJSONObject(i));
                    outlets.add(outlet);
                }
                return outlets;
            }catch(Exception ex){
                return null;
            }
        }else{
            return null;
        }
    }

    public void uiGetAllOutlets(ArrayList<Outlet> outlets){
        for(Outlet outlet : outlets){
            Log.d("out",outlet.toString());
            setMarker(outlet.getLat(), outlet.getLon(), outlet.getTitle(), outlet.getOutletid());
        }
    }

    public void setMarker(Double lat, Double lon, String tittel, String id){
        LatLng ny = new LatLng(lat,lon);
        Marker nyMarker= mMap.addMarker(new MarkerOptions().position(ny).title(tittel));
        nyMarker.setTag(id);
    }

    public void zoomToLocation(){
        LatLng current = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14));
    }

    public void setCoordinates(double latid, double longit){
        latitude = latid;
        longitude = longit;
    }



}
