package org.xdevs23.net;

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

}
