package io.xdevs23.cornowser.browser.browser.xwalk;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import org.xdevs23.android.content.res.AssetHelper;
import org.xdevs23.annotation.DontUse;
import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;
import org.xdevs23.general.URLEncode;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkView;

import java.lang.reflect.Field;
import java.util.regex.Matcher;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.browser.modules.CornHandler;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxAnimations;
import io.xdevs23.cornowser.browser.browser.modules.ui.RenderColorMode;

/**
 * A delicious and crunchy XWalkView with awesome features
 */
public class CrunchyWalkView extends XWalkView {

    public static String userAgent =
            "Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + ") " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Cornowser/%s Chrome/51.0.2675.1 Mobile Safari/537.36";

    public int currentProgress = 0;

    protected Bitmap favicon;

    private CornResourceClient resourceClient;
    private CornUIClient       uiClient;


    private void init() {
        Logging.logd("    Initializing our crunchy XWalkView :P");

        Logging.logd("      User agent");
        userAgent =
                String.format(userAgent, ConfigUtils.getVersionName(CornBrowser.getContext()));

        Logging.logd("      Resource Client");
        resourceClient  = new CornResourceClient(this);
        Logging.logd("      UI Client");
        uiClient        = new CornUIClient      (this);
        Logging.logd("      Applying clients");
        setResourceClient(getResourceClient());
        setUIClient(getUIClient());

        Logging.logd("      Configuring settings");
        setUserAgentString(userAgent);

        setOnTouchListener(OmniboxAnimations.mainOnTouchListener);

        drawWithColorMode();

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
        if(getResourceClient().checkIntentableUrl(
                (url.startsWith("intent") || url.startsWith("market"))
                        &&   url.contains("//")
                        && (!url.contains(":" )) ?
                    url.replace("//", "://") : url)
                ) return;
        Matcher urlRegExMatcher     = CornResourceClient.urlRegEx   .matcher(url);
        Matcher urlSecRegExMatcher  = CornResourceClient.urlSecRegEx.matcher(url);
        String nUrl = url;
        if(urlRegExMatcher.matches()) nUrl = url;
        else if(urlSecRegExMatcher.matches()) nUrl = "http://" + url;
        else {
            switch(CornBrowser.getBrowserStorage().getSearchEngine()) {
                case Google:
                    nUrl = "https://google.com/search?q=%s";
                    break;
                case DuckDuckGo:
                    nUrl = "https://duckduckgo.com/?q=%s";
                    break;
                case Yahoo:
                    nUrl = "https://search.yahoo.com/search?q=%s&toggle=1&cop=mss&ei=UTF-8";
                    break;
                case Bing:
                    nUrl = "https://bing.com/search?q=%s";
                    break;
                case Ecosia:
                    nUrl = "https://ecosia.org/search?q=%s";
                    break;
                default: break;
            }

            nUrl = String.format(nUrl, URLEncode.encode(url));
        }
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

    // Handle color modes

    public void drawWithColorMode() {
        Logging.logd("Applying web render color mode...");
        RenderColorMode.ColorMode cm = CornBrowser.getBrowserStorage().getColorMode();

        Logging.logd("Found color mode: " + cm.mode);

        String scriptName = "resetMode";

        switch(cm.mode) {
            case RenderColorMode.ColorMode.NORMAL:
                Logging.logd("Applying normal color mode");
                setBackgroundColor(Color.WHITE);
                break;
            case RenderColorMode.ColorMode.DARK:
                Logging.logd("Applying dark mode");
                scriptName = "darkMode";
                setBackgroundColor(Color.BLACK);
                break;
            case RenderColorMode.ColorMode.NEGATIVE:
                Logging.logd("Applying negative mode");
                scriptName = "negativeMode";
                setBackgroundColor(Color.BLACK);
                break;
            case RenderColorMode.ColorMode.INVERT:
                Logging.logd("Applying inverted mode");
                scriptName = "invertMode";
                setBackgroundColor(Color.WHITE);
                break;
            case RenderColorMode.ColorMode.GREYSCALE:
                Logging.logd("Applying greyscale");
                scriptName = "greyscaleMode";
                setBackgroundColor(Color.WHITE);
                break;
            default:
                Logging.logd("Warning: Unknown color mode " + cm.mode + ".");
                setBackgroundColor(Color.WHITE);
                break;
        }

        Logging.logd("Applying...");
        CornHandler.sendRawJSRequest(this,
                AssetHelper.getAssetString("appScripts/colormodes/" + scriptName + ".js",
                        getContext()));
    }

    public void onLongPress(String url) {
        // This is going to be handled later (maybe), but let this stay here first
        // TODO: Handle long press and show dialog
    }

    @Override
    public Bitmap getFavicon() {
        return favicon;
    }

    /**
     * Get the private bridge
     * @return Bridge
     */
    protected Object getBridge() {
        Object bridge;
        try {
            Field field = getClass().getSuperclass().getDeclaredField("bridge");
            field.setAccessible(true);
            bridge = field.get(this);
            field.setAccessible(false);
        } catch(Exception ex) {
            StackTraceParser.logStackTrace(ex);
            bridge = null;
        }
        return bridge;
    }

}