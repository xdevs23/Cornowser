package io.xdevs23.cornowser.browser.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.ui.utils.BarColors;
import org.xdevs23.ui.widget.EasyListView4;

import io.xdevs23.cornowser.browser.R;

public class SettingsActivity extends XquidCompatActivity {

    protected static XquidCompatActivity thisActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);

        setSupportActionBar(toolbar);

        BarColors.enableBarColoring(getWindow(), R.color.light_blue_800);

        thisActivity = this;

        getFragmentManager().beginTransaction().replace(R.id.settings_pref_content_frame,
                new SettingsPreferenceFragment().setContext(getApplicationContext())).commit();
    }

    public static class SettingsPreferenceFragment extends PreferenceFragment {

        private Context pContext;

        public SettingsPreferenceFragment setContext(Context context) {
            this.pContext = context;
            return this;
        }

        public Context getpContext() {
            return this.pContext;
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings_preferences);

            Preference licensesPref = (Preference) findPreference("settings_licenses");
            licensesPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.thisActivity);
                    builder.setView((new EasyListView4(SettingsActivity.thisActivity).setListArray(R.array.app_license_list)));
                    builder.create().show();
                    return false;
                }
            });
        }
    }
}