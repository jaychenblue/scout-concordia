package com.example.scoutconcordia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;



public class FromToActivity extends Activity {

    public static final String[] locations = new String[]{
            "B Building", "CI Building", "CL Building", "D Building", "EN Building"
            , "ER Building", "EV Building", "FA Building", "FB Building", "FG Building"
            , "GA Building", "GM Building", "GN Building", "GS Building",
            "H Building", "K Building", "LB Building", "LD Building", "LS Building",
            "M Building", "MB Building", "MI Building", "MU Building", "P Building", "PR Building",
            "Q Building", "R Building", "RR Building", "S Building", "SB Building", "T Building",
            "TD Building", "V Building", "VA Building", "X Building", "Z Building",
            "AD Building", "BB Building", "BH Building", "CC Building", "CJ Building",
            "DO Building", "FC Building", "GE Building", "HA Building", "HB Building",
            "HC Building", "HU Building", "JR Building", "PC Building", "PS Building",
            "PT Building", "PY Building", "RA Building", "RF Building", "SC Building",
            "SH Building", "SI Building", "SP Building", "TA Building", "TB Building",
            "VE Building", "VL Building"};

    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup);

        AutoCompleteTextView fromSearchBar = findViewById(R.id.fromSearchBar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, locations);
        fromSearchBar.setAdapter(adapter);

        AutoCompleteTextView toSearchBar = findViewById(R.id.toSearchBar);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,locations);
        toSearchBar.setAdapter(adapter2);
    }

    public void byDriving(View view) {
        AutoCompleteTextView fromSearch = findViewById(R.id.fromSearchBar);
        String from = fromSearch.getText().toString();
        AutoCompleteTextView toSearch = findViewById(R.id.toSearchBar);
        String to = toSearch.getText().toString();

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("to", to);
        intent.putExtra("mode", "driving");
        startActivity(intent);
    }

    public void byWalking(View view) {
        AutoCompleteTextView fromSearch = findViewById(R.id.fromSearchBar);
        String from = fromSearch.getText().toString();
        AutoCompleteTextView toSearch = findViewById(R.id.toSearchBar);
        String to = toSearch.getText().toString();

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("to", to);
        intent.putExtra("mode", "walking");
        startActivity(intent);
    }

    public void byBus(View view) {
        AutoCompleteTextView fromSearch = findViewById(R.id.fromSearchBar);
        String from = fromSearch.getText().toString();
        AutoCompleteTextView toSearch = findViewById(R.id.toSearchBar);
        String to = toSearch.getText().toString();

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("to", to);
        intent.putExtra("mode", "bus");
        startActivity(intent);
    }


}
