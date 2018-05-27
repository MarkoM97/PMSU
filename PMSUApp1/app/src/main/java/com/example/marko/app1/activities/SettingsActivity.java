package com.example.marko.app1.activities;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.marko.app1.R;
import com.example.marko.app1.utils.AppCompatPreferenceActivity;
import com.example.marko.app1.utils.DatePreference;

import java.util.List;

public class SettingsActivity extends AppCompatPreferenceActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addPreferencesFromResource(R.xml.preferences);



        final ListPreference listPreferencePosts = (ListPreference) findPreference("sort_posts");
        final ListPreference listPreferenceComments = (ListPreference) findPreference("sort_comments");
        final DatePreference datePreference = (DatePreference) findPreference("date_filter");
        //ListPreference listPreferenceComments = (Lis)findPreference("sort_comments");

        Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
//                graficki postavlja setovanu vrednost
                listPreferencePosts.setValue(o.toString());
//                ispisuje setovanu vrednost

                Toast.makeText(getApplication(),listPreferencePosts.getValue(), Toast.LENGTH_SHORT).show();
                return false;
            }
        };

        Preference.OnPreferenceChangeListener listener1 = new Preference.OnPreferenceChangeListener(){

            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                listPreferenceComments.setValue(o.toString());
                Toast.makeText(getApplication(),listPreferenceComments.getValue(),Toast.LENGTH_SHORT).show();
                return false;
            }
        };

        DatePreference.OnPreferenceChangeListener listener2 = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                datePreference.setText(o.toString());
                String currentSelected = datePreference.getText();
                Log.d("CURRENT DATE", currentSelected);


                Toast.makeText(getApplication(), "Posts visible from : " + currentSelected, Toast.LENGTH_SHORT).show();
                return false;
            }
        };

        listPreferencePosts.setOnPreferenceChangeListener(listener);
        listPreferenceComments.setOnPreferenceChangeListener(listener1);
        datePreference.setOnPreferenceChangeListener(listener2);

        //Ispravan nacin
        //getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    public long getSelectedItemId() {
        return super.getSelectedItemId();
    }


}
