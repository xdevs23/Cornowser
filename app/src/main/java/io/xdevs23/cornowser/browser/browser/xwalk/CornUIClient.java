package io.xdevs23.cornowser.browser.browser.xwalk;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.ValueCallback;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.android.content.res.AssetHelper;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;
import org.xdevs23.net.DownloadUtils;
import org.xdevs23.ui.dialog.EditTextDialog;
import org.xdevs23.ui.dialog.templates.DismissDialogButton;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.io.InputStream;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.CornHandler;
import io.xdevs23.cornowser.browser.browser.modules.WebThemeHelper;
import io.xdevs23.cornowser.browser.browser.modules.tabs.Tab;

/**
 * A custom UIClient for our crunchy view
 */
public class CornUIClient extends XWalkUIClient {

    public boolean skipDCheck = false;

    // This is for controlling errors on start-up loading
    protected boolean readyForBugfreeBrowsing = false;


    public CornUIClient(XWalkView view) {
        super(view);
    }

    @Override
    public void onReceivedTitle(XWalkView view, String title) {
        Logging.logd("Web received title: " + title);
        super.onReceivedTitle(view, title);
        WebThemeHelper.tintNow(CrunchyWalkView.fromXWalkView(view));
        CornBrowser.getTabSwitcher().getCurrentTab().setTitle(title);
    }

    @Override
    public void onIconAvailable(XWalkView view, String url, Message startDownload) {
        final CrunchyWalkView fView = CrunchyWalkView.fromXWalkView(view);
        final String fUrl = url;
        final Handler handler = new Handler();
        final Runnable tintNowRunnable = new Runnable() {
            @Override
            public void run() {
                WebThemeHelper.tintNow(CrunchyWalkView.fromXWalkView(fView));
            }
        };
        Thread downIcon = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = DownloadUtils.getInputStreamForConnection(fUrl);
                    fView.favicon = BitmapFactory.decodeStream(inputStream);
                    handler.post(tintNowRunnable);
                } catch(Exception ex) {
                    StackTraceParser.logStackTrace(ex);
                }
            }
        });
        downIcon.start();
    }

    @Override
    public void onReceivedIcon(XWalkView view, String url, Bitmap icon) {
        // Do nothing
    }

    @Override
    public void onRequestFocus(XWalkView view) {
        super.onRequestFocus(view);
    }

    @Override
    public void onJavascriptCloseWindow(XWalkView view) {
        if(CornBrowser.getTabSwitcher().getTabStorage().getTabCount() > 1)
            CornBrowser.getTabSwitcher().removeTab(CornBrowser.getTabSwitcher().getCurrentTab());
        else
            CornBrowser.getWebEngine().load(CornBrowser.getBrowserStorage().getUserHomePage());
    }

    @Override
    public boolean onCreateWindowRequested(XWalkView view, InitiateBy initiator, ValueCallback<XWalkView> callback) {
        CornBrowser.getTabSwitcher().addTab(new Tab());
        callback.onReceiveValue(CornBrowser.getTabSwitcher().getCurrentTab().getWebView());
        return true;
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
    public boolean onJavascriptModalDialog(XWalkView view, XWalkUIClient.JavascriptMessageType type,
                                           String url, String message, String defaultValue, XWalkJavascriptResult result) {
        return super.onJavascriptModalDialog(view, type, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
        AlertDialog.Builder b = new AlertDialog.Builder(CornBrowser.getActivity());
        b
                .setCancelable(true)
                .setTitle(view.getTitle())
                .setMessage(message)
                .setPositiveButton(CornBrowser.getContext().getString(R.string.answer_ok), new DismissDialogButton())
                .create()
                .show()
        ;
        result.confirm();
        return true;
    }

    @Override
    public boolean onJsConfirm(XWalkView view, String url, String message, XWalkJavascriptResult result) {
        final XWalkJavascriptResult fResult = result;
        AlertDialog.Builder b = new AlertDialog.Builder(CornBrowser.getActivity());
        b
                .setCancelable(true)
                .setTitle(view.getTitle())
                .setMessage(message)
                .setPositiveButton(CornBrowser.getContext().getString(R.string.answer_ok),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fResult.confirm();
                    }
                })
                .setNegativeButton(CornBrowser.getContext().getString(R.string.answer_cancel),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fResult.cancel();
                    }
                })
                .create()
                .show()
        ;
        return true;
    }

    @Override
    public boolean onJsPrompt(XWalkView view, String url, String message, String defaultValue, XWalkJavascriptResult result) {
        final XWalkJavascriptResult fResult = result;
        final EditTextDialog d = new EditTextDialog(CornBrowser.getContext(),
                (XquidCompatActivity) CornBrowser.getActivity(),
                message,
                defaultValue);
        d.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fResult.confirmWithResult(d.getEnteredText());
                dialog.dismiss();
            }
        });
        d.showDialog();


        return true;
    }

    protected boolean isDangerousPage(String url) {
        try {
            String matchUrl = url.substring(
                    url.indexOf("//") + 2,
                    url.indexOf("/", url.indexOf("//") + 2));
            String[] dUrls = AssetHelper.getAssetString("list/badPages.txt", CornBrowser.getContext())
                    .split("\n");
            for (String s : dUrls) {
                Logging.logd(matchUrl + " checking...");
                if (matchUrl.contains(s))
                    return true;
            }
            return false;
        } catch(Exception ex) {
            Logging.logd("Warning: Checking for dangerous page failed.");
            return false;
        }
    }

    @Override
    public void onPageLoadStarted(XWalkView view, String url) {
        CrunchyWalkView.fromXWalkView(view).favicon = null;
        CornBrowser.resetOmniPositionState(true);
        Logging.logd("Page load started for: " + url);
        CrunchyWalkView.fromXWalkView(view).currentProgress = 0;
        final XWalkView fView = view;
        final String fUrl = url;
        if(skipDCheck || (!isDangerousPage(url))) {
            Logging.logd("Web page not detected as dangerous.");
            skipDCheck = false;
            super.onPageLoadStarted(view, url);
        } else {
            Logging.logd("Dangerous website detected: " + url);
            fView.stopLoading();
            AlertDialog.Builder b = new AlertDialog.Builder(CornBrowser.getActivity());
            b
                    .setTitle(CornBrowser.getContext().getString(R.string.webrender_dangerous_site_title))
                    .setMessage(CornBrowser.getContext().getString(R.string.webrender_dangerous_site_message))
                    .setPositiveButton(CornBrowser.getContext().getString(R.string.answer_yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    skipDCheck = true;
                                    CornBrowser.publicWebRender.load(fUrl);
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton(CornBrowser.getContext().getString(R.string.answer_no),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    fView.stopLoading();
                                    dialog.dismiss();
                                }
                            })
                    .create().show();
            CornBrowser.initOmniboxPosition();
            CornBrowser.getWebEngine().loadWorkingUrl();
            return;
        }
        CornBrowser.handleGoForwardControlVisibility();
    }

    @Override
    public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
        if( (status == LoadStatus.CANCELLED || status == LoadStatus.FAILED)
                && (!readyForBugfreeBrowsing) && (!url.isEmpty()) )
            view.load(url, null);
        else {
            readyForBugfreeBrowsing = true;
            CornBrowser.getWebEngine().resumeTimers();
            CornBrowser.getWebEngine().onShow();
            CornBrowser.publicWebRenderLayout.bringToFront();
            CornBrowser.getWebEngine().bringToFront();
            CornBrowser.omnibox.bringToFront();
            CornBrowser.getWebEngine().refreshDrawableState();
        }

        Logging.logd("Page load stopped");
        super.onPageLoadStopped(view, url, status);

        CornBrowser.handleGoForwardControlVisibility();

        CornBrowser.getOmniPtrLayout().setRefreshing(false);

        CornBrowser.getWebProgressBar().setProgress(1.0f);
        CornBrowser.getWebProgressBar().setVisibility(View.INVISIBLE);

        CornBrowser.publicWebRender.drawWithColorMode();

        CrunchyWalkView.fromXWalkView(view).currentProgress = 100;
    }

}
