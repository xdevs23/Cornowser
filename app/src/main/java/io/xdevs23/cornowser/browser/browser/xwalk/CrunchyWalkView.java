package io.xdevs23.cornowser.browser.browser.xwalk;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import org.xwalk.core.XWalkView;

public class CrunchyWalkView extends XWalkView {

    private CornResourceClient resourceClient;
    private CornUIClient       uiClient;

    private void init() {
        resourceClient  = new CornResourceClient(this);
        uiClient        = new CornUIClient      (this);
        setResourceClient(getResourceClient());
        setUIClient(getUIClient());
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
}
