package org.xdevs23.ui.view.listview;

import android.content.Context;
import android.widget.ArrayAdapter;

import io.xdevs23.cornowser.browser.R;

/**
 * Custom ListView
 */
public class XDListView {
	
	public static ArrayAdapter<String> create(Context context, String[] content) {
		return new ArrayAdapter<String>(context, R.layout.listview_activity, content);
	}

    public static ArrayAdapter<String> createLittle(Context context, String[] content) {
        return new ArrayAdapter<String>(context, R.layout.listview_little, content);
    }
	
}
