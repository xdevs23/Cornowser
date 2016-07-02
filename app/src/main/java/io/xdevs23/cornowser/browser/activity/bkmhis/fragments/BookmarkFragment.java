package io.xdevs23.cornowser.browser.activity.bkmhis.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.xdevs23.management.config.SPConfigEntry;
import org.xdevs23.ui.view.listview.XDListView;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.BrowserStorage;

public class BookmarkFragment extends Fragment {

    public SPConfigEntry bookmarks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        updateAdapter(view);
        return view;
    }

    public void updateAdapter(View view) {
        bookmarks =
                new SPConfigEntry(CornBrowser.getBrowserStorage()
                        .getString(BrowserStorage.BPrefKeys.bookmarksPref, ""));
        ListView lview = ((ListView)view.findViewById(R.id.bookmarks_list));
        // Set the adapter to display the bookmarks in a list
        lview.setAdapter(XDListView.createDual(getContext(),
                bookmarks.values.toArray(new String[bookmarks.values.size()]),
                bookmarks.keys.toArray(new String[bookmarks.keys.size()])));
        // When user clicks, open new tab with bookmark
        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CornBrowser.getTabSwitcher().addTab(
                        ((TextView)((ViewGroup)view).getChildAt(1)).getText().toString());
                startActivity(new Intent(getContext(), CornBrowser.class));
            }
        });
        // When user long clicks, remove bookmark
        lview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                bookmarks.removeValue(((TextView)((ViewGroup)view).getChildAt(1)).getText().toString());
                CornBrowser.getBrowserStorage().putString(
                        BrowserStorage.BPrefKeys.bookmarksPref, bookmarks.toString()
                );
                updateAdapter();
                return true;
            }
        });
    }

    public void updateAdapter() {
        updateAdapter(getView());
    }

}
