package org.xdevs23.android.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;
import org.xdevs23.ui.utils.BarColors;

import io.xdevs23.cornowser.browser.R;

public class XquidCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logging.logd("INIT START");
        super.onCreate(savedInstanceState);

        Logging.logd("DEBUG ENABLED");

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
}
