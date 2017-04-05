package com.example.agneh.poweroutletapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by agneh on 2017-03-28.
 */

public class AddOutletFR extends Fragment {
    public AddOutletFR() {
        //required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_outlet, container, false);
    }



    public void insertOutlet(double lat, double lon,String title, String description){
        new InsertOutlet().execute(Double.toString(lat),Double.toString(lon),title,description);
    }


    public void uiInsertOutlet(String outletid){
        if(outletid!=null)
            Log.d("out",outletid);
        else Log.d("out","something went wrong");
        //TODO:Write code to set UI elements

    }



    private class InsertOutlet extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String...params) {
            String lat = params[0];
            String lon = params[1];
            String title = params[2];
            String description = params[3];
            String url = "http://lekrot.no/poapi/insertoutlet.php";
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            HashMap<String,String> map = new HashMap<>();
            map.put("lat",lat);
            map.put("lon",lon);
            map.put("title",title);
            map.put("description",description);
            String result = webreq.makeWebServiceCall(url, WebRequest.POSTRequest,map);
            Log.d("Response: ", "> " + result);
            return Functions.tryParseInt(result) ? result : null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            uiInsertOutlet(result);
        }
    }

}
