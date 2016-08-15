package org.xdevs23.ui.view.listview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    public static ArrayAdapter<String> createDual(Context context, String[] first, String[] second) {
        String[] all = new String[first.length * 2];
        for ( int i = 0; i < first.length; i++ ) {
            all[i]                =  first[i];
            all[first.length + i] = second[i];
        }
        return new DualAdapter(context, R.layout.listview_activity_dualrow, all);
    }

    public static class DualAdapter extends ArrayAdapter<String> {

        private int allLength = 0;

        protected static class ViewHolder {
            public TextView TextView1, TextView2;
        }

        public int getCount() {
            return allLength / 2;
        }

        public long getItemId(int position) {
            return position;
        }

        public DualAdapter(Context context, int resource, String[] content) {
            super(context, resource, content);
            allLength = content.length;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            RelativeLayout mainView = (RelativeLayout)
                    inflater.inflate(R.layout.listview_activity_dualrow, null);
            holder.TextView1 = (TextView) mainView.getChildAt(0);
            holder.TextView2 = (TextView) mainView.getChildAt(1);
            holder.TextView1.setText(getItem(position));
            holder.TextView2.setText(getItem(allLength / 2 + position));
            holder.TextView1.setSingleLine();
            holder.TextView1.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            holder.TextView2.setSingleLine();
            holder.TextView2.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            return mainView;
        }
    }
	
}
