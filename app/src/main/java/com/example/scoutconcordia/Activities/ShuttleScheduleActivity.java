package com.example.scoutconcordia.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scoutconcordia.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShuttleScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle_schedule);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_bar_activity_shuttle_schedule);
        bottomNavigationView.setSelectedItemId(R.id.nav_shuttle);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
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

                    case R.id.nav_shuttle:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}