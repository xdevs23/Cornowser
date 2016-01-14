package io.xdevs23.cornowser.browser;

import android.app.Application;
import android.content.Context;

import org.xdevs23.debugutils.Logging;

public class Core extends Application {

    protected static Context coreContext;

    @Override
    public void onCreate() {
        Logging.logd("PRE INIT START");
        coreContext = this.getApplicationContext();
        super.onCreate();
    }

}
