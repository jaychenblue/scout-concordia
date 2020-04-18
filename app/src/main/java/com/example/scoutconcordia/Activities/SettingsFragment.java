package com.example.scoutconcordia.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.scoutconcordia.R;

import java.util.prefs.PreferenceChangeListener;

import javax.annotation.Nullable;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SettingsFragment extends PreferenceFragment  {

    private static boolean disabilityPreferences = false;//static variable that will be used in the MapsActivity for accessibility settings
    private Context mContext;
    private Activity mActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //display preferences
        addPreferencesFromResource(R.xml.preferences);

        //get application context
        mContext = this.getActivity();
        mActivity = this.getActivity();

        /**
         * SwitchPreference stores the boolean in the SharedPreferences
         * This ensures that the state of the switch is saved when the app is restarted.
         */
        final SwitchPreference accessibility = (SwitchPreference) findPreference(this.getResources()
                .getString(R.string.AccessibilitySettings));


        /**
            void setOnPreferenceChangeListener (Preference.OnPreferenceChangeListener onPreferenceChangeListener)
                Sets the callback to be invoked when this Preference is changed by the user
                (but before the internal state has been updated).

            Parameters
                onPreferenceChangeListener Preference.OnPreferenceChangeListener: The callback to be invoked.
        */

        // SwitchPreference preference change listener
        accessibility.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if(accessibility.isChecked()){
                    // Checked the switch programmatically
                    accessibility.setChecked(false);
                    disabilityPreferences = false; //accessibility settings not needed
                    Toast.makeText(mActivity,"Disability Preference variable value:" +disabilityPreferences,Toast.LENGTH_SHORT).show();


                }else {

                    // Unchecked the switch programmatically
                    accessibility.setChecked(true);
                    disabilityPreferences = true; //user wants accessibility settings on
                    Toast.makeText(mActivity,"Disability preference variable value: " + disabilityPreferences,Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });
    }

    static public Boolean getDisabilityPreference() {
        return disabilityPreferences;
    }


}

