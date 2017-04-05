package com.example.agneh.poweroutletapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by agneh on 2017-03-28.
 */

public class DisplayOutletFR extends DialogFragment {

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
        View thisView = inflater.inflate(R.layout.fragment_display_outlet, container,false);
        return thisView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // we do not necesserily need to fetch the title, but this shows how we can extract data
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        getDialog().setCanceledOnTouchOutside(true);
    }
}
