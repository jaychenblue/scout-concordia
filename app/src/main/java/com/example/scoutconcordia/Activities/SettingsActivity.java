package com.example.scoutconcordia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.scoutconcordia.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.services.calendar.model.Setting;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Toolbar on top of the page
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //set title of page
        getSupportActionBar().setTitle("Settings");

        if(findViewById(R.id.fragment_container)!= null){
            if(savedInstanceState !=null)
                return;

            getFragmentManager().beginTransaction().add(R.id.fragment_container, new SettingsFragment()).commit();
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bar_activity_maps);
        bottomNavigationView.setSelectedItemId(R.id.nav_map);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_map:
                        Intent mapsIntent = new Intent(SettingsActivity.this, MapsActivity.class);
                        startActivity(mapsIntent);
                        SettingsActivity.this.overridePendingTransition(0,0);
                        break;

                    case R.id.nav_schedule:
                        Intent calendarIntent = new Intent(SettingsActivity.this, CalendarActivity.class);
                        startActivity(calendarIntent);
                        SettingsActivity.this.overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_shuttle:
                        Intent shuttleIntent = new Intent(SettingsActivity.this, ShuttleScheduleActivity.class);
                        startActivity(shuttleIntent);
                        SettingsActivity.this.overridePendingTransition(0, 0);
                        break;

                    default:
                        break;
                }
                return false;
            }
        });


    }


    //inflates the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    //Handling menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle item selection
        switch (menuItem.getItemId()) {
            case R.id.main_home:
                Intent mapsIntent = new Intent(SettingsActivity.this, MapsActivity.class);
                startActivity(mapsIntent);
                SettingsActivity.this.overridePendingTransition(0, 0);
                break;

            case R.id.main_schedule:
                Intent calendarIntent = new Intent(SettingsActivity.this, CalendarActivity.class);
                startActivity(calendarIntent);
                SettingsActivity.this.overridePendingTransition(0, 0);
                break;

            case R.id.main_settings:
                break;

            case R.id.main_shuttle:
                Intent shuttleIntent = new Intent(SettingsActivity.this, ShuttleScheduleActivity.class);
                startActivity(shuttleIntent);
                SettingsActivity.this.overridePendingTransition(0, 0);
                break;

            default:
                return super.onOptionsItemSelected(menuItem);

        }
        return false;
    }

}
