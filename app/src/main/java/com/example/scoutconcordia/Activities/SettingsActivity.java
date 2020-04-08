package com.example.scoutconcordia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.scoutconcordia.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Toolbar on top of the page
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


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
                Intent settingsIntent = new Intent(SettingsActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                SettingsActivity.this.overridePendingTransition(0,0);
                break;

            case R.id.main_shuttle:
                Intent shuttleIntent = new Intent(SettingsActivity.this, ShuttleScheduleActivity.class);
                startActivity(shuttleIntent);
                SettingsActivity.this.overridePendingTransition(0, 0);
                break;

        }
        return false;
    }

}
