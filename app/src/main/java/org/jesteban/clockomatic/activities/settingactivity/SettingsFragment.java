package org.jesteban.clockomatic.activities.settingactivity;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.jesteban.clockomatic.R;

public class SettingsFragment extends PreferenceFragment {
    public final static String KEY_PREFERENCE_SETALARM = "setalarm_checkbox";
    public final static String KEY_PREFERENCE_IMPORT_OLD_DATA="import_old_data_checkbox";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }



}