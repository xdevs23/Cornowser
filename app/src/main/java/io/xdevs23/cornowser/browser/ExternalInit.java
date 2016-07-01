package io.xdevs23.cornowser.browser;

import android.content.Context;

import io.xdevs23.cornowser.browser.activity.BookmarkHistoryActivity;

public class ExternalInit {

    private ExternalInit() {

    }

    protected static void initBookmarkHistory(Context context) {
        BookmarkHistoryActivity.tabTitles =
                context.getResources().getStringArray(R.array.bkmhis_tab_titles);
    }

    public static void initExternal(Context context) {
        initBookmarkHistory(context);
    }

}
