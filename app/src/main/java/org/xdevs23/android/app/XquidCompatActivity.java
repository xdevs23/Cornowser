package org.xdevs23.android.app;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugUtils.Logging;
import org.xdevs23.debugUtils.StackTraceParser;
import org.xdevs23.ui.utils.BarColors;

import io.xdevs23.cornowser.browser.R;

public class XquidCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logging.logd("DEBUG ENABLED");

        if(ConfigUtils.isDebuggable())
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    StackTraceParser.logStackTrace(ex);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                    finish();
                }
            });

        BarColors.enableBarColoring(this.getWindow(), R.color.colorPrimaryDark);
    }
}
