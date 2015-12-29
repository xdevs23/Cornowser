package io.xdevs23.cornowser.browser.browser.modules;

import android.app.Activity;

import org.xdevs23.debugUtils.StackTraceParser;

import io.xdevs23.cornowser.browser.browser.xwalk.CornResourceClient;
import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

public class CornHandler {

    public enum CornRequests {
        loadWorkingUrl
    }

    public static void handleRequest(String req, Activity activity,
                                     CrunchyWalkView view, String url,
                                     CornResourceClient resClient) {

        String internalReq = req.split("(://)", 1)[1];

        String reqParts[] = internalReq.split("(:)");

        String mainReq = reqParts[0];

        String reqParams[] = new String[reqParts.length - 2];

        System.arraycopy(reqParts, 1, reqParams, 0, reqParams.length);

        try {
            switch(CornRequests.valueOf(mainReq)) {
                case loadWorkingUrl: view.load(resClient.currentWorkingUrl, null); break;
                default: break;
            }
        } catch(Exception ex) {
            StackTraceParser.logStackTrace(ex);
        }

    }

}
