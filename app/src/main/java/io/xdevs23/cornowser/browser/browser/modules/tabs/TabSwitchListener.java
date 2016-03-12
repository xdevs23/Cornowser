package io.xdevs23.cornowser.browser.browser.modules.tabs;

public interface TabSwitchListener {

    public abstract void onTabAdded(Tab tab);
    public abstract void onTabRemoved(Tab tab);
    public abstract void onTabSwitched(Tab tab);
    public abstract void onTabChanged(Tab tab);
    public abstract void onTabChanged(Tab tab, boolean forcing);

}
