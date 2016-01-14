package io.xdevs23.cornowser.browser.browser.modules;

import android.app.Activity;

import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.browser.xwalk.CornResourceClient;
import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

public class CornHandler {

    public static class HandlerStorage {
        public static String
                currentTemplateContent = "";
    }

    public enum CornRequests {
        loadWorkingUrl,
        setWebThemeColor,
        startTemplateFilling
    }

    public static void handleRequest(String req, Activity activity,
                                     CrunchyWalkView view, String url,
                                     CornResourceClient resClient) {

        Logging.logd("Handling request " + req);

        String internalReq = req.split("://")[1];

        String reqParts[] = internalReq.split(":");

        String mainReq = reqParts[0];

        String reqParams[] = new String[reqParts.length - 1];

        System.arraycopy(reqParts, 1, reqParams, 0, reqParams.length);

        try {
            boolean isFound = false;
            for( CornRequests r : CornRequests.values())
                if(r.name().equalsIgnoreCase(mainReq))
                    isFound = true;
            if(!isFound) return;

            switch(CornRequests.valueOf(mainReq)) {
                case loadWorkingUrl: view.load(resClient.currentWorkingUrl); break;
                case setWebThemeColor:
                    if(reqParams[0].equalsIgnoreCase("default"))
                        WebThemeHelper.resetWebThemeColorAlt(CornBrowser.omnibox,
                                CornBrowser.getStaticWindow());
                    WebThemeHelper.setWebThemeColor(reqParams[0],
                            CornBrowser.omnibox, CornBrowser.getStaticWindow());
                    break;
                case startTemplateFilling:
                    view.evaluateJavascript(HandlerStorage.currentTemplateContent, null);
                    HandlerStorage.currentTemplateContent = "";
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

    public static void sendRawJSRequest(CrunchyWalkView view, String req) {
        view.evaluateJavascript(req, null);
    }

    public static void evalJSAlt(CrunchyWalkView view, String req) {
        view.load("javascript:" + req);
    }

}
