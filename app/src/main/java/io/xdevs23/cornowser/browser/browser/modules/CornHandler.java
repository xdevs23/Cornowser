package io.xdevs23.cornowser.browser.browser.modules;

import android.app.Activity;

import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.browser.xwalk.CornResourceClient;
import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

public class CornHandler {

    public enum CornRequests {
        loadWorkingUrl,
        setWebThemeColor
    }

    public static void handleRequest(String req, Activity activity,
                                     CrunchyWalkView view, String url,
                                     CornResourceClient resClient) {

        Logging.logd("Handling request " + req);

        String internalReq = req.split("://")[1];
        Logging.logd("  Req: " + internalReq);

        String reqParts[] = internalReq.split(":");

        String mainReq = reqParts[0];
        Logging.logd("  mainReq: " + mainReq);

        String reqParams[] = new String[reqParts.length - 1];

        System.arraycopy(reqParts, 1, reqParams, 0, reqParams.length);
        Logging.logd("  reqParams.length: " + reqParams.length);

        try {
            Logging.logd("  Handling...");
            boolean isFound = false;
            for( CornRequests r : CornRequests.values())
                if(r.name().toLowerCase().equals(mainReq.toLowerCase()))
                    isFound = true;
            if(!isFound) return;

            switch(CornRequests.valueOf(mainReq)) {
                case loadWorkingUrl: view.load(resClient.currentWorkingUrl, null); break;
                case setWebThemeColor:
                    if(reqParams[0].toLowerCase().equals("default"))
                        WebThemeHelper.resetWebThemeColor(CornBrowser.omnibox);
                    WebThemeHelper.setWebThemeColor(reqParams[0],
                            CornBrowser.omnibox, CornBrowser.getStaticWindow());
                    break;
                default: break;
            }
        } catch(Exception ex) {
            StackTraceParser.logStackTrace(ex);
        }

    }

    public static void sendJSRequestWithCallback(CrunchyWalkView view, String req, String... params) {
        StringBuilder sb = new StringBuilder();
        for ( String str : params ) sb.append(str).append(":");
        sb.deleteCharAt(sb.lastIndexOf(":"));
        view.evaluateJavascript("try{document.location.href=\"CornHandler://" + sb.toString() + "\";}catch(ex){" +
                "document.location.href=\"CornHandler://" + req + ":default\";}", null);
    }

}
