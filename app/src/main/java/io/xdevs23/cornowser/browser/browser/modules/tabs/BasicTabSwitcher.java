package io.xdevs23.cornowser.browser.browser.modules.tabs;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import org.xdevs23.general.ExtendedAndroidClass;

import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

public abstract class BasicTabSwitcher extends ExtendedAndroidClass implements TabSwitcherBase {

    protected int currentTab;
    protected TabStorage tabStorage = new TabStorage();

    private RelativeLayout rootView;

    private CrunchyWalkView webRender;

    public BasicTabSwitcher(Context context, RelativeLayout rootView, CrunchyWalkView webRender) {
        super(context);
        this.rootView = rootView;
        this.webRender = webRender;
        init();
    }

    public BasicTabSwitcher(RelativeLayout rootView, CrunchyWalkView webRender) {
        this(webRender.getContext(), rootView, webRender);
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
        tabStorage.addTab(tab);
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
    }

    @Override
    public void removeTab(String url) {
        tabStorage.removeTab(url);
    }

    @Override
    public void removeTab(int tabIndex) {
        tabStorage.removeTab(tabIndex);
    }

    protected void setSwitcherView(View view) {
        rootView.addView(view);
    }

    protected CrunchyWalkView getWebRender() {
        return webRender;
    }

    protected RelativeLayout getRootView() {
        return rootView;
    }

}
