package org.xdevs23.general;

import android.content.Context;

public class ExtendedAndroidClass {

    private Context myContext;

    public ExtendedAndroidClass() {
        this.myContext = null;
    }

    public ExtendedAndroidClass(Context context) {
        this.myContext = context;
    }

    public ExtendedAndroidClass setContext(Context context) {
        this.myContext = context;
        return this;
    }

    public Context getContext() {
        return myContext;
    }

}
