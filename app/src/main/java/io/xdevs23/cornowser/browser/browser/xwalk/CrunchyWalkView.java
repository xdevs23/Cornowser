package io.xdevs23.cornowser.browser.browser.xwalk;


import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;

import org.xdevs23.annotation.DontUse;
import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugutils.Logging;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

import java.util.regex.Matcher;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxAnimations;
import io.xdevs23.cornowser.browser.browser.modules.ui.RenderColorMode;

/**
 * A delicious and crunchy XWalkView with awesome features
 */
public class CrunchyWalkView extends XWalkView {

    public static String userAgent =
            "Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.DEVICE + ") " +
                    "AppleWebKit/601.2.7 (KHTML, like Gecko) Cornowser/%s Chrome/50.0.2646.0 Mobile Safari/601.2.7";

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
        XWalkSettings crispySettings = this.getSettings();
        crispySettings.setUserAgentString(userAgent);

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

    // Handle color modes

    public void drawWithColorMode() {
        Logging.logd("Applying web render color mode...");
        RenderColorMode.ColorMode cm = CornBrowser.getBrowserStorage().getColorMode();
        Paint paint = new Paint();
        final float[] negativeColor = {
                -1.0f, 0, 0, 0, 255,    // Red
                0, -1.0f, 0, 0, 255,    // Green
                0, 0, -1.0f, 0, 255,    // Blue
                0, 0, 0,  1.0f, 0       // Alpha
        };
        final float[] darkColor = {
                1f, 0, 0, 0, -255,
                0, 1f, 0, 0, -255,
                0, 0, 1f, 0, -255,
                0, 0, 0, 1f,    0
        };
        final float[] invertColor = {
                -1f, 0, 0, 0, 0,
                0, -1f, 0, 0, 0,
                0, 0, -1f, 0, 0,
                0, 0, 0, 1f,  0
        };

        Logging.logd("Found color mode: " + cm.mode);

        switch(cm.mode) {
            case RenderColorMode.ColorMode.NORMAL:
                Logging.logd("Applying normal color mode");
                paint.setColorFilter(null);
                break;
            case RenderColorMode.ColorMode.DARK:
                Logging.logd("Applying dark mode");
                paint.setColorFilter(new ColorMatrixColorFilter(darkColor));
                break;
            case RenderColorMode.ColorMode.NEGATIVE:
                Logging.logd("Applying negative mode");
                paint.setColorFilter(new ColorMatrixColorFilter(negativeColor));
                break;
            case RenderColorMode.ColorMode.INVERT:
                Logging.logd("Applying inverted mode");
                paint.setColorFilter(new ColorMatrixColorFilter(invertColor));
                break;
            case RenderColorMode.ColorMode.GREYSCALE:
                Logging.logd("Applying greyscale");
                ColorMatrix m = new ColorMatrix();
                m.setSaturation(0);
                paint.setColorFilter(new ColorMatrixColorFilter(m));
                break;
            default:
                Logging.logd("Warning: Unknown color mode " + cm.mode + ".");
                break;
        }

        Logging.logd("Setting layer type...");
        setLayerType(LAYER_TYPE_HARDWARE, paint);
    }

}