package io.xdevs23.cornowser.browser.browser;

import android.content.Context;
import android.content.SharedPreferences;

public class BrowserStorage {

    private String
            userHomePage
            ;

    private Context myContext;

    public SharedPreferences mSharedPreferences;

    public BrowserStorage(Context context) {
        myContext = context;
        init();
    }

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

    public void init() {
        mSharedPreferences = getContext().getSharedPreferences("userprefs", 0);
        setUserHomePage(getPref(BPrefKeys.userHomePage, BrowserDefaults.HOME_URL));
    }

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


    public static final class BPrefKeys {
        public static final String
                userHomePage = "pref_user_homepage"
        ;
    }

}
