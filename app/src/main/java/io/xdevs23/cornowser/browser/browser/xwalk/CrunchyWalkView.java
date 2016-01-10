package io.xdevs23.cornowser.browser.browser.xwalk;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import org.xdevs23.annotation.DontUse;
import org.xdevs23.config.AppConfig;
import org.xdevs23.debugutils.Logging;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

import java.util.regex.Matcher;

/**
 * A delicious and crunchy XWalkView with awesome features
 */
public class CrunchyWalkView extends XWalkView {

    private CornResourceClient resourceClient;
    private CornUIClient       uiClient;

    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN: break;
                case MotionEvent.ACTION_UP:   break;
                case MotionEvent.ACTION_MOVE: break;
                default: break;
            }
            return false;
        }
    };

    public static String userAgent =
            "Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.DEVICE + ") " +
                    "AppleWebKit/601.1.46 (KHTML, like Gecko) Cornowser/" +
                    AppConfig.versionName + " Chrome/49.0.2593.0 Mobile Safari/601.1";

    private void init() {
        Logging.logd("    Initializing our crunchy XWalkView :P");

        Logging.logd("      Resource Client");
        resourceClient  = new CornResourceClient(this);
        Logging.logd("      UI Client");
        uiClient        = new CornUIClient      (this);
        Logging.logd("      Applying clients");
        setResourceClient(getResourceClient());
        setUIClient(getUIClient());

        Logging.logd("      Touch listener");
        setOnTouchListener(onTouchListener);

        Logging.logd("      Configuring settings");
        XWalkSettings crispySettings = this.getSettings();
        crispySettings.setUserAgentString(userAgent);

        Logging.logd("      Done!");
    }

    /* Don't use this! */
    @DontUse
    public CrunchyWalkView(Context context) {
        super(context);
        init();
    }

    public CrunchyWalkView(Context context, Activity activity) {
        super(context, activity);
        init();
    }

    public CornResourceClient getResourceClient() {
        return resourceClient;
    }

    public CornUIClient getUIClient() {
        return uiClient;
    }

    public static CrunchyWalkView fromXWalkView(XWalkView view) {
        return (CrunchyWalkView)view;
    }

    @Override
    public void load(String url, String content) {
        if(url == null) { super.load(null, content); return; }
        Matcher urlRegExMatcher     = CornResourceClient.urlRegEx   .matcher(url);
        Matcher urlSecRegExMatcher  = CornResourceClient.urlSecRegEx.matcher(url);
        String nUrl = url;
        if(urlRegExMatcher.matches()) nUrl = url;
        else if(urlSecRegExMatcher.matches()) nUrl = "http://" + url;
        else nUrl = "https://google.com/search?q=" + url
                    .replace("+", "%2B").replace(" ", "+"); // What is C++ -> What+is+C%2B%2B
        super.load(nUrl, content);
    }

    public void load(String url) {
        load(url, null);
    }

    public void loadWorkingUrl() {
        load(getResourceClient().currentWorkingUrl);
    }

    public void evaluateJavascript(String script) {
        evaluateJavascript(script, null);
    }

    public void loadContent(String content) {
        load(null, content);
    }

    public boolean canGoForward() {
        return getNavigationHistory().canGoForward();
    }

    public boolean canGoBack() {
        return getNavigationHistory().canGoBack();
    }

    public void goForward(int steps) {
        if(canGoForward())
            getNavigationHistory().navigate(XWalkNavigationHistory.Direction.FORWARD, steps);
    }

    public void goForward() {
        goForward(1);
    }

    public void goBack(int steps) {
        if(canGoBack())
            getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, steps);
    }

    public void goBack() {
        goBack(1);
    }

}
