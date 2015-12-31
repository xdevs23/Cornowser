package io.xdevs23.cornowser.browser;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;

public class Core extends Application {

    @Override
    public void onCreate() {
        Logging.logd("PRE INIT START");
        super.onCreate();
    }

}
