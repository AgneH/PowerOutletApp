package com.example.agneh.poweroutletapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Populates Menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //No other choices as for today. TO DO: remove switch if we do not come across other options
        switch(item.getItemId()){
            case R.id.Home:
                Toast.makeText(this, "You clicked Home" , Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }




}
