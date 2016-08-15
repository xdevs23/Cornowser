package io.xdevs23.cornowser.browser.browser.modules;

import android.app.Activity;

import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.browser.xwalk.CornResourceClient;
import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

public class CornHandler {

    public static final String dblpntSplitter = "_|[[??!_!??]]|_";

    public static class HandlerStorage {
        public static String
                currentTemplateContent = "";
    }

    public enum CornRequests {
        loadWorkingUrl,
        setWebThemeColor,
        startTemplateFilling,
        handleLongpressLink,
        handleLongpressImage
    }

    public static void handleRequest(String req, Activity activity,
                                     CrunchyWalkView view, String url,
                                     CornResourceClient resClient) {

        Logging.logd("Handling request " + req);

        String internalReq = req.split("://", 2)[1];

        internalReq = internalReq.replace("::", dblpntSplitter);

        String reqParts[] = internalReq.split(":");

        for( int i = 0; i < reqParts.length; i++)
            reqParts[i] = reqParts[i].replace(dblpntSplitter, "::");

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

    public static void sendRawJSRequest(CrunchyWalkView view, String req) {
        view.evaluateJavascript(req, null);
    }

}
