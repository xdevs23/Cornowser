package io.xdevs23.cornowser.browser.activity.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.ui.utils.BarColors;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.adblock.AdBlockManager;

public class AdBlockSettings extends XquidCompatActivity {

    protected static XquidCompatActivity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_adblock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.adblock_settings_toolbar);

        setSupportActionBar(toolbar);

        BarColors.enableBarColoring(getWindow(), R.color.red_900);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch(Exception ex) { /* */ }

        thisActivity = this;

        getFragmentManager().beginTransaction().replace(R.id.adblock_pref_content_frame,
                 new AdBlockPreferenceFragment().setContext(getApplicationContext(), this)).commit();
    }

    public static class AdBlockPreferenceFragment extends PreferenceFragment {

        private Context pContext;
        private XquidCompatActivity activity;

        public AdBlockPreferenceFragment setContext(Context context, XquidCompatActivity activity) {
            this.pContext = context;
            this.activity = activity;
            return this;
        }

        public Context getpContext() {
            return this.pContext;
        }

        private void initAdBlockEnable() {
            final SwitchPreference pref = (SwitchPreference) findPreference("adblock_enable");

            pref.setChecked(CornBrowser.getBrowserStorage().isAdBlockEnabled());

            pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    CornBrowser.getBrowserStorage().saveEnableAdBlock((boolean)newValue);
                    pref.setChecked((boolean)newValue);
                    if((boolean)newValue) doUpdateAdBlock();
                    return false;
                }
            });
        }

        private void doUpdateAdBlock() {
            final ProgressDialog dialog = new ProgressDialog(activity);
            dialog.setTitle(getString(R.string.adblock_download_hosts_title));
            dialog.setMessage(getString(R.string.adblock_hosts_downloading));
            dialog.setCancelable(true);
            dialog.setIndeterminate(true);
            AdBlockManager.setOnHostsUpdatedListener(new AdBlockManager.OnHostsUpdatedListener() {
                @Override
                public void onUpdateFinished() {
                    dialog.dismiss();
                }
            });
            dialog.show();
            AdBlockManager.downloadHostsAsync();
        }

        private void initDownloadHostsPref() {
            Preference pref = findPreference("adblock_download_hosts");

            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    doUpdateAdBlock();
                    return false;
                }
            });
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.adblock_preferences);

            initAdBlockEnable();
            initDownloadHostsPref();
        }
    }
}
