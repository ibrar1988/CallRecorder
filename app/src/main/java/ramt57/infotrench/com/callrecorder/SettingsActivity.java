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
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.util.List;
public class SettingsActivity extends AppCompatPreferenceActivity {
    static Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx=this;
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
            Preference button = findPreference("DIRECTORY");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent chooserIntent = new Intent(ctx, DirectoryChooserActivity.class);
                    DirectoryChooserConfig config = DirectoryChooserConfig.builder().newDirectoryName("CallRecorder")
                            .allowNewDirectoryNameModification(true)
                            .build();
                    chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
                    startActivityForResult(chooserIntent, 1001);
                    return true;
                }
            });
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1001) {
                if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                    SharedPreferences filepreference=ctx.getSharedPreferences("DIRECTORY",MODE_PRIVATE);
                    SharedPreferences.Editor editor=filepreference.edit();
                    editor.putString("DIR",data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR));
                    editor.apply();
                    Intent intent=new Intent(ctx,MainActivity.class);
                    startActivity(intent);
                } else {
                    // Nothing selected
                }
            }
        }
    }

}
