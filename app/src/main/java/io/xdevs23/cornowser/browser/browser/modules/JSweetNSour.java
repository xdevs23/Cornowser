package io.xdevs23.cornowser.browser.browser.modules;

import android.content.Context;
import android.support.annotation.RawRes;

import org.xdevs23.android.content.res.AssetHelper;

import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

public class JSweetNSour {

    public static void fillTemplate(@RawRes String templateRes, String content, CrunchyWalkView view) {
        CornHandler.HandlerStorage.currentTemplateContent =
                String.format(
                        AssetHelper.getAssetString("appScripts/templateFiller.js", view.getContext()),
                        content.replace("\"", "\\").replace("\n", "\" + \"")
                );
        view.load("file:///android_asset/" + templateRes);
    }

    public static void fillHTML5Template(String content, CrunchyWalkView view) {
        fillTemplate("htdocs/5.html", content, view);
    }

    public static String putCSSes(String content, Context c) {
        return
                String.format(
                        content,

                        AssetHelper.getAssetString("htdocs/Design/appMenuBar.css",  c),
                        AssetHelper.getAssetString("htdocs/Design/classes.css",     c),
                        AssetHelper.getAssetString("htdocs/Design/objects.css",     c),
                        AssetHelper.getAssetString("htdocs/Design/social.css",      c),
                        AssetHelper.getAssetString("htdocs/Design/transitions.css", c),
                        AssetHelper.getAssetString("htdocs/Design/webdesign.css",   c)

                );
    }

}
