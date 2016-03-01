package org.xdevs23.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import java.net.HttpURLConnection;
import java.net.URL;

public final class NetUtils {

    private NetUtils() {

    }

    public static boolean isInternetAvailable() {
        try {
            HttpURLConnection urlc = (HttpURLConnection)
                    (new URL("http://clients3.google.com/generate_204")
                            .openConnection());
            urlc.setRequestProperty("User-Agent", "Android");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(2400);
            urlc.connect();
            return (urlc.getResponseCode() == 204 &&
                    urlc.getContentLength() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return mWifi.isConnected();
        } else {
            Network[] nets = connManager.getAllNetworks();
            for(Network net : nets) {
                NetworkInfo ni = connManager.getNetworkInfo(net);
                if (ni.getType() == ConnectivityManager.TYPE_WIFI
                        || ni.getType() == ConnectivityManager.TYPE_ETHERNET
                        && ni.isConnected()) return true;
            }
        }
        return false;
    }

}
