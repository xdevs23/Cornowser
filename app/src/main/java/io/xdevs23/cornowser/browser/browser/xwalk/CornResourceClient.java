package io.xdevs23.cornowser.browser.browser.xwalk;


import android.net.http.SslError;
import android.webkit.ValueCallback;

import org.xdevs23.android.content.res.AssetHelper;
import org.xdevs23.debugUtils.Logging;
import org.xdevs23.net.http.HttpStatusCodeHelper;
import org.xwalk.core.ClientCertRequest;
import org.xwalk.core.XWalkHttpAuthHandler;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

import java.util.regex.Pattern;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;

/**
 * A cool "resource client" for our crunchy view
 */
public class CornResourceClient extends XWalkResourceClient {

    public CornResourceClient(XWalkView view) {
        super(view);
    }

    public static Pattern urlRegEx = Pattern.compile(
            "(" +
                    "(^(https|http|ftp|rtmp)://.*[.].*)|" +         // Protocols for adresses
                    "^(file:///).*|" +                              // Local files
                    "(javascript:).*" +                             // Javascript

                    ")"
    );

    public static Pattern urlSecRegEx = Pattern.compile(
            "(" +
                    "(.*[.].*)|" +                                  // Adresses without protocol
                    "(localhost).*|" +                              // localhost
                    "(.*[.].*[.].*[.].*)|" +                        // IPv4 adresses
                    "(.*(::|:).*)|" +                               // IPv6 adresses
                    "(.*@.*[.].*)" +                                // user@host.domain

                    ")"
    );

    @Override
    public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
        Logging.logd("Starting url loading");
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onLoadStarted(XWalkView view, String url) {
        Logging.logd("Web load started");
        super.onLoadStarted(view, url);
        CornBrowser.browserInputBar.setText(view.getUrl());
    }

    @Override
    public void onLoadFinished(XWalkView view, String url) {
        Logging.logd("Web load finished");
        super.onLoadFinished(view, url);
        CornBrowser.browserInputBar.setText(view.getUrl());
    }

    @Override
    public void onProgressChanged(XWalkView view, int percentage) {
        super.onProgressChanged(view, percentage);
        CornBrowser.getWebProgressBar().setProgress((float) percentage / 100);
    }

    @Override
    public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
        Logging.logd("Web load error on url: " + failingUrl + ", description: " + description);
        String errHtmlContent =
                String.format(

                        AssetHelper.getAssetString("error.html", CornBrowser.getContext()), // Error file

                        CornBrowser.getRStr(R.string.html_error_title),                     // Title 'Error'
                        errorCode,

                        String.format(CornBrowser.getRStr(R.string.html_error_body_info),
                                HttpStatusCodeHelper.getStatusCodeString(errorCode)), // Desc

                        CornBrowser.getRStr(R.string.html_reload_btn_value) // Reload button

                );
        CornBrowser.getWebEngine().load(null,
                AssetHelper.getAssetString("5.html", CornBrowser.getContext())
                .replace("<!-- BODY -->", errHtmlContent));
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
