package io.xdevs23.cornowser.browser.browser.xwalk;


import org.xdevs23.debugUtils.Logging;
import org.xwalk.core.XWalkHttpAuthHandler;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;
import org.xwalk.core.internal.XWalkWebResourceResponseBridge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.xdevs23.cornowser.browser.CornBrowser;

public class CornResourceClient extends XWalkResourceClient {

    public CornResourceClient(XWalkView view) {
        super(view);
    }

    public static Pattern urlRegEx = Pattern.compile(
            "(" +
                    "(^(https|http|ftp|file|rtmp)://.*[.].*)|" +    // Protocols for adresses
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
        super.onReceivedLoadError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedHttpAuthRequest(XWalkView view, XWalkHttpAuthHandler handler, String host, String realm) {
        Logging.logd("Web received http authentication request");
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

}
