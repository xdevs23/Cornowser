package io.xdevs23.cornowser.browser.activity.settings;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.ui.utils.BarColors;

import io.xdevs23.cornowser.browser.R;

public class AdBlockSettings extends XquidCompatActivity {

    protected static XquidCompatActivity thisActivity;

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

        // getFragmentManager().beginTransaction().replace(R.id.settings_pref_content_frame,
        //         new SettingsPreferenceFragment().setContext(getApplicationContext(), this)).commit();
        // TODO: Add xml
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


        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // TODO: add xml
            //addPreferencesFromResource(R.xml.settings_preferences);

            // TODO: init stuff here
        }
    }
}
