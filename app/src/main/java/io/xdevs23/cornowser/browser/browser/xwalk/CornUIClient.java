package io.xdevs23.cornowser.browser.browser.xwalk;

import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

public class CornUIClient extends XWalkUIClient {

    public CornUIClient(XWalkView view) {
        super(view);
    }

    @Override
    public void onReceivedTitle(XWalkView view, String title) {
        super.onReceivedTitle(view, title);
    }

}
