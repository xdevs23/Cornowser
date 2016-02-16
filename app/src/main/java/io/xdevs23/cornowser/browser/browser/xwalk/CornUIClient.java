package io.xdevs23.cornowser.browser.browser.xwalk;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.android.content.res.AssetHelper;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.ui.dialog.EditTextDialog;
import org.xdevs23.ui.dialog.templates.DismissDialogButton;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.CornHandler;
import io.xdevs23.cornowser.browser.browser.modules.WebThemeHelper;

/**
 * A custom UIClient for our crunchy view
 */
public class CornUIClient extends XWalkUIClient {

    public boolean skipDCheck = false;

    // This is for controlling errors on start-up loading
    private boolean readyForBugfreeBrowsing = false;


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
        super.onReceivedIcon(view, url, icon);
    }

    @Override
    public void onRequestFocus(XWalkView view) {
        super.onRequestFocus(view);
    }

    @Override
    public void onJavascriptCloseWindow(XWalkView view) {
        CornBrowser.getTabSwitcher().removeTab(CornBrowser.getTabSwitcher().getCurrentTab());
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

        return super.onJsConfirm(view, url, message, fResult);
    }

    @Override
    public boolean onJsPrompt(XWalkView view, String url, String message, String defaultValue, XWalkJavascriptResult result) {
        final XWalkJavascriptResult fResult = result;
        final EditTextDialog d = new EditTextDialog(CornBrowser.getContext(),
                (XquidCompatActivity) CornBrowser.getActivity(),
                message,
                defaultValue);
        d.showDialog();
        d.setOnClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fResult.confirmWithResult(d.getEnteredText());
                dialog.dismiss();
            }
        });

        return super.onJsPrompt(view, url, message, defaultValue, fResult);
    }

    protected boolean isDangerousPage(String url) {
        String matchUrl = url.substring(
                url.indexOf("//") + 2,
                url.indexOf("/", url.indexOf("//") + 2));
        String[] dUrls = AssetHelper.getAssetString("list/badPages.txt", CornBrowser.getContext())
                .split("\n");
        for ( String s : dUrls ) {
            Logging.logd(matchUrl + " checking...");
            if (matchUrl.contains(s))
                return true;
        }
        return false;
    }

    @Override
    public void onPageLoadStarted(XWalkView view, String url) {
        CornBrowser.resetOmniPositionState(true);
        Logging.logd("Page load started for: " + url);
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
            return;
        }
        CornBrowser.toggleGoForwardControlVisibility(CornBrowser.getWebEngine().canGoForward());
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

        CornBrowser.toggleGoForwardControlVisibility(CornBrowser.getWebEngine().canGoForward());

        CornBrowser.getOmniPtrLayout().setRefreshing(false);

        CornBrowser.getWebProgressBar().setProgress(1.0f);
        CornBrowser.getWebProgressBar().setVisibility(View.INVISIBLE);

        CornBrowser.publicWebRender.drawWithColorMode();
    }

}
