package com.example.scoutconcordia;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import static android.content.ContentValues.TAG;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CalendarActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;
    private String email = null;
    private String selectedCalendar = null;
    private static final int RC_SIGN_IN = 9001;
    private Calendar calendarService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn();
    }

    public void continueActivity(){
        showCalendarsList(calendarService);

        //use selectedCalendar as the id to fetch events
    }

    public void displayCalendar(String id){
        try {
            CalendarListEntry calendar = calendarService.calendarList().get(id).execute();
            Toast.makeText(this, calendar.getSummary(), Toast.LENGTH_LONG).show();

        }catch (Throwable t){}
    }

    public Dialog onCreateDialogSingleChoice(ArrayList<String> calendars, final HashMap<String, String> calendarIds) {

        final CharSequence[] cs = calendars.toArray(new CharSequence[calendars.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialThemeDialog);

        builder.setTitle("Select schedule calendar")
                .setCancelable(false)
                .setSingleChoiceItems(cs, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCalendar = cs[which].toString();
                    }
                })
                .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        displayCalendar(calendarIds.get(selectedCalendar));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent mapIntent = new Intent(CalendarActivity.this, MapsActivity.class);
                        startActivity(mapIntent);
                        CalendarActivity.this.overridePendingTransition(0, 0);
                    }
                });

        return builder.create();
    }

    public void showCalendarsList(Calendar service){
        HashMap<String, String> calendarIds = new HashMap<String, String>();
        ArrayList<String> calendars =  new ArrayList<>();
        try {
            String pageToken = null;
            do {
                CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
                List<CalendarListEntry> items = calendarList.getItems();

                for (CalendarListEntry calendarListEntry : items) {
                    //Log.d("CALENDARSSS: ",calendarListEntry.getSummary());
                    calendars.add(calendarListEntry.getSummary());
                    calendarIds.put(calendarListEntry.getSummary(), calendarListEntry.getId());
                }
                pageToken = calendarList.getNextPageToken();
            } while (pageToken != null);

//            return
        }
        catch(Throwable t){
        }
        Dialog dialog = onCreateDialogSingleChoice(calendars, calendarIds);
        dialog.show();
    }

    protected void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // result of the starActivityForResult in signIn()
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // Verifies if the requqest code is that of the startActivityForResult using signInIntent in signIn()
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInresult(task);
        }
    }

    // handles the behavious once the signning in has completed
    protected void handleSignInresult(Task<GoogleSignInAccount> task){
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            email = account.getEmail();
            new RetrieveAccessToken().execute();
            Toast.makeText(this, email, Toast.LENGTH_LONG).show();
        }catch(ApiException e){
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class RetrieveAccessToken extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String scopes = "oauth2:email";
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), email, scopes);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
                //startActivityForResult(e.getIntent(), REQ_SIGN_IN_REQUIRED);
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;
        }

        public Calendar createCalendarService(String s){
            JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
            NetHttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();

            GoogleCredential CREDENTIALS = new GoogleCredential.Builder().setTransport(new NetHttpTransport())
                    .setJsonFactory(JacksonFactory.getDefaultInstance())
                    .build();

            CREDENTIALS.setAccessToken(s);

            return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, CREDENTIALS)
                    .setApplicationName("ScoutConcordia").build();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            calendarService = createCalendarService(s);
            continueActivity();
        }
    }
}