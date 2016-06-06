package org.xdevs23.android.content.res;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;

import org.xdevs23.io.stream.InputStreamUtils;

import java.io.IOException;

public class AssetHelper {

    public static String getAssetString(@RawRes String assetPath, @NonNull Context context) {
        try {
            return InputStreamUtils.readToString(context.getAssets().open(assetPath));
        } catch(IOException ex) {
            return "";
        }
    }

}
