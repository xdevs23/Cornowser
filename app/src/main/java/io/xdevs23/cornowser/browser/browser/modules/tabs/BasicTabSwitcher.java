package io.xdevs23.cornowser.browser.browser.modules.tabs;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import org.xdevs23.general.ExtendedAndroidClass;

public abstract class BasicTabSwitcher extends ExtendedAndroidClass implements TabSwitcherBase {

    public SwitcherStatus switcherStatus = SwitcherStatus.HIDDEN;

    protected int currentTab;
    protected TabStorage tabStorage = new TabStorage();

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

    protected void setSwitcherView(View view) {
        rootView.addView(view);
    }

    protected RelativeLayout getRootView() {
        return rootView;
    }

}
