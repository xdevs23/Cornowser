package org.xdevs23.android.content.res;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;

import org.xdevs23.io.stream.InputStreamUtils;

import java.io.IOException;

public class AssetHelper {

    public static String getAssetString(@RawRes String assetPath, @NonNull Context c) {
        try {
            return InputStreamUtils.readToString(c.getAssets().open(assetPath));
        } catch(IOException ex) {
            return "";
        }
    }

}
