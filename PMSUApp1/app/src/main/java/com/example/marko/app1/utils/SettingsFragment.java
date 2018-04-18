package com.example.marko.app1.utils;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.marko.app1.R;

/**
 * Created by Marko on 4/17/2018.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

    }

}
