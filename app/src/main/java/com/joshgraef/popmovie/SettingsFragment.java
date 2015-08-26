package com.joshgraef.popmovie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/***************************************************************************************************
 * Author:          jsgraef
 * Date:            26-Aug-2015
 * Description:     Handles the preferences for the app which are right now, just sorting preferences
 **************************************************************************************************/
public class SettingsFragment extends PreferenceFragment {

    private SharedPreferences.OnSharedPreferenceChangeListener spChanged =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if (key.equals(getString(R.string.pref_sort_by))) {
                        Preference p = findPreference(key);
                        p.setSummary(sharedPreferences.getString(key, getString(R.string.pref_sort_by)));
                    }
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_main);

        findPreference(getString(R.string.pref_sort_by))
                .setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .getString(getString(R.string.pref_sort_by), getString(R.string.pref_sort_by_popularity)));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(spChanged);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(spChanged);
    }

}
