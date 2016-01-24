package io.xdevs23.cornowser.browser.browser.xwalk;


import android.app.Activity;
import android.content.Context;
import android.os.Build;

import org.xdevs23.annotation.DontUse;
import org.xdevs23.config.AppConfig;
import org.xdevs23.debugutils.Logging;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

import java.util.regex.Matcher;

import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxAnimations;

/**
 * A delicious and crunchy XWalkView with awesome features
 */
public class CrunchyWalkView extends XWalkView {

    private CornResourceClient resourceClient;
    private CornUIClient       uiClient;

    public static String userAgent =
            "Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.DEVICE + ") " +
                    "AppleWebKit/601.1.46 (KHTML, like Gecko) Cornowser/" +
                    AppConfig.versionName + " Chrome/49.0.2612.0 Mobile Safari/601.1";

    private void init() {
        Logging.logd("    Initializing our crunchy XWalkView :P");

        Logging.logd("      Resource Client");
        resourceClient  = new CornResourceClient(this);
        Logging.logd("      UI Client");
        uiClient        = new CornUIClient      (this);
        Logging.logd("      Applying clients");
        setResourceClient(getResourceClient());
        setUIClient(getUIClient());

        Logging.logd("      Configuring settings");
        XWalkSettings crispySettings = this.getSettings();
        crispySettings.setUserAgentString(userAgent);

        setOnTouchListener(OmniboxAnimations.mainOnTouchListener);

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

    public void reload(boolean ignoreCache) {
        reload(ignoreCache ? RELOAD_IGNORE_CACHE : RELOAD_NORMAL);
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

    public boolean goBack(int steps) {
        if(canGoBack())
            getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, steps);
        else return false;
        return true;
    }

    public boolean goBack() {
        return goBack(1);
    }

    public CrunchyWalkView immediatelyLoadUrl(String url) {
        load(url);
        return this;
    }

}
