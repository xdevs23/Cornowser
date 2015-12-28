package org.xdevs23.android.content.res;

import android.content.Context;

import org.xdevs23.io.stream.InputStreamUtils;

import java.io.IOException;

public class AssetHelper {

    public static String getAssetString(String assetPath, Context context) {
        try {
            return InputStreamUtils.readToString(context.getAssets().open(assetPath));
        } catch(IOException ex) {
            return "";
        }
    }

}
