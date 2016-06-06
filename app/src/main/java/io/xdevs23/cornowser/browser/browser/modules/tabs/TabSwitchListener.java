package io.xdevs23.cornowser.browser.browser.modules.tabs;

public interface TabSwitchListener {

    void onTabAdded(Tab tab);
    void onTabRemoved(Tab tab);
    void onTabSwitched(Tab tab);
    void onTabChanged(Tab tab);
    void onTabChanged(Tab tab, boolean forcing);

}
