package io.xdevs23.cornowser.browser.browser.modules.adblock;

import org.xdevs23.crypto.hashing.HashUtils;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;
import org.xdevs23.file.FileUtils;
import org.xdevs23.general.StringManipulation;
import org.xdevs23.net.DownloadUtils;
import org.xdevs23.net.NetUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import io.xdevs23.cornowser.browser.CornBrowser;

public class AdBlockManager {

    protected static String[] hosts = new String[] {""};

    private static String adBlockFile =
            new File(CornBrowser.getContext().getExternalFilesDir(null), "adblock.txt")
                    .getAbsolutePath();

    private static OnHostsUpdatedListener defaultHostsUpdatedListener = new OnHostsUpdatedListener() {
        @Override
        public void onUpdateFinished() {
            // Do nothing
        }
    };

    private static OnHostsUpdatedListener hostsUpdatedListener = defaultHostsUpdatedListener;

    private static boolean areHostsLoaded = false;

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
        areHostsLoaded = false;
        hosts = FileUtils.readFileStringArray(adBlockFile);
        areHostsLoaded = true;
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

        Logging.logd("AdBlock: Downloading hosts...");
        // Create new string array to later store host files
        String[] adblockFiles = new String[AdBlockConst.HOST_FILES_ADBLOCK.length];

        // Download all host files and store into string array
        for ( int i = 0; i < adblockFiles.length; i++)
            adblockFiles[i] = DownloadUtils.downloadString(AdBlockConst.HOST_FILES_ADBLOCK[i]);

        Logging.logd("AdBlock: Processing hosts...");
        // Filter/parse the host files
        ArrayList<String> filteredAdBlockFiles = new ArrayList<String>();
        for ( String s : adblockFiles )
            Collections.addAll(filteredAdBlockFiles, AdBlockParser.parseRawAdBlockList(s.split("\n")));

        // Merge all host files together and save them into a single string
        StringBuilder sb = new StringBuilder();
        for ( String s : filteredAdBlockFiles )
            sb.append(s).append("\n");

        Logging.logd("AdBlock: Checking...");
        Logging.logd("AdBlock: existing hosts array length: " + hosts.length);
        Logging.logd("AdBlock: new hosts array length:      " + filteredAdBlockFiles.size());
        Logging.logd("AdBlock: new merged hosts length:     " + sb.length());

        boolean allowUpdate = true;

        if(hosts.length > 1) {
            // Check if the file needs to be updated
            String mergedExistingHosts = StringManipulation.arrayToString(hosts);
            Logging.logd("AdBlock: existing hosts length: " + mergedExistingHosts.length());
            String existingHash = HashUtils.hash(mergedExistingHosts,
                    HashUtils.HashTypes.SHA1);
            String newHash = HashUtils.hash(sb.toString(), HashUtils.HashTypes.SHA1);

            Logging.logd("AdBlock: Existing hash: " + existingHash);
            Logging.logd("AdBlock: New hash:      " + newHash);

            allowUpdate = !existingHash.equals(newHash);
        } else {
            Logging.logd("AdBlock: Hash check skipped. Hosts too small");
        }

        if (allowUpdate) {
            Logging.logd("AdBlock: Writing to file...");
            FileUtils.writeFileString(adBlockFile, sb.toString()); // Write the merged host files to file
        } else Logging.logd("AdBlock: File already up-to-date. No action is taken.");

        Logging.logd("AdBlock: Finished!");
        hostsUpdatedListener.onUpdateFinished();
        hostsUpdatedListener = defaultHostsUpdatedListener;
    }

    public static boolean isAdBlockedHost(String url) {
        if(!CornBrowser.getBrowserStorage().isAdBlockEnabled())             return false;
        if(!areHostsLoaded)                                                 return false;
        if(hosts == null || hosts.length <= 1)                              return false;
        if(AdBlockParser.isHostListed(url, AdBlockConst.WHITELISTED_HOSTS)) return false;
        try {
            return AdBlockParser.isHostListed(url, hosts);
        } catch(Exception ex) {
            Logging.logd("AdBlock: Exception thrown while checking");
            StackTraceParser.logStackTrace(ex);
            return false;
        }
    }

    public static void setOnHostsUpdatedListener(OnHostsUpdatedListener listener) {
        hostsUpdatedListener = listener;
    }


    public static interface OnHostsUpdatedListener {
        public void onUpdateFinished();
    }

}
