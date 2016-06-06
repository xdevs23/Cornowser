package org.xdevs23.android.content.share;

import android.app.Activity;
import android.content.Intent;

public class ShareUtil {

    private ShareUtil() {

    }

    public static void shareText(String title, String text, Activity activity) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(shareIntent, title));
    }

}
