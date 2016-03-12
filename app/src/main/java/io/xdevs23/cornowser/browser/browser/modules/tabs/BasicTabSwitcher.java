package io.xdevs23.cornowser.browser.browser.modules.tabs;

import android.content.Context;
import android.widget.RelativeLayout;

import org.xdevs23.general.ExtendedAndroidClass;

import io.xdevs23.cornowser.browser.CornBrowser;

public abstract class BasicTabSwitcher extends ExtendedAndroidClass implements TabSwitcherBase {

    public SwitcherStatus switcherStatus = SwitcherStatus.HIDDEN;


    protected TabStorage tabStorage = new TabStorage();

    protected int currentTab = -1;


    private RelativeLayout rootView;


    public enum SwitcherStatus {
        VISIBLE,
        HIDDEN
    }

    public BasicTabSwitcher(Context context, RelativeLayout rootView) {
        super(context);
        this.rootView = rootView;
        init();
    }

    public BasicTabSwitcher(RelativeLayout rootView) {
        this(rootView.getContext(), rootView);
    }

    public abstract void init();


    @Override
    public Tab getCurrentTab() {
        return tabStorage.getTab(currentTab);
    }

    @Override
    public TabStorage getTabStorage() {
        return tabStorage;
    }

    @Override
    public void addTab(Tab tab) {
        tab.setId(tabStorage.getTabCount());
        tabStorage.addTab(tab);
        CornBrowser.publicWebRender = tab.webView;
        currentTab = tab.tabId;
    }

    @Override
    public void addTab(String url) {
        addTab(new Tab(url));
    }

    @Override
    public void addTab(String url, String title) {
        addTab(new Tab(url, title));
    }

    @Override
    public void removeTab(Tab tab) {
        tabStorage.removeTab(tab);
        currentTab = 0;
    }

    @Override
    public void removeTab(String url) {
        tabStorage.removeTab(url);
        currentTab = 0;
    }

    @Override
    public void removeTab(int tabIndex) {
        tabStorage.removeTab(tabIndex);
        currentTab = 0;
    }

    @Override
    public void switchTab(int tab) {
        for ( Tab t : tabStorage.getTabList() ) {
            if(t != tabStorage.getTab(tab)) {
                t.webView.pauseTimers();
                t.webView.onHide();
            }
        }
        tabStorage.getTab(tab).webView.resumeTimers();
        tabStorage.getTab(tab).webView.onShow();
        showTab(tabStorage.getTab(tab));
    }

    @Override
    public void showTab(Tab tab) {
        try {
            tab.webView.drawWithColorMode();
        } catch (Exception ex) {
            // Ignore
        }
    }

    @Override
    public void hideSwitcher() {
        switcherStatus = SwitcherStatus.HIDDEN;
    }

    @Override
    public void showSwitcher() {
        switcherStatus = SwitcherStatus.VISIBLE;
    }

    public void switchSwitcher() {
        if(switcherStatus == SwitcherStatus.HIDDEN) showSwitcher();
        else hideSwitcher();
    }

    protected RelativeLayout getRootView() {
        return rootView;
    }

    public void changeCurrentTab(String url, String title) {
        getCurrentTab().setUrl(url);
        getCurrentTab().setTitle(title);
    }

    public void fixWebResumation() {
        for ( Tab t : tabStorage.getTabList() ) {
            t.webView.resumeTimers();
            t.webView.onShow();
        }
        switchTab(currentTab);
    }

    public abstract void updateAllTabs();

}
