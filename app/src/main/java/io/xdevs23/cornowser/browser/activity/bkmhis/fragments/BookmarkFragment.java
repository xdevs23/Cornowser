package io.xdevs23.cornowser.browser.activity.bkmhis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.xdevs23.management.config.SPConfigEntry;
import org.xdevs23.ui.view.listview.XDListView;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.BrowserStorage;

public class BookmarkFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        SPConfigEntry bookmarks =
                new SPConfigEntry(CornBrowser.getBrowserStorage()
                        .getString(BrowserStorage.BPrefKeys.bookmarksPref, ""));
        ((ListView)view.findViewById(R.id.bookmarks_list))
            .setAdapter(XDListView.create(getContext(),
                bookmarks.keys.toArray(new String[0])));
        return view;
    }

}
