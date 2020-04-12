package com.example.scoutconcordia.Activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.scoutconcordia.R;

import javax.annotation.Nullable;

public class SettingsFragment extends PreferenceFragment {

    /**
     *
     * @param savedInstanceState settings are saved to SharedPreferences so that information is
     *                           not lost when the activity is recreated
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Loads the preferences from preferences.xml
        addPreferencesFromResource(R.xml.preferences);
    }

}
