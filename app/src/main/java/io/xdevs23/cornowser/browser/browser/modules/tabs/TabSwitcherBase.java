package io.xdevs23.cornowser.browser.browser.modules.tabs;

public interface TabSwitcherBase {

    public abstract void switchTab(int tab);
    public abstract void showTab(Tab tab);

    public abstract Tab getCurrentTab();

    public abstract TabStorage getTabStorage();

    public abstract void addTab(Tab tab);
    public abstract void addTab(String url);
    public abstract void addTab(String url, String title);

    public abstract void removeTab(Tab tab);
    public abstract void removeTab(String url);
    public abstract void removeTab(int tabIndex);

    public abstract void showSwitcher();
    public abstract void hideSwitcher();

}
