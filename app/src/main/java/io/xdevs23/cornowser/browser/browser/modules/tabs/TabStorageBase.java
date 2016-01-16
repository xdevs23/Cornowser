package io.xdevs23.cornowser.browser.browser.modules.tabs;

import java.util.ArrayList;

public interface TabStorageBase {

    public abstract Tab addTab();
    public abstract Tab addTab(String url);
    public abstract void addTab(Tab tab);

    public abstract void removeTab(int tab);
    public abstract void removeTab(Tab tab);
    public abstract void removeTab(String url);
    public abstract void removeLastTab();

    public abstract Tab getTab(int tab);
    public abstract Tab getTab(String url);
    public abstract Tab getLastTab();
    public abstract ArrayList<Tab> getTabList();

}
