package com.example.scoutconcordia.Activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.scoutconcordia.R;

import javax.annotation.Nullable;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

}
