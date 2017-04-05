package com.example.agneh.poweroutletapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by agneh on 2017-03-28.
 */

public class DisplayOutletFR extends DialogFragment {

    private View thisView;
    public DisplayOutletFR() {
        //required empty public constructor
    }

    public static DisplayOutletFR newInstance(String title) {
        DisplayOutletFR frag = new DisplayOutletFR();
        Bundle args = new Bundle();
        // this is a way to send data to Dialog window
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_display_outlet, container,false);
        return thisView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // we do not necesserily need to fetch the title, but this shows how we can extract data
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        getDialog().setCanceledOnTouchOutside(true);
        getOutlet("8");
    }

    public void getOutlet(String s){
        new GetOutlet().execute(s);
    }


    public void uiGetOutlet(Outlet outlet){
        Log.d("out",outlet.toString());
        for(Comment comment : outlet.comments){
            Log.d("out",comment.toString());
        }
        //TODO:Write code to set UI elements
        TextView txtTitle = (TextView) thisView.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) thisView.findViewById(R.id.txtDescription);
        txtTitle.setText(outlet.getTitle());
        txtDescription.setText(outlet.getDescription());
    }

    private class GetOutlet extends AsyncTask<String,String,Outlet> {
        @Override
        protected Outlet doInBackground(String...params) {
            String outletid = params[0];
            String url = "http://lekrot.no/poapi/getoutlet.php";
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            HashMap<String,String> map = new HashMap<>();
            map.put("outletid",outletid);
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.POSTRequest,map);
            Log.d("Response: ", "> " + jsonStr);
            try{
                JSONObject jOutlet = new JSONObject(jsonStr);
                Outlet outlet = Functions.parseOutlet(jOutlet);
                return outlet;
            }catch (Exception ex){
                return null;
            }
        }
        @Override
        protected void onPostExecute(Outlet outlet) {
            super.onPostExecute(outlet);
            uiGetOutlet(outlet);
        }
    }

}
