package io.xdevs23.cornowser.browser.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.ui.dialog.EditTextDialog;
import org.xdevs23.ui.utils.BarColors;
import org.xdevs23.ui.widget.EasyListView4;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxAnimations;

public class SettingsActivity extends XquidCompatActivity {

    protected static XquidCompatActivity thisActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);

        setSupportActionBar(toolbar);

        BarColors.enableBarColoring(getWindow(), R.color.light_blue_800);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch(Exception ex) { /* */ }

        thisActivity = this;

        getFragmentManager().beginTransaction().replace(R.id.settings_pref_content_frame,
                new SettingsPreferenceFragment().setContext(getApplicationContext(), this)).commit();
    }

    public static class SettingsPreferenceFragment extends PreferenceFragment {

        private Context pContext;
        private XquidCompatActivity activity;

        public SettingsPreferenceFragment setContext(Context context, XquidCompatActivity activity) {
            this.pContext = context;
            this.activity = activity;
            return this;
        }

        public Context getpContext() {
            return this.pContext;
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings_preferences);


            // License dialog
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

            // Home page
            Preference homePagePref =
                    (Preference) findPreference("settings_userhomepage");
            homePagePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    EditTextDialog dialog = new EditTextDialog(
                            getpContext(),
                            activity,
                            getString(R.string.settings_browsing_homepage_title),
                            CornBrowser.getBrowserStorage().getUserHomePage()
                    );
                    final EditTextDialog fDialog = dialog;
                    dialog.setOnClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CornBrowser.getBrowserStorage().saveUserHomePage(
                                    fDialog.getEnteredText()
                            );
                            dialog.dismiss();
                        }
                    }).showDialog();
                    return false;
                }
            });

            // Omnibox position

            ListPreference omniPosPref =
                    (ListPreference) findPreference("settings_omnibox_pos");

            omniPosPref.setValueIndex(OmniboxAnimations.getOmniboxPositionInt());

            omniPosPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    CornBrowser.getBrowserStorage().saveOmniboxPosition( Integer.parseInt((String)newValue) != 0 );
                    return false;
                }
            });

        }
    }
}