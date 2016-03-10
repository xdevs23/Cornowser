package io.xdevs23.cornowser.browser.browser.modules.tabs;

import android.app.Activity;
import android.content.Context;

import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

public class Tab {

    protected String tabUrl, tabTitle;
    protected CrunchyWalkView webView;

    public int tabId = 0;


    public Tab() {
        tabUrl      = "";
        tabTitle    = "";
    }

    public Tab(String url) {
        this();
        tabUrl = url;
    }

    public Tab(String url, String title) {
        this(url);
        tabTitle = title;
    }

    public Tab(int tabId) {
        this.tabId = tabId;
    }

    public Tab(TabStorage tabStorage, int tab) {
        this(tabStorage.getTab(tab).tabUrl, tabStorage.getTab(tab).tabTitle);
    }

    public Tab initWithWebRender(Context context, Activity activity) {
        return setWebRender(new CrunchyWalkView(context, activity));
    }

    public Tab setWebRender(CrunchyWalkView view) {
        webView = view;
        return this;
    }

    public String getUrl() {
        return tabUrl;
    }

    public String getTitle() {
        return tabTitle;
    }

    public void setUrl(String url) {
        tabUrl = url;
    }

    public void setTitle(String title) {
        tabTitle = title;
    }

    public Tab setId(int id) {
        tabId = id;
        return this;
    }

    public static String getTabUrl(Tab tab) {
        return tab.getUrl();
    }

    public static String getTabTitle(Tab tab) {
        return tab.getTitle();
    }

    public CrunchyWalkView getWebView() {
        return webView;
    }

}
