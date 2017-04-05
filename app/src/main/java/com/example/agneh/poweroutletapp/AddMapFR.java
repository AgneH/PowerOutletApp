package com.example.agneh.poweroutletapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by agneh on 2017-03-28.
 */

public class AddMapFR extends Fragment {

    interface Map_AddOutlet_Listener{
        void addOutletClicked();
    }

    private Map_AddOutlet_Listener addOutletListener;

    public AddMapFR() {
        //required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_add_map, container, false);


        Button btnDisplayOutlet = (Button) thisView.findViewById(R.id.btn_add_location);

        btnDisplayOutlet.setOnClickListener(new View.OnClickListener() {
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
