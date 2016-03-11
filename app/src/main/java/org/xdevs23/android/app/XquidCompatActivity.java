package org.xdevs23.android.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;

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

    public static float dp2px(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    protected void endApplication() {
        onDestroy();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    protected void softEndApplication() {
        onDestroy();
        finish();
    }

}
