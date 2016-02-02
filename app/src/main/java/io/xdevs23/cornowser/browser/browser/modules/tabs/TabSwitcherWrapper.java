package io.xdevs23.cornowser.browser.browser.modules.tabs;

public class TabSwitcherWrapper {

    private BasicTabSwitcher tabSwitcher;


    public TabSwitcherWrapper(BasicTabSwitcher tabSwitcher) {
        this.tabSwitcher = tabSwitcher;
    }

    public BasicTabSwitcher getTabSwitcher() {
        return tabSwitcher;
    }

}
