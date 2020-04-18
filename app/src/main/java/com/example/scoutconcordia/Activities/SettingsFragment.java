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
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.scoutconcordia.R;

import java.util.prefs.PreferenceChangeListener;

import javax.annotation.Nullable;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SettingsFragment extends PreferenceFragment  {

    public static final String PREF_ACCESSIBILITY = "accessibility_settings";
    private static boolean disabilityPreferences = false;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    Context context = getContext();
    private Context mContext;
    private Activity mActivity;


    // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences (context);
    //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        mContext = this.getActivity();
        mActivity = this.getActivity();

        final SwitchPreference accessibility = (SwitchPreference) findPreference(this.getResources()
                .getString(R.string.AccessibilitySettings));


        /*
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
                    Toast.makeText(mActivity,"Unchecked",Toast.LENGTH_SHORT).show();

                    // Checked the switch programmatically
                    accessibility.setChecked(false);
                }else {
                    Toast.makeText(mActivity,"Checked",Toast.LENGTH_SHORT).show();

                    // Unchecked the switch programmatically
                    accessibility.setChecked(true);
                }
                return false;
            }
        });

//        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener()
//        {
//            @Override
//            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
//            {
//                if(key.equals(PREF_ACCESSIBILITY))
//                {
//
//
//                    //   accessibilityPreference.getSharedPreferences();
//                    SwitchPreference accessibilityPreference = (SwitchPreference)findPreference(key);
//
//                    if(accessibilityPreference.isEnabled()){
//                        accessibilityPreference.setSummaryOn("SWITCH");
//                        disabilityPreferences = true;
//
//                    } else if (!accessibilityPreference.isEnabled()){
//                        accessibilityPreference.setSummaryOff("SWITCH IS OFF");
//                        disabilityPreferences = false;
//                    }
//
//
//
//                }
//            }
//        };

    }

    public static boolean getDisabilityPreference(){
        return disabilityPreferences;
    }

    @Override
    public void onResume()
    {
        super.onResume();

       // getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
//        SwitchPreference accessibilityPreference = (SwitchPreference)findPreference(PREF_ACCESSIBILITY);
//
//        if(accessibilityPreference.isEnabled()){
//            accessibilityPreference.setSummaryOn("SWITCH");
//            disabilityPreferences = true;
//
//        } else {
//            accessibilityPreference.setSummaryOff("SWITCH IS OFF");
//            disabilityPreferences = false;
//        }

    }

    @Override
    public void onPause()
    {
        super.onPause();
    //    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }



}

