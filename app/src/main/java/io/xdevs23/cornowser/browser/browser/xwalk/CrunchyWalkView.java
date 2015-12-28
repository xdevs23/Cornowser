package io.xdevs23.cornowser.browser.browser.xwalk;


import android.app.Activity;
import android.content.Context;

import org.xdevs23.debugUtils.Logging;
import org.xwalk.core.XWalkView;

import java.util.regex.Matcher;

public class CrunchyWalkView extends XWalkView {

    private CornResourceClient resourceClient;
    private CornUIClient       uiClient;

    private void init() {
        Logging.logd("Initializing our crunchy XWalkView :P");
        resourceClient  = new CornResourceClient(this);
        uiClient        = new CornUIClient      (this);
        setResourceClient(getResourceClient ());
        setUIClient      (getUIClient       ());
    }

    /** Don't use this! **/
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

    @Override
    public void load(String url, String content) {
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
