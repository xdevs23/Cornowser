package io.xdevs23.cornowser.browser;

import android.app.Application;

import org.xdevs23.debugutils.Logging;

public class Core extends Application {

    @Override
    public void onCreate() {
        Logging.logd("PRE INIT START");
        super.onCreate();
    }

}
