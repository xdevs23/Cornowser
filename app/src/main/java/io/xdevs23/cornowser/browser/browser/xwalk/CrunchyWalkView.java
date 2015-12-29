package io.xdevs23.cornowser.browser.browser.xwalk;


import android.app.Activity;
import android.content.Context;
import android.os.Build;

import org.xdevs23.debugUtils.Logging;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

import java.util.regex.Matcher;

/**
 * A delicious and crunchy XWalkView with awesome features
 */
public class CrunchyWalkView extends XWalkView {

    private CornResourceClient resourceClient;
    private CornUIClient       uiClient;

    public static String userAgent =
            "Mozilla/5.0 (" + Build.DEVICE + "; Android " + Build.VERSION.RELEASE + ") " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Cornowser/1.0.0.0 Crosswalk/17 Chrome/49.0.2593.0 Safari/537.36";

    private void init() {
        Logging.logd("Initializing our crunchy XWalkView :P");
        resourceClient  = new CornResourceClient(this);
        uiClient        = new CornUIClient      (this);
        setResourceClient(getResourceClient());
        setUIClient      (getUIClient());

        XWalkSettings crispySettings = this.getSettings();

        crispySettings.setInitialPageScale(1);
        crispySettings.setTextZoom(0);
        crispySettings.setUserAgentString(userAgent);
    }

    /* Don't use this! */
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
}
