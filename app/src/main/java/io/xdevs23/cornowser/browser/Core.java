package io.xdevs23.cornowser.browser;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import org.xdevs23.debugutils.Logging;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Core extends Application {

    protected static Context coreContext;

    @Override
    public void onCreate() {
        Logging.logd("PRE INIT START");
        coreContext = this.getApplicationContext();
        super.onCreate();
    }

}
