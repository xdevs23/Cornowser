package io.xdevs23.cornowser.browser.browser.xwalk;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;

import org.xdevs23.debugutils.Logging;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import io.xdevs23.cornowser.browser.CornBrowser;

/**
 * A custom UIClient for our crunchy view
 */
public class CornUIClient extends XWalkUIClient {

    // This is for controlling errors on start-up loading
    private boolean readyForBugfreeBrowsing = false;

    public CornUIClient(XWalkView view) {
        super(view);
    }

    @Override
    public void onReceivedTitle(XWalkView view, String title) {
        Logging.logd("Web received title: " + title);
        super.onReceivedTitle(view, title);
    }

    @Override
    public void onIconAvailable(XWalkView view, String url, Message startDownload) {
        super.onIconAvailable(view, url, startDownload);
    }

    @Override
    public void onReceivedIcon(XWalkView view, String url, Bitmap icon) {
        // TODO: Put that icon into the omnibox
        super.onReceivedIcon(view, url, icon);
    }

    @Override
    public void onRequestFocus(XWalkView view) {
        super.onRequestFocus(view);
    }

    @Override
    public void onJavascriptCloseWindow(XWalkView view) {
        // TODO: handle this thing
        super.onJavascriptCloseWindow(view);
    }

    @Override
    public boolean onJavascriptModalDialog(XWalkView view, XWalkUIClient.JavascriptMessageType type, String url, String message, String defaultValue, XWalkJavascriptResult result) {
        return super.onJavascriptModalDialog(view, type, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
        // TODO: handle this thing
        return true;
    }

    @Override
    public boolean onJsConfirm(XWalkView view, String url, String message, XWalkJavascriptResult result) {
        // TODO: handle this thing
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(XWalkView view, String url, String message, String defaultValue, XWalkJavascriptResult result) {
        // TODO: handle this thing
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
        if( (status == LoadStatus.CANCELLED || status == LoadStatus.FAILED)
                && (!readyForBugfreeBrowsing) )
            view.load(url, null);
        else if(status == LoadStatus.FINISHED) readyForBugfreeBrowsing = true;

        Logging.logd("Page load stopped");
        super.onPageLoadStopped(view, url, status);

        CornBrowser.getWebProgressBar().setProgress(1.0f);
        CornBrowser.getWebProgressBar().setVisibility(View.INVISIBLE);
    }
}
