package com.example.agneh.poweroutletapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements HomeFR.Home_AddOutletMap_Listener, AddOutletMapFR.Map_AddOutlet_Listener{

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //adds home fragment when activity is created
        fragment = new HomeFR();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,fragment);
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Populates Menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                ft.commit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //  @Override
    public void addOutletMapClicked() {
        //Adds Add Outlet Map fragment to the main window
        fragment = new AddOutletMapFR();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void addOutletClicked() {
        //Adds Add Outlet fragment to the main window
        fragment = new AddOutletFR();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_content,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}

