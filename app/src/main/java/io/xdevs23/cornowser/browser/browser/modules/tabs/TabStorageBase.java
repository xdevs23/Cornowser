package io.xdevs23.cornowser.browser.browser.modules.tabs;

import java.util.ArrayList;

public interface TabStorageBase {

    Tab addTab();
    Tab addTab(String url);
    void addTab(Tab tab);

    void removeTab(int tab);
    void removeTab(Tab tab);
    void removeTab(String url);
    void removeLastTab();

    Tab getTab(int tab);
    Tab getTab(String url);
    Tab getLastTab();

    ArrayList<Tab> getTabList();

}
