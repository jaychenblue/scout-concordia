package com.example.scoutconcordia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;

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
            Toast.makeText(this, email, Toast.LENGTH_LONG).show();
        }catch(ApiException e){
            Log.d("signInResult:failed", e.getMessage());
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
}