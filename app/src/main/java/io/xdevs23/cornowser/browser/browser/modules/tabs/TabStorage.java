package io.xdevs23.cornowser.browser.browser.modules.tabs;

import org.xdevs23.general.ExtendedAndroidClass;

import java.util.ArrayList;

import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

public class TabStorage extends ExtendedAndroidClass implements TabStorageBase {

    private ArrayList<Tab> tabList = new ArrayList<Tab>();

    @Override
    public Tab addTab() {
        Tab tab = new Tab();
        tab.webView = new CrunchyWalkView(getContext());
        tabList.add(tab);
        return tab;
    }

    @Override
    public Tab addTab(String url) {
        Tab tab = new Tab(url);
        tab.webView = new CrunchyWalkView(getContext());
        tab.webView.load(url);
        tabList.add(tab);
        return tab;
    }

    public Tab addTab(CrunchyWalkView view) {
        Tab tab = new Tab(view.getUrl(), view.getTitle());
        tab.webView = view;
        tabList.add(tab);
        return tab;
    }

    @Override
    public void addTab(Tab tab) {
        tabList.add(tab);
    }

    /**
     * Makes sure that the view is stopped and memory can be freed up by GC
     * @param tab Tab to recycle
     */
    protected void recycleTab(Tab tab) {
        tab.webView.stopLoading();
        tab.webView = null;
    }

    @Override
    public void removeTab(int tabIndex) {
        recycleTab(tabList.get(tabIndex));
        tabList.remove(tabIndex);
    }

    @Override
    public void removeTab(Tab tab) {
        tabList.remove(tab);
        recycleTab(tab);
    }

    @Override
    public void removeTab(String url) {
        for ( Tab t : tabList )
            if(t.tabUrl.equals(url)) { removeTab(t); return; }
    }

    @Override
    public void removeLastTab() {
        removeTab(tabList.size() - 1);
    }

    @Override
    public Tab getTab(int tabIndex) {
        return tabList.get(tabIndex);
    }

    @Override
    public Tab getTab(String url) {
        for ( Tab t : tabList )
            if(t.tabUrl.equals(url)) return t;
        return new Tab();
    }

    @Override
    public Tab getLastTab() {
        return tabList.get(tabList.size() - 1);
    }

    @Override
    public ArrayList<Tab> getTabList() {
        return tabList;
    }

    public int getTabCount() {
        return tabList.size();
    }

    public int getTabCountIndex() {
        return (tabList.size() - 1);
    }

    public int getTabIndex(Tab tab) {
        for ( int i = 0; i < tabList.size(); i++ )
            if(tabList.get(i).tabUrl.equals(tab.tabUrl) && tabList.get(i).tabTitle.equals(tab.tabTitle))
                return i;

        throw new IllegalStateException();
    }

}
