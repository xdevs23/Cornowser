package io.xdevs23.cornowser.browser;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugUtils.StackTraceParser;
import org.xwalk.core.XWalkView;

public class CornBrowser extends AppCompatActivity {

    public static XWalkView publicWebRender = null;

    public static RelativeLayout
            omnibox                 = null,
            publicWebRenderLayout   = null
            ;

    private static View staticView;
    private static Context staticContext;
    private static Activity staticActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_corn);

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

        staticActivity = this;
        staticContext  = this.getApplicationContext();
        staticView     = findViewById(R.id.corn_root_view);
    }
}
