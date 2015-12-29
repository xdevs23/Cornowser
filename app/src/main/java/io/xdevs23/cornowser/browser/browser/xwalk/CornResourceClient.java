package io.xdevs23.cornowser.browser.browser.xwalk;


import android.net.http.SslError;
import android.webkit.ValueCallback;

import org.xdevs23.android.content.res.AssetHelper;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.net.http.HttpStatusCodeHelper;
import org.xwalk.core.ClientCertRequest;
import org.xwalk.core.XWalkHttpAuthHandler;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

import java.util.regex.Pattern;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.CornHandler;

/**
 * A cool "resource client" for our crunchy view
 */
public class CornResourceClient extends XWalkResourceClient {

    public String currentWorkingUrl = "about:blank";

    public static Pattern urlRegEx = Pattern.compile(
            "(" +
                    "(^(https|http|ftp|rtmp)://.*[.].*)|" +         // Protocols for adresses
                    "^(file:///).*|" +                              // Local files
                    "^(CornHandler://).*|" +                        // Handler
                    "^(javascript:).*" +                            // Javascript

                    ")"
    );

    public static Pattern urlSecRegEx = Pattern.compile(
            "(" +
                    "(.*[.].*)|" +                                  // Adresses without protocol
                    "^(localhost).*|" +                             // localhost
                    "(.*[.].*[.].*[.].*)|" +                        // IPv4 adresses
                    "(.*(::|:).*)|" +                               // IPv6 adresses
                    "(.*@.*[.].*)" +                                // user@host.domain

                    ")"
    );

    public CornResourceClient(XWalkView view) {
        super(view);
    }

    @Override
    public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
        Logging.logd("");
        if(url.startsWith("CornHandler://")) {
            CornHandler.handleRequest(
                    url,
                    CornBrowser.getActivity(),
                    CrunchyWalkView.fromXWalkView(view),
                    view.getUrl(),
                    this);
            return true;
        }
        Logging.logd("Starting url loading");
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onLoadStarted(XWalkView view, String url) {
        Logging.logd("");
        Logging.logd("Web load started");
        super.onLoadStarted(view, url);
        CornBrowser.browserInputBar.setText(view.getUrl());
    }

    @Override
    public void onLoadFinished(XWalkView view, String url) {
        Logging.logd("");
        Logging.logd("Web load finished");
        super.onLoadFinished(view, url);
        CornBrowser.browserInputBar.setText(view.getUrl());
        currentWorkingUrl = view.getUrl();
    }

    @Override
    public void onProgressChanged(XWalkView view, int percentage) {
        super.onProgressChanged(view, percentage);
        Logging.logd("Actual loading progress: " + percentage);
        CornBrowser.getWebProgressBar().setProgress(percentage);
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

                        AssetHelper.getAssetString("error.html", CornBrowser.getContext()), // Error file

                        CornBrowser.getRStr(R.string.html_error_title),                     // Title 'Error'
                        errorCode,

                        String.format(CornBrowser.getRStr(R.string.html_error_body_info),
                                HttpStatusCodeHelper.getStatusCodeString(errorCode)), // Desc

                        CornBrowser.getRStr(R.string.html_reload_btn_value) // Reload button

                );
        view.load(null,
                AssetHelper.getAssetString("5.html", CornBrowser.getContext())
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

    public void onReceivedSslError(XWalkView view, ValueCallback<Boolean> callback, SslError error) {
        super.onReceivedSslError(view, callback, error);
    }

    public void onReceivedClientCertRequest(XWalkView view, ClientCertRequest handler) {
        super.onReceivedClientCertRequest(view, handler);
    }

    public void doUpdateVisitedHistory(XWalkView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
    }

}
