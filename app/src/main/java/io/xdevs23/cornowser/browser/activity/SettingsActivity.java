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

import java.lang.reflect.Field;
import java.util.List;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.activity.settings.AdBlockSettings;
import io.xdevs23.cornowser.browser.browser.BrowserStorageEnums;
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

        BarColors.enableBarColoring(getWindow(), R.color.settings_statusbar_background);

        try {
            assert getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch(Exception ex) { /* */ }

        thisActivity = this;

        getFragmentManager().beginTransaction().replace(R.id.settings_pref_content_frame,
                new SettingsPreferenceFragment().setContext(getApplicationContext(), this)).commit();
    }


    public static class SettingsPreferenceFragment extends PreferenceFragment implements
                                                        Preference.OnPreferenceChangeListener,
                                                        Preference.OnPreferenceClickListener {

        private Context pContext;
        private XquidCompatActivity activity;

        private static final String
                KEY_USERHOMEPAGE            =   "settings_userhomepage",
                KEY_SEARCH_ENGINE           =   "settings_search_engine",
                KEY_GO_ADBLOCK              =   "settings_go_adblock",
                KEY_LAST_SESSION            =   "settings_last_session",
                KEY_OMNIBOX_POSITION        =   "settings_omnibox_pos",
                KEY_FULLSCREEN              =   "settings_fullscreen",
                KEY_WEB_COLORMODE           =   "settings_web_colormode",
                KEY_OMNIBOX_COLORING        =   "settings_omni_coloring",
                KEY_DIALOG_LICENSES         =   "settings_licenses",
                KEY_DIALOG_TRANSLATIONS     =   "settings_credits_translation",
                KEY_DIALOG_CREDITS_SPECIAL  =   "settings_credits_special",
                KEY_DEBUG_MODE              =   "settings_debug_mode",
                KEY_OPTOUT_CLYTICS          =   "settings_optout_clytics"
                        ;

        Preference
                homePagePref,
                adblockPref
                        ;

        SwitchPreference
                lastSessionPref,
                fullscreenPref,
                omniColoringPref,
                debugModePref,
                optoutClyticsPref
                        ;

        ListPreference
                searchEnginePref,
                omniboxPosPref,
                colorModePref
                        ;

        public SettingsPreferenceFragment setContext(Context context, XquidCompatActivity activity) {
            this.pContext = context;
            this.activity = activity;
            return this;
        }

        public Context getpContext() {
            return this.pContext;
        }

        private void initializePreferences(Preference... prefs) {
            for ( Preference pref : prefs ) {
                if ( pref instanceof SwitchPreference ||
                        pref instanceof ListPreference) {
                    pref.setOnPreferenceChangeListener(this);
                } else {
                    pref.setOnPreferenceClickListener(this);
                }
            }
        }

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

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings_preferences);

            homePagePref    = findPreference(KEY_USERHOMEPAGE);
            adblockPref     = findPreference(KEY_GO_ADBLOCK);

            lastSessionPref     = (SwitchPreference) findPreference(KEY_LAST_SESSION);
            fullscreenPref      = (SwitchPreference) findPreference(KEY_FULLSCREEN);
            omniColoringPref    = (SwitchPreference) findPreference(KEY_OMNIBOX_COLORING);
            debugModePref       = (SwitchPreference) findPreference(KEY_DEBUG_MODE);
            optoutClyticsPref   = (SwitchPreference) findPreference(KEY_OPTOUT_CLYTICS);

            searchEnginePref    = (ListPreference)   findPreference(KEY_SEARCH_ENGINE);
            omniboxPosPref      = (ListPreference)   findPreference(KEY_OMNIBOX_POSITION);
            colorModePref       = (ListPreference)   findPreference(KEY_WEB_COLORMODE);

            initializePreferences(
                    homePagePref, adblockPref,

                    lastSessionPref, fullscreenPref, omniColoringPref,
                    debugModePref, optoutClyticsPref,

                    searchEnginePref, omniboxPosPref, colorModePref
            );

            initAboutDialogs();

            updatePrefs();
        }

        private void updatePrefs() {
            optoutClyticsPref   .setChecked(CornBrowser.getBrowserStorage().isCrashlyticsOptedOut());
            debugModePref       .setChecked(CornBrowser.getBrowserStorage().getDebugMode());
            omniColoringPref    .setChecked(CornBrowser.getBrowserStorage().getOmniColoringEnabled());
            fullscreenPref      .setChecked(CornBrowser.getBrowserStorage().getIsFullscreenEnabled());
            lastSessionPref     .setChecked(CornBrowser.getBrowserStorage().isLastSessionEnabled());
            colorModePref       .setValueIndex(CornBrowser.getBrowserStorage().getColorMode().mode);
            omniboxPosPref      .setValueIndex(OmniboxAnimations.getOmniboxPositionInt());
            searchEnginePref    .setValue(CornBrowser.getBrowserStorage().getSearchEngine().name());
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();
            switch(key) {

                case KEY_USERHOMEPAGE:
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
                    break;

                case KEY_SEARCH_ENGINE:
                    if( newValue.equals(getString(R.string.general_custom)) ) {
                        final EditTextDialog edialog = new EditTextDialog(
                                getpContext(),
                                activity,
                                getString(R.string.settings_browsing_searchengine_title),
                                CornBrowser.getBrowserStorage().getCustomSearchEngine(),
                                getString(R.string.settings_browsing_searchengine_custom_desc)
                        );
                        edialog.setOnClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CornBrowser.getBrowserStorage().saveSearchEngine(
                                        BrowserStorageEnums.SearchEngine.Custom
                                );
                                CornBrowser.getBrowserStorage().saveCustomSearchEngine(
                                        edialog.getEnteredText()
                                );
                                searchEnginePref.setValue(
                                        CornBrowser.getBrowserStorage().getSearchEngine().name());
                                dialog.dismiss();
                            }
                        }).showDialog();
                        return true;
                    }
                    CornBrowser.getBrowserStorage().saveSearchEngine((String)newValue);
                    searchEnginePref.setValue(CornBrowser.getBrowserStorage().getSearchEngine().name());
                    break;

                case KEY_GO_ADBLOCK:
                    startActivity(new Intent(pContext, AdBlockSettings.class));
                    break;

                case KEY_OMNIBOX_COLORING:
                    CornBrowser.getBrowserStorage().saveOmniColoring((boolean)newValue);
                    break;

                case KEY_OPTOUT_CLYTICS:
                    CornBrowser.getBrowserStorage().saveCrashlyticsOptedOut((boolean)newValue);
                    optoutClyticsPref.setChecked((boolean)newValue);
                    break;

                case KEY_DEBUG_MODE:
                    CornBrowser.getBrowserStorage().saveDebugMode((boolean)newValue);
                    debugModePref.setChecked((boolean)newValue);
                    break;

                case KEY_WEB_COLORMODE:
                    CornBrowser.getBrowserStorage()
                            .saveColorMode(RenderColorMode
                                    .toColorMode(Integer.parseInt((String)newValue)));
                    colorModePref.setValueIndex(Integer.parseInt((String)newValue));
                    break;

                case KEY_OMNIBOX_POSITION:
                    CornBrowser.getBrowserStorage().saveOmniboxPosition( Integer.parseInt((String)newValue) != 0 );
                    omniboxPosPref.setValueIndex(OmniboxAnimations.getOmniboxPositionInt());
                    break;

                case KEY_FULLSCREEN:
                    CornBrowser.getBrowserStorage().saveEnableFullscreen((boolean)newValue);
                    fullscreenPref.setChecked((boolean)newValue);
                    break;

                case KEY_LAST_SESSION:
                    CornBrowser.getBrowserStorage().saveEnableSaveSession((boolean) newValue);
                    lastSessionPref.setChecked((boolean) newValue);
                    break;

                default: break;
            }
            if(preference instanceof SwitchPreference)
                ((SwitchPreference)preference).setChecked((boolean)newValue);
            else if(preference instanceof ListPreference)
                ((ListPreference)preference).setValue((String)newValue);
            return true;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            onPreferenceChange(preference, null);
            return true;
        }
    }

}