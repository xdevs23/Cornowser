package io.xdevs23.cornowser.browser.browser.modules.adblock;

import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;
import org.xdevs23.file.FileUtils;
import org.xdevs23.net.DownloadUtils;
import org.xdevs23.net.NetUtils;

import java.io.File;
import java.util.ArrayList;

import io.xdevs23.cornowser.browser.CornBrowser;

public class AdBlockManager {

    protected static String[] hosts;

    private static String adBlockFile =
            new File(CornBrowser.getContext().getExternalFilesDir(null), "adblock.txt")
                    .getAbsolutePath();


    private AdBlockManager() {

    }

    public static void initAdBlock() {
        try {
            Logging.logd("AdBlock: Starting initialization");
            Logging.logd("AdBlock: Hosts file is " + adBlockFile);
            loadHosts();
            downloadHostsAsync();
        } catch(Exception ex) {
            StackTraceParser.logStackTrace(ex);
        }
    }

    public static void loadHosts() {
        Logging.logd("AdBlock: Loading hosts...");
        hosts = FileUtils.readFileStringArray(adBlockFile);
        Logging.logd("AdBlock: All hosts loaded!");
    }

    public static void downloadHostsAsync() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                downloadHosts();
                loadHosts();
            }
        })).start();
    }

    public static void downloadHosts() {
        Logging.logd("AdBlock: Download of hosts started.");
        Logging.logd("AdBlock: Checking connection...");
        if(!NetUtils.isInternetAvailable()) return;

        Logging.logd("AdBlock: Downloading and processing files...");
        // Create new string array to later store host files
        String[] adblockFiles = new String[AdBlockConst.HOST_FILES_ADBLOCK.length];

        // Download all host files and store into string array
        for ( int i = 0; i < adblockFiles.length; i++)
            adblockFiles[i] = DownloadUtils.downloadString(AdBlockConst.HOST_FILES_ADBLOCK[i]);

        // Filter/parse the host files
        ArrayList<String> filteredAdBlockFiles = new ArrayList<String>();
        for ( String s : adblockFiles )
            for ( String sf : AdBlockParser.parseRawAdBlockList(s.split("\n")))
                filteredAdBlockFiles.add(sf);

        // Merge all host files together and save them into a single string
        StringBuilder sb = new StringBuilder();
        for ( String s : filteredAdBlockFiles )
            sb.append(s).append("\n");

        sb.delete(sb.length() - 2, sb.length() - 1); // Delete last new line character
        FileUtils.writeFileString(adBlockFile, sb.toString()); // Write the merged host files to file
        Logging.logd("AdBlock: Finished!");

    }

    public static boolean isAdBlockedHost(String url) {
        if(!CornBrowser.getBrowserStorage().isAdBlockEnabled()) return false;
        if(hosts.length <= 1) return false;
        try {
            return AdBlockParser.isHostListed(url, hosts);
        } catch(Exception ex) {
            Logging.logd("AdBlock: Exception thrown while checking");
            StackTraceParser.logStackTrace(ex);
            return false;
        }
    }

}
