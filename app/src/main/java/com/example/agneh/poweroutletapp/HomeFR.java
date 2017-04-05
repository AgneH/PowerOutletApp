package com.example.agneh.poweroutletapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by agneh on 2017-03-28.
 */

public class HomeFR extends Fragment {
    public HomeFR() {
        //required empty public constructor
    }

    interface Home_AddMap_Listener {
        void addMapClicked();
    }

    private Home_AddMap_Listener addMapListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnDisplayOutlet = (Button) thisView.findViewById(R.id.btn_display_location);

        btnDisplayOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOutletDialog();
            }
        });

        FloatingActionButton btnAddMap = (FloatingActionButton) thisView.findViewById(R.id.abtn_add_location_map);
        btnAddMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMapListener.addMapClicked();
            }
        });

        return thisView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.addMapListener = (Home_AddMap_Listener)context;
    }


    private void showOutletDialog(){
        FragmentManager fm = getFragmentManager();
        DisplayOutletFR outletDialog = DisplayOutletFR.newInstance("Some Tittle");
        outletDialog.show(fm, "fragment");
    }



}
