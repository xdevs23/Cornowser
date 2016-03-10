package io.xdevs23.cornowser.browser.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.ui.dialog.EditTextDialog;
import org.xdevs23.ui.utils.BarColors;
import org.xdevs23.ui.widget.EasyListView4;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.activity.settings.AdBlockSettings;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxAnimations;
import io.xdevs23.cornowser.browser.browser.modules.ui.RenderColorMode;

public class SettingsActivity extends XquidCompatActivity {

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

        /* Init stuff */

        // region Prefs: Browsing

        public void initHomePagePref() {
            Preference homePagePref = findPreference("settings_userhomepage");
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
        }

        public void initSearchEnginePref() {
            final ListPreference searchEnginePref =
                    (ListPreference) findPreference("settings_search_engine");

            searchEnginePref.setValue(CornBrowser.getBrowserStorage().getSearchEngine().name());

            searchEnginePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    CornBrowser.getBrowserStorage().saveSearchEngine((String)newValue);
                    searchEnginePref.setValue(CornBrowser.getBrowserStorage().getSearchEngine().name());
                    return false;
                }
            });
        }

        public void initAdBlockPref() {
            Preference pref = findPreference("settings_go_adblock");
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(pContext, AdBlockSettings.class));
                    return false;
                }
            });
        }

        public void initSaveSessionPref() {
            final SwitchPreference pref = (SwitchPreference) findPreference("settings_last_session");
            pref.setChecked(CornBrowser.getBrowserStorage().isLastSessionEnabled());
            pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    CornBrowser.getBrowserStorage().saveEnableSaveSession((boolean) newValue);
                    pref.setChecked((boolean) newValue);
                    return false;
                }
            });
        }

        // endregion

        // region Prefs: Layout

        public void initOmniboxPosPref() {
            // Omnibox position

            final ListPreference omniPosPref =
                    (ListPreference) findPreference("settings_omnibox_pos");

            omniPosPref.setValueIndex(OmniboxAnimations.getOmniboxPositionInt());

            omniPosPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    CornBrowser.getBrowserStorage().saveOmniboxPosition( Integer.parseInt((String)newValue) != 0 );
                    omniPosPref.setValueIndex(OmniboxAnimations.getOmniboxPositionInt());
                    return false;
                }
            });
        }

        public void initFullscreenPref() {
            // Fullscreen

            final SwitchPreference fullscreenPref =
                    (SwitchPreference) findPreference("settings_fullscreen");

            fullscreenPref.setChecked(CornBrowser.getBrowserStorage().getIsFullscreenEnabled());

            fullscreenPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    CornBrowser.getBrowserStorage().saveEnableFullscreen((boolean)newValue);
                    fullscreenPref.setChecked((boolean)newValue);
                    return false;
                }
            });
        }

        // endregion

        // region Prefs: Appearance

        public void initColorModePref() {
            // Color mode

            final ListPreference colorModePref =
                    (ListPreference) findPreference("settings_web_colormode");

            colorModePref.setValueIndex(CornBrowser.getBrowserStorage().getColorMode().mode);

            colorModePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    CornBrowser.getBrowserStorage()
                            .saveColorMode(RenderColorMode
                                    .toColorMode(Integer.parseInt((String)newValue)));
                    colorModePref.setValueIndex(Integer.parseInt((String)newValue));
                    return false;
                }
            });
        }

        /**
         * Omnibox coloring
         */
        public void initOmniColoringPref() {

            final SwitchPreference omniColorPref =
                    (SwitchPreference) findPreference("settings_omni_coloring");

            omniColorPref.setChecked(CornBrowser.getBrowserStorage().getOmniColoringEnabled());

            omniColorPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    CornBrowser.getBrowserStorage().saveOmniColoring((boolean)newValue);
                    omniColorPref.setChecked((boolean)newValue);
                    return false;
                }
            });
        }

        // endregion

        // region Prefs: Misc

        public void initAboutDialogs() {
            // License dialog
            EasyListView4.showDialogUsingPreference(
                    findPreference("settings_licenses"),
                    R.array.app_license_list,
                    thisActivity);

            // Translation credits dialog
            EasyListView4.showDialogUsingPreference(
                    findPreference("settings_credits_translation"),
                    R.array.credits_translation_list,
                    thisActivity
            );

            // Special thanks dialog
            EasyListView4.showDialogUsingPreference(
                    findPreference("settings_credits_special"),
                    R.array.credits_special_list,
                    thisActivity
            );
        }

        public void initDebugModePref() {
            final SwitchPreference debugModePref =
                    (SwitchPreference) findPreference("settings_debug_mode");

            debugModePref.setChecked(CornBrowser.getBrowserStorage().getDebugMode());

            debugModePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    CornBrowser.getBrowserStorage().saveDebugMode((boolean)newValue);
                    debugModePref.setChecked((boolean)newValue);
                    return false;
                }
            });
        }

        public void initClyticsOptOutPref() {
            final SwitchPreference pref =
                    (SwitchPreference) findPreference("settings_optout_clytics");

            pref.setChecked(CornBrowser.getBrowserStorage().isCrashlyticsOptedOut());

            pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    CornBrowser.getBrowserStorage().saveCrashlyticsOptedOut((boolean)newValue);
                    pref.setChecked((boolean)newValue);
                    return false;
                }
            });
        }

        // endregion

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings_preferences);

            // Browsing
            initHomePagePref();
            initSearchEnginePref();
            initAdBlockPref();
            initSaveSessionPref();

            // Layout
            initOmniboxPosPref();
            initFullscreenPref();

            // Appearance
            initColorModePref();
            initOmniColoringPref();

            // Misc
            initDebugModePref();
            initClyticsOptOutPref();
            initAboutDialogs();
        }
    }

}