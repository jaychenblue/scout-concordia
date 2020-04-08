package com.example.scoutconcordia.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.scoutconcordia.R;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount account;
    private GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 9001; // request code for the signIn intent (onActivityResult)
    private Calendar service = null;    // Google Calendar Api service
    private ArrayList<String> calendarNames = new ArrayList<>();    // user's google calendars' name list
    private ArrayList<String> calendarIds = new ArrayList<>();    // user's google calendars' Ids list
    private List<Event> eventsList = new ArrayList<>(); // List of calendar events from the selected calendar
    private String selectedCalendarId = null;   // holds the id of the google calendar that the user selects in single choice dialog
    private java.util.Calendar calendar = java.util.Calendar.getInstance(); // calendar object for creating, mutating (set time, add days) Date objects
    private int[][] tableIds = null; // holds ids of text views (columns)  making up the table layout, [x][y], x is row , y is column

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
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this);

        // if user has signed out, sign in. Else read calendar. (Only works when user has signed out from the app).
        /* Does not work if user removes account from device, without signing out from the app, as getLastSignedInAccount(Context ctx) continues
           to return the account and not null. Although this does not seem to be a problem at first as it seems not having the account logged in
           on the device does not matter. But when request to read the calendar is made, an exception is thrown saying invalid credentials.
           This is handled in the catch block, where exception is thrown, by calling signOut and asking the user to sign in again. */
        /* Checking for granted scopes on the api client continues to return true even when permission has been revoked by the user. This is also handled
           inside the catch block by calling revokeAccess on the googleSignInClient, signing out and asking user to sign in again. */
        if(lastSignedInAccount == null || lastSignedInAccount.getAccount() == null) {
            signIn(); // sign in, create a RetrieveCalendar object in handleSignInResult
        }
        else{
            account = lastSignedInAccount;
            new RetrieveCalendars().execute();
        }

        //Toolbar on top of the page
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //set title of page
        getSupportActionBar().setTitle("Schedule");



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
    // single choice dialog, and first value is selected by default
    // dialog is not cancellable
    // user either has to select an option, or go back to MapsActivity by pressing cancel or back button
    public Dialog onCreateDialogSingleChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialThemeDialog);
        builder.setTitle("Select schedule calendar")
                .setCancelable(false);

        // if calendars found
        if(!calendarNames.isEmpty()){
            // char sequence holds the names of all calendars of the user
            final CharSequence[] cs = calendarNames.toArray(new CharSequence[calendarNames.size()]);
            selectedCalendarId = calendarIds.get(0);
            builder.setSingleChoiceItems(cs, 0, new DialogInterface.OnClickListener() {    //creates the selection list
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
        }
        else{
            builder.setMessage("No calendars found for the signed in Google account!");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent mapIntent = new Intent(CalendarActivity.this, MapsActivity.class);
                            startActivity(mapIntent);
                            CalendarActivity.this.overridePendingTransition(0, 0);
                         }
            });
        }
        return builder.create();
    }

    // - loops through every event in the events list
    // - verifies the day of the event, start time, and duration
    // - fills the table columns (textView) in the table layout overlapping the event timing
    // - Table structure is in TableLayout in activity_calendar.xml
    //      - first column is representing time, second column is current day, the column after is the day after and so on
    //          -> each column contains a textview
    //      -first row is date, second row is day
    //      - rows after are time sections (each row = 15 mins)
    private void displayTable() {
        tableIds = tableLayoutIds(); // ids of columns (text views) in the table table layout
        String[][] dateInfoObj = dateInfoObj(); // see method description
        fillDayDateRows(dateInfoObj);   // set date and day labels in table layout
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd"); // date formatter to compare Date object dates
        SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm"); // time formatter

        for (Event event : eventsList) {
            String eventName = null;
            String eventLocation = null;

            try{
                eventName = event.getSummary();
                eventLocation = event.getLocation();
            }
            catch(Throwable t){
                Log.d(TAG, t.getMessage());
            }

            try {
                Date startDate = new Date(event.getStart().getDateTime().getValue());   // start date of the event
                Date endDate = new Date(event.getEnd().getDateTime().getValue());   // end date of the event

                // event starts or ends outside school hours, continue without displaying
                if(getTimeHour(startDate) < 8 || getTimeHour(startDate) == 23 || getTimeHour(endDate) > 23){
                    continue;
                }
                // time difference between start and end of event in milliseconds
                long length = endDate.getTime() - startDate.getTime();

                // (length of event in mins)/15 mins to get the number of rows the event covers
                // each row is 15 mins
                int totalRows = (int)(length / 1000 / 60) / 15;

                /* convert date to "yyyy-MM-dd" string format to compare with date fit rom
                   datInfoObj as dates in dateInfoObj are stored in "yyyy-MM-dd" */
                String date = sdfDate.format(startDate);

                int startRow = eventStartRow(startDate, endDate);
                String start = sdfTime.format(startDate); // start time "h:mm"
                String end = sdfTime.format(endDate);     // end time "h:mm"
                String classHours = start + " - " + end;

                if (date.equals(dateInfoObj[0][1])) {   // event is today
                    fillEvent(0, startRow, totalRows, eventName, eventLocation, classHours, getResources().getColor(R.color.scheduleEventColorOption1));
                } else if (date.equals(dateInfoObj[1][1])) { //event is tomorrow
                    fillEvent(1, startRow, totalRows, eventName, eventLocation, classHours, getResources().getColor(R.color.scheduleEventColorOption2));
                } else if (date.equals(dateInfoObj[2][1])) { // event is the day after
                    fillEvent(2, startRow, totalRows, eventName, eventLocation, classHours, getResources().getColor(R.color.scheduleEventColorOption1));
                } else if (date.equals(dateInfoObj[3][1])) {
                    fillEvent(3, startRow, totalRows, eventName, eventLocation, classHours, getResources().getColor(R.color.scheduleEventColorOption2));
                } else if (date.equals(dateInfoObj[4][1])) {
                    fillEvent(4, startRow, totalRows, eventName, eventLocation, classHours, getResources().getColor(R.color.scheduleEventColorOption1));
                } else if (date.equals(dateInfoObj[5][1])) {
                    fillEvent(5, startRow, totalRows, eventName, eventLocation, classHours, getResources().getColor(R.color.scheduleEventColorOption2));
                } else if (date.equals(dateInfoObj[6][1])) {
                    fillEvent(6, startRow, totalRows, eventName, eventLocation, classHours, getResources().getColor(R.color.scheduleEventColorOption1));
                }
            }
            catch(Throwable t){
                Log.d(TAG, t.getMessage());
            };
        }
    }

    // sets the values for the date's row (first row) and day's row (second row) for all 7 columns
    // first column are set to today's date and day respectively, next column is next day
    // uses the dateInfoObj created with dateInfoObj() method to get date and days value for each of the 7 days starting current day
    private void fillDayDateRows(String[][] dateInfoObj){
        Resources r = getResources();
        String name = getPackageName();
        for(int i = 0; i < 7; ++i){
            int dayColumnId = r.getIdentifier("day" + i, "id", name);
            int dateColumnId = r.getIdentifier("date" + i, "id", name);
            TextView dayColumn = findViewById(dayColumnId);
            TextView dateColumn = findViewById(dateColumnId);
            dateColumn.setText(dateInfoObj[i][0]);
            dayColumn.setText(dateInfoObj[i][2]);
        }
    }

    // returns a multidimensional array containing string values for the day and date from current day up to the 7th day
    //String[][] dateInfoObj= new String[7][3]; // [x][0] = date (dd), [x][1] = date (yyyy-MM-dd), [x][2] = name of day
    // where x = 0 is today, x = 1 is tomorrow and so on.  0 <= x < 7
    private String[][] dateInfoObj(){
        String[][] dateInfoObj= new String[7][3];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());

        try {
            calendar.setTime(sdf.parse(today));
        } catch (Throwable t) {
            Log.d(TAG, t.getMessage());
        }

        for(int i = 0; i < 7; ++i) {
            dateInfoObj[i][0] = Integer.toString(calendar.get(java.util.Calendar.DATE));
            dateInfoObj[i][1] = sdf.format(calendar.getTime());
            dateInfoObj[i][2] = getDayOfWeek(calendar.get(java.util.Calendar.DAY_OF_WEEK));
            calendar.add(java.util.Calendar.DATE, 1);
        }
        return dateInfoObj;
    }

    // set text, text color, background for the text views overlapping the event timing
    // attach onClickListener to texteviews
    private void fillEvent(int eventDayColumn, int startRow, int totalRows, String summary, String location, String classHours, int colorId){
        try {
          for (int j = startRow; j < totalRows + startRow; ++j) {
              TextView textView = (TextView) findViewById(tableIds[eventDayColumn][j]);
              textView.setTextColor(getResources().getColor(R.color.white));
              textView.setBackgroundColor(colorId);
              if (j == startRow) {
                  textView.setText(removeStringOverflow(summary, 15));
              } else if (j == startRow + 1) {
                  textView.setText(removeStringOverflow(location, 15));
              } else if (j == startRow + 2) {
                  textView.setText(classHours);
              }
              attachOnClickListener(textView, location, summary, classHours);
          }
      }
      catch (Throwable t){
          Log.d(TAG, t.toString());
      }
    }

    private String removeStringOverflow (String str, int substringLength){
        try {
            if (str.length() <= substringLength) {
                return str;
            } else {
                return str.substring(0, substringLength) + "...";
            }
        }catch(Throwable t){
            Log.d(TAG, t.getMessage());
            return null;
        }
    }

    // displays directionsDailog created by calling directionsDialog(...)
    private void attachOnClickListener(TextView textView, final String location, final String summary, final String classHours){
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> items = new ArrayList<String>(Arrays.asList(location, summary, classHours));
                items.removeAll(Collections.singleton(null));

                directionsDialog(items.toArray(new String[0])).show();
            }
        });
    }

    // create a dialong displaying location, title, and hours of the event
    // Go button to get directions to the location
    // Cancel button to cancel dialog
    // dialog is also cancellable by pressing anywhere on the screen
    private Dialog directionsDialog(String[] items){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Go to Location")
                .setItems(items, null)
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("GO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        return builder.create();
    }

    // returns the row number that the event begins at (e.g. if event starts at 9, 5 will be returned, (9-8)*4 +(0/15)+1
    // begins at 8:00 = row 1, at 8:15 = row 2, and so on
    private int eventStartRow(Date start, Date end){
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(start);

        int startHours = c.get(java.util.Calendar.HOUR_OF_DAY);
        int startMins = c.get(java.util.Calendar.MINUTE);
        int startRow = (startHours - 8) * 4 + (startMins / 15) + 1;

        return startRow;
    }


    // returns a multidimensional array that holds ids of all text views for each column in the table Layout in activity_calendar.xml
    // [x][y], x = 0 is the first row
    // y = 1 (0 is not assigned) is the first column (from 8:00 to 8:15)
    private int[][] tableLayoutIds(){
        int[][] tableIds = new int[7][61];
        Resources r = getResources();
        String name = getPackageName();

        for (int i = 1; i < 61; i++) {
            tableIds[0][i] = r.getIdentifier("one" + i, "id", name);
            tableIds[1][i] = r.getIdentifier("two" + i, "id", name);
            tableIds[2][i] = r.getIdentifier("three" + i, "id", name);
            tableIds[3][i] = r.getIdentifier("four" + i, "id", name);
            tableIds[4][i] = r.getIdentifier("five" + i, "id", name);
            tableIds[5][i] = r.getIdentifier("six" + i, "id", name);
            tableIds[6][i] = r.getIdentifier("seven" + i, "id", name);
        }
        return tableIds;
    }

    // return the day of the name for a given date
    private String getDayOfWeek(int value){
        switch (value) {
            case java.util.Calendar.SUNDAY:
                return "SUN";
            case java.util.Calendar.MONDAY:
                return "MON";
            case java.util.Calendar.TUESDAY:
                return "TUE";
            case java.util.Calendar.WEDNESDAY:
                return "WED";
            case java.util.Calendar.THURSDAY:
                return "THU";
            case java.util.Calendar.FRIDAY:
                return "FRI";
            default:
                return "SAT";
        }
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

    // returns the hour value of time in Date object
    private int getTimeHour(Date date){
        calendar.setTime(date);
        return calendar.get(java.util.Calendar.HOUR_OF_DAY);
    }

    protected void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void signOut(){
        googleSignInClient.signOut();
    }

    // result of the startActivityForResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            // if the request code equals that of the signInIntent
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInresult(task);
            }
        }
    }

    // handles the behaviour once the sign in has completed
    protected void handleSignInresult(Task<GoogleSignInAccount> task) {
        try {
            account = task.getResult(ApiException.class);
            new RetrieveCalendars().execute();
        } catch (Throwable e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);    // removes the exit and enter animations when changing activities
    }

    // asynchronous task (if done on main thread, networkOnMainThread exception is thrown
    // - build a global google api calendar service
    //      (get an access token using the user's email and set this token to the credentials)
    // - retrieves a list of google calendars
    private class RetrieveCalendars extends AsyncTask<Void, Void, Void> {
        private boolean isSuccesful = true; // true when query is successful, set to false when an exception is thrown
        @Override
        protected Void doInBackground(Void... params) {
            String scopes = "oauth2:https://www.googleapis.com/auth/calendar.readonly";
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), account.getEmail(), scopes);
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
                    // retrieve calendars where user is the owner
                    CalendarList calendarList = service.calendarList().list().setMinAccessRole("owner").setPageToken(pageToken).execute();
                    List<CalendarListEntry> calendars = calendarList.getItems();

                    for(CalendarListEntry calendarListEntry : calendars) {
                        String name = calendarListEntry.getSummary();
                        String id = calendarListEntry.getId();
                        calendarNames.add(name);
                        calendarIds.add(id);
                    }
                    pageToken = calendarList.getNextPageToken();    // if there is no next page, token will be null
                }
                while (pageToken != null);
            } catch (Throwable t) {
                Log.e(TAG, t.getMessage());
                isSuccesful = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(v);
            // if query is successful, show selection dialog
            if(isSuccesful) {
                onCreateDialogSingleChoice().show();
            }
            // if not, exception was due to user revoking permission or removing account fromm device
            // revoke access, sign out, and sign in again.
            // this will make sure that user is logged in on the device and that they grant permission again.
            else{
                googleSignInClient.revokeAccess();
                signOut();
                signIn();
            }
        }
    }

    // asynchronous task (if done on main thread, networkOnMainThread exception is thrown
    // - uses the global google calendar api service created in RetrieveCalendars
    // - retrieves events of a google calendar  (within the interval today - today + 7)
    // - fill the global List<Event> eventsList to be then used to display the events
    private class RetrieveEvents extends AsyncTask<Void, Void, Void> {
        Date test = null;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // current date with time set to 00:00:00:00 (H:M:S:MS)
                // lower limit for events search query
                Date dateMin =  setTime(new Date());

                //  add 8 days to the lower limits
                calendar.add(java.util.Calendar.DATE, 8);

                // upper limit for events search query
                Date dateMax = calendar.getTime();
                test = dateMax;
                String pageToken = null;
                do {
                    // returns events within the date interval
                    Events events = service.events().list(selectedCalendarId)
                            .setTimeMin(new DateTime(dateMin))
                            .setTimeMax(new DateTime(dateMax))
                            .setSingleEvents(true)
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
            displayTable();
        }
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
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        // Handle item selection
        switch (menuItem.getItemId()) {
            case R.id.main_home:
                Intent mapIntent = new Intent(CalendarActivity.this, MapsActivity.class);
                startActivity(mapIntent);
                CalendarActivity.this.overridePendingTransition(0, 0);
                break;

            case R.id.main_schedule:
                break;

            case R.id.main_shuttle:
                Intent shuttleIntent = new Intent(CalendarActivity.this, ShuttleScheduleActivity.class);
                startActivity(shuttleIntent);
                CalendarActivity.this.overridePendingTransition(0, 0);
                break;

        }
        return false;
    }

}