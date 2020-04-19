package com.example.scoutconcordia.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.scoutconcordia.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShuttleScheduleActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle_schedule);

        //Toolbar on top of the page
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //set title of page
        getSupportActionBar().setTitle("ScoutConcordia");


        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_bar_activity_shuttle_schedule);
        bottomNavigationView.setSelectedItemId(R.id.nav_shuttle);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.nav_map:
                        Intent mapIntent = new Intent(ShuttleScheduleActivity.this, MapsActivity.class);
                        startActivity(mapIntent);
                        ShuttleScheduleActivity.this.overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_schedule:
                        Intent calendarIntent = new Intent(ShuttleScheduleActivity.this, CalendarActivity.class);
                        startActivity(calendarIntent);
                        ShuttleScheduleActivity.this.overridePendingTransition(0, 0);
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    /**
     * This is the method that inflates the menu
     * @param menu This is the first paramter to onCreateOptionsMenu method.
     * @return boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * This is the method that Handling menu clicks
     * @param menuItem This is the first paramter to onOptionsItemSelected method.
     * @return boolean.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem)
    {
        // Handle item selection
        switch (menuItem.getItemId()) {
            case R.id.main_home:
                Intent mapIntent = new Intent(ShuttleScheduleActivity.this, MapsActivity.class);
                startActivity(mapIntent);
                ShuttleScheduleActivity.this.overridePendingTransition(0, 0);
                break;

            case R.id.main_schedule:
                Intent calendarIntent = new Intent(ShuttleScheduleActivity.this, CalendarActivity.class);
                startActivity(calendarIntent);
                ShuttleScheduleActivity.this.overridePendingTransition(0, 0);
                break;
            case R.id.main_shuttle:
               break;

            case R.id.main_settings:
                Intent settingsIntent = new Intent(ShuttleScheduleActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                ShuttleScheduleActivity.this.overridePendingTransition(0,0);
                break;

            default:
            return super.onOptionsItemSelected(menuItem);

        }
        return false;
    }
}