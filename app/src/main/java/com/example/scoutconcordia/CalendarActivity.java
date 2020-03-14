package com.example.scoutconcordia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 9001; // request code for the signIn intent (onActivityResult request code)
    private String email = null;    // email address of user
    private Calendar service = null;    // Google Calendar Api service
    private ArrayList<String> calendarNames = new ArrayList<>();    // user's google calendars' name list
    private ArrayList<String> calendarIds = new ArrayList<>();    // user's google calendars' name list
    private List<Event> eventsList = new ArrayList<>(); // List of calendar events from the selected calendar
    private String selectedCalendarId = null;   // holds the id of the google calendar that the user has chosen to import
    private java.util.Calendar calendar = java.util.Calendar.getInstance(); // calendar object for creating mutating (set time, add days) Date objects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar.readonly")) // requesting google calendar scope
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_bar_activity_calendar);
        bottomNavigationView.setSelectedItemId(R.id.nav_schedule);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_map:
                        Intent mapIntent = new Intent(CalendarActivity.this, MapsActivity.class);
                        startActivity(mapIntent);
                        CalendarActivity.this.overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_schedule:
                        break;

                    case R.id.nav_shuttle:
                        Intent shuttleIntent = new Intent(CalendarActivity.this, ShuttleScheduleActivity.class);
                        startActivity(shuttleIntent);
                        CalendarActivity.this.overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
    }

    // dialog to display a selection list with all calendar entry names
    // single selection is allowed, and first value is selected by default
    // dialog is not cancellable
    // user either has to select an option, or go back to MapsActivity by pressing cancel or back button

    public Dialog onCreateDialogSingleChoice() {
        final CharSequence[] cs = calendarNames.toArray(new CharSequence[calendarNames.size()]);    // char sequence holds the names of all calendars of the user
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialThemeDialog);
        selectedCalendarId = calendarIds.get(0);
        builder.setTitle("Select schedule calendar")
                .setCancelable(false)
                .setSingleChoiceItems(cs, 0, new DialogInterface.OnClickListener() {    //creates the selection list
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCalendarId = calendarIds.get(which);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {    // cancel button
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent mapIntent = new Intent(CalendarActivity.this, MapsActivity.class);
                        startActivity(mapIntent);
                        CalendarActivity.this.overridePendingTransition(0, 0);
                    }
                })
                .setPositiveButton("Select", new DialogInterface.OnClickListener() {    // select button
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new RetrieveEvents().execute();
                    }
                });


        return builder.create();
    }

    // sets time of a Date type object to 00:00:00:00 (H:S:M:MS)
    private Date setTime(Date date){
        calendar.setTime(date);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    // creates a new sign in intent
    protected void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // result of the starActivityForResult for the singInIntent in signIn()
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the request code equals that of the signInIntent
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInresult(task);
        }
    }

    // handles the behaviour once the sign in has completed
    protected void handleSignInresult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            email = account.getEmail();
            new RetrieveCalendars().execute();  // async task to retrieve the list of google calendars
        } catch (Throwable e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);    // removes the exit adn enter animations when changing activities
    }

    // asynchronous task (if done on main thread, networkOnMainThread exception is thrown
    // - build a global google api calendar service to be then used to retrieve events from a calendar
    //      (get an access token using the user's email and set this token to the credentials used)
    // - retrieves a list of google calendars
    private class RetrieveCalendars extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String scopes = "oauth2:email";
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), email, scopes);
                JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
                NetHttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();

                GoogleCredential CREDENTIALS = new GoogleCredential.Builder().setTransport(new NetHttpTransport())
                        .setJsonFactory(JacksonFactory.getDefaultInstance())
                        .build();

                CREDENTIALS.setAccessToken(token);

                service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, CREDENTIALS)
                        .setApplicationName("ScoutConcordia").build();

                String pageToken = null;
                do {
                    CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
                    List<CalendarListEntry> calendars = calendarList.getItems();

                    for(CalendarListEntry calendarListEntry : calendars) {
                        String name = calendarListEntry.getSummary();
                        String id = calendarListEntry.getId();
                        calendarNames.add(name);
                        calendarIds.add(id);
                    }
                    pageToken = calendarList.getNextPageToken();    // if there is no next page token will be null
                }
                while (pageToken != null);
            } catch (Throwable t) {
                Log.e("TAG", t.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            onCreateDialogSingleChoice().show();
        }
    }

    // asynchronous task (if done on main thread, networkOnMainThread exception is thrown
    // - uses the global google calendar api servive
    // - retrieves a list of a google calendar events (within the interval today - today + 7)
    private class RetrieveEvents extends AsyncTask<Void, Void, Void> {
        Date test = null;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Date dateMin =  setTime(new Date());   // current date with time set to 00:00:00:00 (H:M:S:MS), lower limit for events search query
                /**
                 * 8 days are added to the date as time is set to 00:00:00:00 (H:M:S:MS)
                 * at time 00:00:00:00 (H:M:S:MS) date would change to the next date
                 */
                calendar.add(java.util.Calendar.DATE, 8);
                Date dateMax = calendar.getTime(); // upper limit for events search query, upper limit is 7 days from current day
                test = dateMax;
                String pageToken = null;
                do {
                    // returns events within the data interval from today to next 7 days
                    Events events = service.events().list(selectedCalendarId)
                            .setTimeMin(new DateTime(dateMin))
                            .setTimeMax(new DateTime(dateMax))
                            .setPageToken(pageToken)
                            .execute();
                    List<Event> eventItems= events.getItems();
                    eventItems.addAll(0, eventsList);
                    eventsList = eventItems;

                    pageToken = events.getNextPageToken();
                }
                while (pageToken != null);
            } catch (Throwable t) {
                Log.e(TAG, t.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
        }
    }

}