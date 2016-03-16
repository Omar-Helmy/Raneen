package com.omar.exapmle.raneen;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Omar on 15/03/2016.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_screen);
    }
}