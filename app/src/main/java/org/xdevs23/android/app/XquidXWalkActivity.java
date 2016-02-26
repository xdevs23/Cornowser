package org.xdevs23.android.app;

import android.os.Bundle;

import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;
import org.xdevs23.ui.utils.BarColors;
import org.xwalk.core.XWalkActivity;

import io.xdevs23.cornowser.browser.R;

public class XquidXWalkActivity extends XWalkActivity {

    @Override
    protected void onXWalkReady() {
        Logging.logd("XWalk is ready!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logging.logd("INIT START");
        super.onCreate(savedInstanceState);

        // Logging.logd only logs to logcat if debug mode is enabled
        // Disable debug mode in any type of releases in the AppConfig class
        Logging.logd("DEBUG ENABLED");
        Logging.logd(
                "Using debug mode could (maybe) have impact on performance. " +
                        "Please make sure that you use release mode when releasing a new " +
                        "version to get the best performance and avoid logcat spamming."
        );

        if(ConfigUtils.isDebuggable())
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    StackTraceParser.logStackTrace(ex);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    finish();
                }
            });

        BarColors.enableBarColoring(this.getWindow(), R.color.colorPrimaryDark);
    }

    protected void endApplication() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    protected void softEndApplication() {
        finish();
    }

}
