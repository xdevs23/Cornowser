package io.xdevs23.cornowser.browser.browser.modules.tabs;

public interface TabSwitcherBase {

    void switchTab(int tab);
    void showTab(Tab tab);
    void showTab(Tab tab, boolean isNew);

    Tab getCurrentTab();

    TabStorage getTabStorage();

    void addTab(Tab tab);
    void addTab(String url);
    void addTab(String url, String title);

    void removeTab(Tab tab);
    void removeTab(String url);
    void removeTab(int tabIndex);

    void showSwitcher();
    void hideSwitcher();

}
