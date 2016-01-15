package io.xdevs23.cornowser.browser.browser.xwalk;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;

import org.xdevs23.debugutils.Logging;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.browser.modules.CornHandler;
import io.xdevs23.cornowser.browser.browser.modules.WebThemeHelper;

/**
 * A custom UIClient for our crunchy view
 */
public class CornUIClient extends XWalkUIClient {

    // This is for controlling errors on start-up loading
    private boolean readyForBugfreeBrowsing = false;

    protected Bitmap currentFavicon = null;

    public CornUIClient(XWalkView view) {
        super(view);
    }

    @Override
    public void onReceivedTitle(XWalkView view, String title) {
        Logging.logd("Web received title: " + title);
        super.onReceivedTitle(view, title);
        WebThemeHelper.tintNow(CrunchyWalkView.fromXWalkView(view));
    }

    @Override
    public void onIconAvailable(XWalkView view, String url, Message startDownload) {
        super.onIconAvailable(view, url, startDownload);
        WebThemeHelper.tintNow(CrunchyWalkView.fromXWalkView(view));
    }

    @Override
    public void onReceivedIcon(XWalkView view, String url, Bitmap icon) {
        currentFavicon = icon;
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
    public boolean onConsoleMessage(XWalkView view, String message, int lineNumber, String sourceId, ConsoleMessageType messageType) {
        if(message.toLowerCase().contains("cornhandler://"))
            CornHandler.handleRequest(message,
                    CornBrowser.getActivity(),
                    CrunchyWalkView.fromXWalkView(view),
                    view.getUrl(),
                    CornBrowser.getWebEngine().getResourceClient());
        return super.onConsoleMessage(view, message, lineNumber, sourceId, messageType);
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
    public void onPageLoadStarted(XWalkView view, String url) {
        currentFavicon = null;
        super.onPageLoadStarted(view, url);
        CornBrowser.resetBarColor();
    }

    @Override
    public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
        if( (status == LoadStatus.CANCELLED || status == LoadStatus.FAILED)
                && (!readyForBugfreeBrowsing) && (!url.isEmpty()) )
            view.load(url, null);
        else readyForBugfreeBrowsing = true;

        Logging.logd("Page load stopped");
        super.onPageLoadStopped(view, url, status);

        CornBrowser.getWebProgressBar().setProgress(1.0f);
        CornBrowser.getWebProgressBar().setVisibility(View.INVISIBLE);
    }

    public Bitmap getCurrentFavicon() {
        return currentFavicon;
    }

}
