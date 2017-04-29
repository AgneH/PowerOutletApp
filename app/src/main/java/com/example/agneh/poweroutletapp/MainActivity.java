package com.example.agneh.poweroutletapp;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CommentDialog.CommentDialogListener, HomeFR.Home_AddOutletMap_Listener, AddOutletMapFR.Map_AddOutlet_Listener, com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    Fragment fragment;

    //Current loc
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    double latitude = 0.0;
    double longitude = 0.0;
    int rotation;




    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Current loc
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //adds home fragment when activity is created
        fragment = new HomeFR();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,fragment);
        ft.commit();
    }

    //Current loc
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    //Current loc
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    //Current loc
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {


                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.main_content);
                if (f instanceof HomeFR) {
                    ((HomeFR) f).setCoordinates(latitude, longitude);
                    ((HomeFR) f).zoomToLocation();
                }
            }
        }
    }


    //Current loc
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try{
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient, mLocationRequest, this);

                    }catch(SecurityException e){
                        Toast.makeText(MainActivity.this,
                                "SecurityException:\n" + e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,
                            "Permission denied",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Current loc
    @Override
    public void onConnectionSuspended(int i) {

    }

    //Current loc
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this,
                "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
    }


    //Current loc
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

            latitude = location.getLatitude();
            longitude= location.getLongitude();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Populates Menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //Enable Button Visibility

        Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.main_content);
        menu.findItem(R.id.Home).setVisible(!(f instanceof HomeFR));
        menu.findItem(R.id.Zoom).setVisible((f instanceof HomeFR)||(f instanceof AddOutletMapFR));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Ads Home button to the menu
        //No other choices as for today. TO DO: remove switch if we do not come across other options
        switch(item.getItemId()){
            case R.id.Home:
                fragment = new HomeFR();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_content,fragment);
                ((HomeFR) fragment).setCoordinates(latitude, longitude);
                ft.commit();
                break;

            case R.id.Zoom:
                Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.main_content);
                if (f instanceof HomeFR) {
                    ((HomeFR) f).setCoordinates(latitude, longitude);
                    ((HomeFR) f).zoomToLocation();
                }
                if (f instanceof AddOutletMapFR) {
                    ((AddOutletMapFR) f).setCoordinates(latitude, longitude);
                    ((AddOutletMapFR) f).zoomToLocation();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //  @Override
    public void addOutletMapClicked(float zoomlevel) {
        //Adds Add Outlet Map fragment to the main window
        fragment = new AddOutletMapFR();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,fragment);
        ((AddOutletMapFR) fragment).setZoomLevel(zoomlevel);
        ((AddOutletMapFR) fragment).setCoordinates(latitude, longitude);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void addOutletClicked(double latitude, double longitude) {
        //Adds Add Outlet fragment to the main window
        fragment = new AddOutletFR();
        Bundle bundle = new Bundle();

        rotation = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        bundle.putInt("orientation", rotation);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,fragment);
        ((AddOutletFR) fragment).setLocation(latitude, longitude);
       // ft.addToBackStack(null);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        fragment.setArguments(bundle);
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public void OnFinishCommentDialog(String outletid, String comment, int upvote) {
        insertComment(outletid,comment,upvote);
    }

    //use this to insert a comment to the db
    //refer to uiInsertComment if you need the result
    public void insertComment(String outletid,String comment,int upvote){
        new InsertComment().execute(outletid,comment,Integer.toString(upvote));
    }

    //runs after insertComment, use this if you need to do anything with the result
    public void uiInsertComment(String commentid){
        if(commentid!=null){
            Log.d("out",commentid);
            Toast.makeText(getBaseContext(),"Comment added",Toast.LENGTH_SHORT).show();
            //Update ui of the fragment
            DisplayOutletFR frag =(DisplayOutletFR) getSupportFragmentManager().findFragmentByTag("Outlet Dialog");
            frag.getOutlet(frag.outletid);
        }else{
            Toast.makeText(getBaseContext(),"Something went wrong. Could not add comment.",Toast.LENGTH_SHORT).show();
            Log.d("out","something went wrong");
        }
    }

    private class InsertComment extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String...params) {
            String outletid = params[0];
            String comment = params[1];
            String upvote = params[2];
            String url = "http://lekrot.no/poapi/insertcomment.php";
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            HashMap<String,String> map = new HashMap<>();
            map.put("outletid",outletid);
            map.put("comment",comment);
            map.put("upvote",upvote);
            String result = webreq.makeWebServiceCall(url, WebRequest.POSTRequest,map);
            Log.d("Response: ", "> " + result);
            return Functions.tryParseInt(result) ? result : null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            uiInsertComment(result);
        }
    }
}

