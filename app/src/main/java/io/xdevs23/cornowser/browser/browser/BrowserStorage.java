package io.xdevs23.cornowser.browser.browser;

import android.content.Context;
import android.content.SharedPreferences;

import io.xdevs23.cornowser.browser.browser.modules.ui.RenderColorMode;

public class BrowserStorage {

    /* Variable storage section */

    private String
                userHomePage
            ;

    private boolean
                omniboxIsBottom,
                enableFullscreen
            ;

    private RenderColorMode.ColorMode renderingColorMode;

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
    }

    /* Variable storage related methods */

    // User homepage

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

    // Omnibox position

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

    // Fullscreen

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

    // Color mode

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
    }

    public void setPref(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void setPref(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void clearPrefs() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Keys for shared preferences
     */
    public static final class BPrefKeys {
        public static final String
                userHomePage    = "pref_user_homepage",
                omniboxPos      = "pref_omni_pos",
                fullscreenPref  = "pref_enable_fullscreen",
                colorModePref   = "pref_render_color_mode"
                        ;
    }

}
