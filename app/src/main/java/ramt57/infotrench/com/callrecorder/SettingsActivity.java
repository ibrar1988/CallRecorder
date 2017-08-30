package ramt57.infotrench.com.callrecorder;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;
public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        EditTextPreference editTextPreference;
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            editTextPreference= (EditTextPreference)findPreference("CallRecorder");
            SharedPreferences getWeightAndAgeStore = getActivity().getSharedPreferences("CallRecorder", Context.MODE_PRIVATE);

        }
    }
//    SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//    String strUserName = SP.getString("username", "NA");
//    boolean bAppUpdates = SP.getBoolean("applicationUpdates",false);
//    String downloadType = SP.getString("downloadType","1");
}
