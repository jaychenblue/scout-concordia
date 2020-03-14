package com.example.scoutconcordia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CalendarActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 9001; // request code for the signIn intent (onActivityResult request code)

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
            Toast.makeText(getApplicationContext(),account.getEmail(), Toast.LENGTH_LONG).show();
        } catch (Throwable e) {
            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}