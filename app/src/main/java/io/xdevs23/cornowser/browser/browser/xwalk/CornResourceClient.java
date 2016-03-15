package io.xdevs23.cornowser.browser.browser.xwalk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;
import android.widget.Toast;

import org.xdevs23.android.content.res.AssetHelper;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;
import org.xdevs23.net.http.HttpStatusCodeHelper;
import org.xwalk.core.ClientCertRequest;
import org.xwalk.core.XWalkHttpAuthHandler;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;

import java.io.ByteArrayInputStream;
import java.util.regex.Pattern;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.CornHandler;
import io.xdevs23.cornowser.browser.browser.modules.WebThemeHelper;
import io.xdevs23.cornowser.browser.browser.modules.adblock.AdBlockManager;

/**
 * A cool "resource client" for our crunchy view
 */
public class CornResourceClient extends XWalkResourceClient {

    public String currentWorkingUrl = "about:blank";

    public static Pattern urlRegEx = Pattern.compile(
            "(" +
                    "(^(https|http|ftp|rtmp)://[^ ]*[.][^ ]*)|" +   // Protocols for addresses
                    "^(file:///).*|" +                              // Local files
                    "^(CornHandler://).*|" +                        // Handler
                    "^(javascript:).*" +                            // Javascript

                    ")"
    );

    public static Pattern urlSecRegEx = Pattern.compile(
            "(" +
                    "([^ ]*[.][^ ]*)|" +                            // Addresses without protocol
                    "^(localhost).*|" +                             // localhost
                    "([^ ]*[.][^ ]*[.][^ ]*[.][^ ]*)|" +            // IPv4 addresses
                    "([^ ]*(::|:)[^ ]*)|" +                         // IPv6 addresses
                    "([^ ]*@[^ ]*[.][^ ]*)" +                       // user@host.tld

                    ")"
    );

    private boolean allowTinting = true;

    private boolean triedIntentLoad = false;


    public CornResourceClient(XWalkView view) {
        super(view);
    }

    @Override
    public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
        if(url.toLowerCase().startsWith("cornhandler://")) {
            CornHandler.handleRequest(
                    url,
                    CornBrowser.getActivity(),
                    CrunchyWalkView.fromXWalkView(view),
                    view.getUrl(),
                    this);
            return true;
        }
        checkIntentableUrl(url.toLowerCase());
        CornBrowser.resetOmniPositionState(true);
        Logging.logd("Starting url loading '" + url + "'");
        return super.shouldOverrideUrlLoading(view, url);
    }

    protected boolean checkIntentableUrl(String url) {
        Context c = CornBrowser.getContext();
        boolean success = false;
        if(url.startsWith("intent")
                || url.startsWith("market")
                || url.contains("play.google.")
                || url.contains("google.com/play")
                || url.contains("spotify.")) {
            try {
                c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                success = true;
            } catch (Exception ex) {
                // Ignore
            }

            if(!success) Toast.makeText(c, "(°-°)", Toast.LENGTH_SHORT).show();
        } else triedIntentLoad = false;
        return success;
    }

    @Override
    public void onLoadStarted(XWalkView view, String url) {
        if(AdBlockManager.isAdBlockedHost(url)) {
            Logging.logd("AdBlock: Blocked ad!");
            return;
        }
        super.onLoadStarted(view, url);
        allowTinting = true;
        CornBrowser.applyInsideOmniText();
        WebThemeHelper.tintNow();
        CornBrowser.publicWebRender.drawWithColorMode();
    }

    @Override
    public void onLoadFinished(XWalkView view, String url) {
        Logging.logd("Web load finished " + url + " " + view.getUrl());
        if(allowTinting)
            WebThemeHelper.tintNow();
        allowTinting = false;
        super.onLoadFinished(view, url);
        CornBrowser.applyInsideOmniText();
        currentWorkingUrl = view.getUrl();
        CornBrowser.publicWebRender.drawWithColorMode();
    }

    @Override
    public XWalkWebResourceResponse shouldInterceptLoadRequest(XWalkView view, XWalkWebResourceRequest request) {
        return super.shouldInterceptLoadRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptLoadRequest(XWalkView view, String url) {
        try {
            if (CornBrowser.getBrowserStorage().isAdBlockEnabled()
                    && CrunchyWalkView.fromXWalkView(view).getUIClient().readyForBugfreeBrowsing
                    && AdBlockManager.isAdBlockedHost(url)) {
                Logging.logd("AdBlock: Ad blocked");
                return new WebResourceResponse("text/plain", "UTF-8",
                        new ByteArrayInputStream("".getBytes()));
            }
        } catch(Exception ex) {
            StackTraceParser.logStackTrace(ex);
        }
        return super.shouldInterceptLoadRequest(view, url);
    }

    @Override
    public void onReceivedResponseHeaders(XWalkView view, XWalkWebResourceRequest request, XWalkWebResourceResponse response) {
        super.onReceivedResponseHeaders(view, request, response);
    }

    @Override
    public void onDocumentLoadedInFrame(XWalkView view, long frameId) {
        super.onDocumentLoadedInFrame(view, frameId);
    }

    @Override
    public void onProgressChanged(XWalkView view, int percentage) {
        super.onProgressChanged(view, percentage);
        Logging.logd("Actual loading progress: " + percentage);
        CrunchyWalkView.fromXWalkView(view).currentProgress = percentage;
        CornBrowser.updateWebProgress();
        if(percentage > 40)
            CornBrowser.getOmniPtrLayout().setRefreshing(false);
    }

    @Override
    public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
        Logging.logd("Web load error on url: " + failingUrl + ", description: " + description + ", err " + errorCode);
        HttpStatusCodeHelper.HttpStatusCode statusCode = HttpStatusCodeHelper.HttpStatusCode.NONE;
        switch(errorCode) {
            case -1:
                if(description.contains("network change")) { view.load(failingUrl, null); return; }
                else statusCode = HttpStatusCodeHelper.HttpStatusCode.UNKNOWN;
                break;
            case ERROR_BAD_URL:
                if(!triedIntentLoad) {
                    triedIntentLoad = true;
                    if(checkIntentableUrl(failingUrl)) return;
                }
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERR_BAD_URL;
                break;
            case ERROR_AUTHENTICATION:
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERR_AUTHENTICATION;
                break;
            case ERROR_CONNECT:
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERR_CONNECT;
                break;
            case ERROR_FAILED_SSL_HANDSHAKE:
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERR_FAILED_SSL_HANDSHAKE;
                break;
            case ERROR_FILE:
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERR_FILE;
                break;
            case ERROR_FILE_NOT_FOUND:
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERROR_NOT_FOUND;
                break;
            case ERROR_HOST_LOOKUP:
                if(!triedIntentLoad) {
                    triedIntentLoad = true;
                    if(checkIntentableUrl(failingUrl)) return;
                }
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERR_NAME_NOT_RESOLVED;
                break;
            case ERROR_IO:
                statusCode = HttpStatusCodeHelper.HttpStatusCode.IO_ERROR;
                break;
            case ERROR_PROXY_AUTHENTICATION:
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERROR_PROXY_AUTHENTICATION_REQUIRED;
                break;
            case ERROR_REDIRECT_LOOP:
                statusCode = HttpStatusCodeHelper.HttpStatusCode.TOO_MANY_REDIRECTS;
                break;
            case ERROR_TIMEOUT:
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERROR_REQUEST_TIME_OUT;
                break;
            case ERROR_TOO_MANY_REQUESTS:
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERROR_TOO_MANY_REQUESTS;
                break;
            case ERROR_UNSUPPORTED_AUTH_SCHEME:
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERR_UNSUPPORTED_AUTH_SCHEME;
                break;
            case ERROR_UNSUPPORTED_SCHEME:
                if(!triedIntentLoad) {
                    triedIntentLoad = true;
                    if(checkIntentableUrl(failingUrl)) return;
                }
                statusCode = HttpStatusCodeHelper.HttpStatusCode.ERR_UNSUPPORTED_SCHEME;
                break;
            case 0:  break;
            default: statusCode = HttpStatusCodeHelper.HttpStatusCode.UNKNOWN;
        }
        handleHtError(statusCode, (CrunchyWalkView)view, description, failingUrl);
    }

    public void handleHtError(int errorCode, CrunchyWalkView view, String description, String failingUrl) {
        String errHtmlContent =
                String.format(

                        AssetHelper.getAssetString("htdocs/error.html", CornBrowser.getContext()), // Error file

                        CornBrowser.getRStr(R.string.html_error_title),                     // Title 'Error'
                        errorCode + "<br />URL: " + failingUrl,

                        String.format(CornBrowser.getRStr(R.string.html_error_body_info),
                                HttpStatusCodeHelper.getStatusCodeString(errorCode)), // Description

                        CornBrowser.getRStr(R.string.html_reload_btn_value) // Reload button

                );
        view.load(null,
                AssetHelper.getAssetString("htdocs/5.html", CornBrowser.getContext())
                        .replace("<!-- BODY -->", errHtmlContent));
    }

    public void handleHtError(HttpStatusCodeHelper.HttpStatusCode ec, CrunchyWalkView v, String d, String f) {
        handleHtError(ec.getNumVal(), v, d, f);
    }

    @Override
    public void onReceivedHttpAuthRequest(XWalkView view, XWalkHttpAuthHandler handler, String host, String realm) {
        Logging.logd("Web received http authentication request");
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public void onReceivedSslError(XWalkView view, ValueCallback<Boolean> callback, SslError error) {
        super.onReceivedSslError(view, callback, error);
    }

    @Override
    public void onReceivedClientCertRequest(XWalkView view, ClientCertRequest handler) {
        super.onReceivedClientCertRequest(view, handler);
    }

    @Override
    public void doUpdateVisitedHistory(XWalkView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
    }

}