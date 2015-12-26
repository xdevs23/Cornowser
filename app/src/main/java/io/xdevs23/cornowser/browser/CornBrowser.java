package io.xdevs23.cornowser.browser;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugUtils.StackTraceParser;
import org.xwalk.core.XWalkView;

import io.xdevs23.cornowser.browser.browser.BrowserDefaults;

public class CornBrowser extends XquidCompatActivity {

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

        staticActivity  = this;
        staticContext   = this.getApplicationContext();
        staticView      = findViewById(R.id.corn_root_view);

        publicWebRender         = (XWalkView)       findViewById(R.id.webrender_xwalkview);
        publicWebRenderLayout   = (RelativeLayout)  findViewById(R.id.webrender_layout);

        omnibox                 = (RelativeLayout)  findViewById(R.id.omnibox_layout);

        publicWebRender.load(BrowserDefaults.HOME_URL, null);
    }

    public static View getView() {
        return staticView;
    }

    public static Context getContext() {
        return staticContext;
    }

    public static Context getActivity() {
        return staticActivity;
    }
}
