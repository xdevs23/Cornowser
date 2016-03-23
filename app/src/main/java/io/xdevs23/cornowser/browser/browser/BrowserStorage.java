package io.xdevs23.cornowser.browser.browser;

import android.content.Context;
import android.content.SharedPreferences;

import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;
import org.xdevs23.management.config.SharedPreferenceArray;

import io.xdevs23.cornowser.browser.browser.modules.adblock.AdBlockConst;
import io.xdevs23.cornowser.browser.browser.modules.ui.RenderColorMode;

public class BrowserStorage {

    //region Variable storage

    private String
                userHomePage
            ;

    private String[]
                lastSession,
                adBlockWhitelist
            ;

    private boolean
                omniboxIsBottom,
                enableFullscreen,
                debugEnabled,
                omniColoringEnabled,
                adBlockEnabled,
                saveLastSession,
                crashlyticsOptOut,
                adBlockNetBehavior,
                waitForAdBlock
            ;

    private RenderColorMode.ColorMode renderingColorMode;

    private BrowserStorageEnums.SearchEngine searchEngine;

    //endregion

    //region Class section
    /* Class section */

    private Context myContext;

    public SharedPreferences mSharedPreferences;

    public BrowserStorage(Context context) {
        myContext = context;
        init();
    }

    public void init() {
        mSharedPreferences = getContext().getSharedPreferences("userprefs", 0);
        setUserHomePage(getPref(BPrefKeys.userHomePage, BrowserDefaults.HOME_URL));
        setOmniboxPosition(getPref(BPrefKeys.omniboxPos, false));
        setEnableFullscreen(getPref(BPrefKeys.fullscreenPref, false));
        setColorMode(getPref(BPrefKeys.colorModePref, RenderColorMode.ColorMode.NORMAL));
        setOmniColoring(getPref(BPrefKeys.omniColorPref, true));
        setSearchEngine(BrowserStorageEnums.SearchEngine.valueOf(getPref(BPrefKeys.searchEngPref,
                BrowserStorageEnums.SearchEngine.Google.name())));
        setDebugMode(getPref(BPrefKeys.debugModePref, false));
        setEnableAdBlock(getPref(BPrefKeys.adBlockEnPref, false));
        setLastBrowsingSession(SharedPreferenceArray.getStringArray(getPref(BPrefKeys.lastSessionPref,
                "")));
        setSaveBrowsingSession(getPref(BPrefKeys.saveLastSessionPref, false));
        setCrashlyticsOptedOut(getPref(BPrefKeys.crashltcOptOutPref, false));
        setAdBlockNetBehavior(getPref(BPrefKeys.adBlockNetBehavPref, AdBlockConst.NET_BEHAVIOR_WIFI));
        setWaitForAdBlock(getPref(BPrefKeys.adBlockWaitForPref, false));
        setAdBlockWhitelist(SharedPreferenceArray.getStringArray(
                getPref(BPrefKeys.adBlockWhitelstPref, "")));
    }

    //endregion


    //region User homepage

    public void setUserHomePage(String url) {
        this.userHomePage = url;
    }

    public void saveUserHomePage(String url) {
        setUserHomePage(url);
        setPref(BPrefKeys.userHomePage, url);
    }

    public String getUserHomePage() {
        return userHomePage;
    }

    //endregion

    // region Search engine

    public void setSearchEngine(BrowserStorageEnums.SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    public void saveSearchEngine(BrowserStorageEnums.SearchEngine searchEngine) {
        setSearchEngine(searchEngine);
        setPref(BPrefKeys.searchEngPref, searchEngine.name());
    }

    public void saveSearchEngine(String engine) {
        saveSearchEngine(BrowserStorageEnums.SearchEngine.valueOf(engine));
    }

    public BrowserStorageEnums.SearchEngine getSearchEngine() {
        return this.searchEngine;
    }

    // endregion

    //region Omnibox position

    public void setOmniboxPosition(boolean isBottom) {
        this.omniboxIsBottom = isBottom;
    }

    public void saveOmniboxPosition(boolean isBottom) {
        setOmniboxPosition(isBottom);
        setPref(BPrefKeys.omniboxPos, isBottom);
    }

    public boolean getOmniboxPosition() {
        return this.omniboxIsBottom;
    }

    //endregion

    //region Fullscreen

    public void setEnableFullscreen(boolean enable) {
        enableFullscreen = enable;
    }

    public void saveEnableFullscreen(boolean enable) {
        setEnableFullscreen(enable);
        setPref(BPrefKeys.fullscreenPref, enable);
    }

    public boolean getIsFullscreenEnabled() {
        return enableFullscreen;
    }

    //endregion

    //region Color mode

    public void setColorMode(RenderColorMode.ColorMode colorMode) {
        renderingColorMode = colorMode;
    }

    public void setColorMode(int mode) {
        renderingColorMode = RenderColorMode.toColorMode(mode);
    }

    public void saveColorMode(RenderColorMode.ColorMode colorMode) {
        setColorMode(colorMode);
        setPref(BPrefKeys.colorModePref, colorMode.mode);
    }

    public RenderColorMode.ColorMode getColorMode() {
        return renderingColorMode;
    }

    //endregion

    //region Debug mode

    public void setDebugMode(boolean enabled) {
        debugEnabled = enabled;
        ConfigUtils.forceDebug = enabled;
    }

    public void saveDebugMode(boolean enabled) {
        setDebugMode(enabled);
        setPref(BPrefKeys.debugModePref, enabled);
    }

    public boolean getDebugMode() {
        return debugEnabled;
    }

    //endregion

    // region Omnibox coloring

    public void setOmniColoring(boolean enabled) {
        omniColoringEnabled = enabled;
    }

    public void saveOmniColoring(boolean enabled) {
        setOmniColoring(enabled);
        setPref(BPrefKeys.omniColorPref, enabled);
    }

    public boolean getOmniColoringEnabled() {
        return omniColoringEnabled;
    }

    // endregion

    // region AdBlock

    public void setEnableAdBlock(boolean enable) {
        adBlockEnabled = enable;
    }

    public void saveEnableAdBlock(boolean enable) {
        setEnableAdBlock(enable);
        setPref(BPrefKeys.adBlockEnPref, enable);
    }

    public boolean isAdBlockEnabled() {
        return adBlockEnabled;
    }

    // endregion

    // region Save session

    public void setLastBrowsingSession(String[] session) {
        lastSession = session;
    }

    public void saveLastBrowsingSession(String[] session) {
        setLastBrowsingSession(session);
        setPref(BPrefKeys.lastSessionPref, SharedPreferenceArray.getPreferenceString(session));
    }

    public String[] getLastBrowsingSession() {
        return lastSession;
    }

    public void setSaveBrowsingSession(boolean save) {
        saveLastSession = save;
    }

    public void saveEnableSaveSession(boolean save) {
        setSaveBrowsingSession(save);
        setPref(BPrefKeys.saveLastSessionPref, save);
        if(!save) rmPref(BPrefKeys.lastSessionPref);
    }

    public boolean isLastSessionEnabled() {
        return saveLastSession;
    }

    // endregion

    // region Crashlytics Opt-Out

    public void setCrashlyticsOptedOut(boolean optedOut) {
        crashlyticsOptOut = optedOut;
    }

    public void saveCrashlyticsOptedOut(boolean optedOut) {
        setCrashlyticsOptedOut(optedOut);
        setPref(BPrefKeys.crashltcOptOutPref, optedOut);
    }

    public boolean isCrashlyticsOptedOut() {
        return crashlyticsOptOut;
    }

    // endregion

    // region AdBlock Network Behavior

    public void setAdBlockNetBehavior(boolean behavior) {
        adBlockNetBehavior = behavior;
    }

    public void saveAdBlockNetBehavior(boolean behavior) {
        setAdBlockNetBehavior(behavior);
        setPref(BPrefKeys.adBlockNetBehavPref, behavior);
    }

    public boolean getAdBlockNetBehavior() {
        return adBlockNetBehavior;
    }

    // endregion

    // region Wait for AdBlock

    public void setWaitForAdBlock(boolean waitFor) {
        waitForAdBlock = waitFor;
    }

    public void saveWaitForAdBlock(boolean waitFor) {
        setWaitForAdBlock(waitFor);
        setPref(BPrefKeys.adBlockWaitForPref, waitFor);
    }

    public boolean isWaitForAdBlockEnabled() {
        return waitForAdBlock;
    }

    // endregion

    // region AdBlock whitelist

    public void setAdBlockWhitelist(String[] whitelist) {
        adBlockWhitelist = whitelist;
    }

    public String[] addDomainToAdBlockWhitelist(String domain) {
        Logging.logd("Add domain to adblock whitelist");
        String[] newList = new String[adBlockWhitelist == null ? 1 : adBlockWhitelist.length + 1];
        if(adBlockWhitelist != null && adBlockWhitelist.length >= 1){
            System.arraycopy(adBlockWhitelist, 0, newList, 0, adBlockWhitelist.length);
            newList[newList.length - 1] = domain;
        } else newList[0] = domain;
        return newList;
    }


    public String[] removeDomainFromAdBlockWhitelist(String domain) {
        Logging.logd("Remove domain from adblock whitelist");
        try {
            Logging.logd("  Checking...");
            String[] newList;
            if(adBlockWhitelist.length == 1) {
                Logging.logd("    => Erasing new list (only one domain was left)");
                newList = null;
            } else {
                Logging.logd("    => Searching for domain");
                newList = new String[adBlockWhitelist.length - 1];
                int foundOffset = 0;
                for (int i = 0; i < adBlockWhitelist.length; i++) {
                    if (adBlockWhitelist[i].contains(domain)) {
                        foundOffset = 1;
                        Logging.logd("      -- Domain found");
                    } else
                        newList[i - foundOffset] = adBlockWhitelist[i];
                }
            }
            return newList;
        } catch(Exception ex) {
            Logging.logd("Removal of domain " + domain + " failed. Printing stack trace.");
            StackTraceParser.logStackTrace(ex);
            return adBlockWhitelist;
        }
    }

    public void saveAdBlockWhitelist(String[] whitelist) {
        Logging.logd("Saving adblock whitelist");
        Logging.logd("  Checking...");
        if(whitelist == null) {
            Logging.logd("    => Erasing whitelist");
            setPref(BPrefKeys.adBlockWhitelstPref, "");
            setAdBlockWhitelist(null);
        } else {
            Logging.logd("    => Saving extended copy with length " + whitelist.length);
            setAdBlockWhitelist(whitelist);
            setPref(BPrefKeys.adBlockWhitelstPref,
                    SharedPreferenceArray.getPreferenceString(whitelist));
        }
    }

    public void saveAdBlockWhitelist(String newDomain) {
        saveAdBlockWhitelist(newDomain, true);
    }

    public void saveAdBlockWhitelist(String domain, boolean add) {
        if(add) saveAdBlockWhitelist(addDomainToAdBlockWhitelist(domain));
        else saveAdBlockWhitelist(removeDomainFromAdBlockWhitelist(domain));
    }

    public String[] getAdBlockWhitelist() {
        Logging.logd("Adblock whitelist size: " +
                (adBlockWhitelist == null ? "null" : adBlockWhitelist.length));
        return adBlockWhitelist;
    }

    // endregion


    //region General
    /* General methods */

    private Context getContext() {
        return myContext;
    }

    public String getPref(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public boolean getPref(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public int getPref(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public void setPref(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        logPref(key, value);
    }

    public void setPref(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
        logPref(key, String.valueOf(value));
    }

    public void setPref(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
        logPref(key, String.valueOf(value));
    }

    public void rmPref(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
        logPrefRm(key);
    }

    public void clearPrefs() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    protected void logPref(String msg) {
        Logging.logd("Pref " + msg);
    }

    protected void logPref(String key, String value) {
        logPref("'" + key + "' saved with value '" + value + "'.");
    }

    protected void logPrefRm(String key) {
        logPref("'" + key + "' removed.");
    }

    /**
     * Keys for shared preferences
     */
    public static final class BPrefKeys {
        public static final String
                userHomePage        = "pref_user_homepage",
                omniboxPos          = "pref_omni_pos",
                fullscreenPref      = "pref_enable_fullscreen",
                colorModePref       = "pref_render_color_mode",
                debugModePref       = "pref_enable_debug_mode",
                omniColorPref       = "pref_enable_omni_coloring",
                searchEngPref       = "pref_search_engine",
                adBlockEnPref       = "pref_adblock_enable",
                saveLastSessionPref = "pref_last_session",
                lastSessionPref     = "saved_last_session",
                crashltcOptOutPref  = "pref_crashlytics_optout",
                adBlockNetBehavPref = "pref_adblock_net_behavior",
                adBlockWaitForPref  = "pref_adblock_wait_for_init",
                adBlockWhitelstPref = "pref_adblock_whitelist"
                        ;
    }

    //endregion

}
