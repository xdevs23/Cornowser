package org.xdevs23.android.content;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtil {

    private ClipboardUtil() {

    }

    public static void copyIntoClipboard(String value, Context context) {
        copyIntoClipboard("clipboardItem", value, context);
    }

    public static void copyIntoClipboard(String label, String content, Context context) {
        ClipboardManager clipboard =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, content);
        clipboard.setPrimaryClip(clip);
    }

}
