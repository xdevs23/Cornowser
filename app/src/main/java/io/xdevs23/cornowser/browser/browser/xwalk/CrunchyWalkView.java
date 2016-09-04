package io.xdevs23.cornowser.browser.browser.xwalk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import org.xdevs23.android.content.ClipboardUtil;
import org.xdevs23.android.content.res.AssetHelper;
import org.xdevs23.android.content.share.ShareUtil;
import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;
import org.xdevs23.file.FileUtils;
import org.xdevs23.general.StringManipulation;
import org.xdevs23.general.URLEncode;
import org.xdevs23.management.config.SPConfigEntry;
import org.xdevs23.net.DownloadUtils;
import org.xdevs23.threads.Sleeper;
import org.xdevs23.ui.view.listview.XDListView;
import org.xwalk.core.XWalkHitTestResult;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkView;

import java.util.regex.Matcher;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.BrowserStorage;
import io.xdevs23.cornowser.browser.browser.modules.CornHandler;
import io.xdevs23.cornowser.browser.browser.modules.tabs.Tab;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxAnimations;
import io.xdevs23.cornowser.browser.browser.modules.ui.RenderColorMode;

/**
 * A delicious and crunchy XWalkView with awesome features
 */
@SuppressLint("ViewConstructor") // We won't be doing this anyways
public class CrunchyWalkView extends XWalkView {

    public static final String HTTP_PREFIX = "http://";
    public static String
            chromeVer = "Chrome/55.0.2849.1",
            webKitVer = "537.36",
            kernelVer = System.getenv("os.version"),
            userAgent =
            "Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + ") " +
                    "AppleWebKit/" + webKitVer + " (KHTML, like Gecko) Cornowser/%s " + chromeVer +
                    " Mobile Safari/" + webKitVer,
            desktopAgent = "Mozilla/5.0 (Linux; "
                    + (kernelVer == null || kernelVer.isEmpty() ? "Generic"
                    :  kernelVer) + ") AppleWebKit/537.36 (KHTML, like Gecko) " + chromeVer +
                    " Safari/" + webKitVer;

    public int currentProgress = 0;

    protected Bitmap favicon;

    protected String currentUrl, currentTitle;

    protected Tab tab;

    protected boolean
            isLongPressDialogAv = true,
            isIncognito         = false;

    private CornResourceClient resourceClient;
    private CornUIClient       uiClient;

    private boolean
            isHistoryBeingWritten   = false;

    private Thread historyUpdateThread;

    private SPConfigEntry browsingHistory;

    private int lastBgColor = 0;

    private CrunchyWalkView init() {
        Logging.logd("    Initializing our crunchy XWalkView :P");

        Logging.logd("      User agent");
        userAgent =
                String.format(userAgent, ConfigUtils.getVersionName(CornBrowser.getContext()));

        Logging.logd("      Resource Client");
        resourceClient  = new CornResourceClient(this);
        Logging.logd("      UI Client");
        uiClient        = new CornUIClient      (this);
        Logging.logd("      Applying clients");
        setResourceClient(getResourceClient());
        setUIClient(getUIClient());

        Logging.logd("      Configuring settings");
        setUserAgentString(userAgent);

        Logging.logd("      Registering onTouchListener");
        setOnTouchListener(OmniboxAnimations.mainOnTouchListener);
        // Thanks to chuan.liu (XWALK-7233) for the awesome example
        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                XWalkHitTestResult result = ((CrunchyWalkView) v).getHitTestResult();
                XWalkHitTestResult.type resultType = result.getType();
                if(result.getExtra() == null) return false;
                switch (resultType) {
                    case IMAGE_TYPE:
                        onLongPress(result.getExtra(), result.getExtra(), true);
                        break;
                    case PHONE_TYPE:
                        break;
                    default:
                        onLongPress(result.getExtra(), result.getExtra(), false);
                        break;
                }
                return true;
            }
        });

        if(!isIncognito) {
            Logging.logd("      Loading history");
            browsingHistory = new SPConfigEntry(CornBrowser.getBrowserStorage()
                    .getString(BrowserStorage.BPrefKeys.historyPref, ""));
        }

        drawWithColorMode();

        Logging.logd("      Done!");
        return this;
    }

    private CrunchyWalkView(Context context, Activity activity) {
        super(context, activity);
    }

    public static CrunchyWalkView newInstance(Activity activity, boolean isIncognito, Tab tab) {
        return (new CrunchyWalkView(activity.getApplicationContext(), activity))
                .setIncognito(isIncognito)
                .setTab(tab)
                .init();
    }

    private CrunchyWalkView setTab(Tab tab) {
        this.tab = tab;
        return this;
    }

    private CrunchyWalkView setIncognito(boolean enable) {
        isIncognito = enable;
        return this;
    }

    @Override
    public void onDestroy() {
        stopLoading();

        // Wait & shut down threads
        try {
            isHistoryBeingWritten = false;
            historyUpdateThread.join();
            historyUpdateThread = null;
        } catch(Exception ex) {
            Logging.logd("[CrunchyWalkView] Could not shut down all threads properly.");
        }

        // Shut down webview
        pauseTimers();
        onHide();

        // Clear caches for visited history
        for ( int i = 0; i < getNavigationHistory().size(); i++ )
            clearCacheForSingleFile(getNavigationHistory().getItemAt(i).getUrl());

        // Clear the history
        getNavigationHistory().clear();

        getResourceClient().destroy();
        getUIClient().destroy();

        resourceClient = null;
        uiClient = null;

        super.onDestroy();
    }

    public CornResourceClient getResourceClient() {
        return resourceClient;
    }

    public CornUIClient getUIClient() {
        return uiClient;
    }

    public static CrunchyWalkView fromXWalkView(XWalkView view) {
        return (CrunchyWalkView)view;
    }

    @Override
    public void load(String url, String content) {
        if(url == null) { super.load(null, content); return; }
        Matcher urlRegExMatcher     = CornResourceClient.urlRegEx   .matcher(url);
        Matcher urlSecRegExMatcher  = CornResourceClient.urlSecRegEx.matcher(url);
        String nUrl = url;
        if(urlRegExMatcher.matches()) nUrl = url;
        else if(urlSecRegExMatcher.matches()) nUrl = HTTP_PREFIX + url;
        else {
            switch(CornBrowser.getBrowserStorage().getSearchEngine()) {
                case Google:
                    nUrl = "https://google.com/search?q=%s";
                    break;
                case DuckDuckGo:
                    nUrl = "https://duckduckgo.com/?q=%s";
                    break;
                case Yahoo:
                    nUrl = "https://search.yahoo.com/search?q=%s&toggle=1&cop=mss&ei=UTF-8";
                    break;
                case Bing:
                    nUrl = "https://bing.com/search?q=%s";
                    break;
                case Ecosia:
                    nUrl = "https://ecosia.org/search?q=%s";
                    break;
                case Custom:
                    nUrl = CornBrowser.getBrowserStorage().getCustomSearchEngine();
                    if(!(nUrl.startsWith(HTTP_PREFIX) || nUrl.startsWith("https://")))
                        nUrl = HTTP_PREFIX + nUrl;
                    break;
                default: break;
            }

            nUrl = String.format(nUrl, URLEncode.encode(url));
        }
        // Check for this error and automatically fix it if present
        nUrl = ( nUrl.startsWith("http://http//") || nUrl.startsWith("http://https//") )
            ? nUrl.replace("http//", "").replace("https//", "") : nUrl;
        super.load(nUrl, content);
    }

    public void load(String url) {
        load(url, null);
    }

    public void loadWorkingUrl() {
        load(getResourceClient().currentWorkingUrl);
    }

    public void reload(boolean ignoreCache) {
        reload(ignoreCache ? RELOAD_IGNORE_CACHE : RELOAD_NORMAL);
    }

    public boolean canGoForward() {
        return getNavigationHistory().canGoForward();
    }

    public boolean canGoBack() {
        try {
            return getNavigationHistory().canGoBack();
        } catch(NullPointerException ex) {
            return false;
        }
    }

    public void goForward(int steps) {
        if(canGoForward())
            getNavigationHistory().navigate(XWalkNavigationHistory.Direction.FORWARD, steps);
    }

    public void goForward() {
        goForward(1);
    }

    public boolean goBack(int steps) {
        if(canGoBack())
            getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, steps);
        else return false;
        return true;
    }

    public boolean goBack() {
        return goBack(1);
    }

    @Override
    public void setBackgroundColor(int color) {
        if(lastBgColor != color) {
            lastBgColor = color;
            super.setBackgroundColor(lastBgColor);
        }
    }
    // Handle color modes

    public void drawWithColorMode() {
        Logging.logd("Applying web render color mode...");
        RenderColorMode.ColorMode cm = CornBrowser.getBrowserStorage().getColorMode();

        Logging.logd("Found color mode: " + cm.mode);

        String scriptName = "resetMode";

        switch(cm.mode) {
            case RenderColorMode.ColorMode.NORMAL:
                Logging.logd("Applying normal color mode");
                setBackgroundColor(Color.WHITE);
                break;
            case RenderColorMode.ColorMode.DARK:
                Logging.logd("Applying dark mode");
                scriptName = "darkMode";
                setBackgroundColor(Color.BLACK);
                break;
            case RenderColorMode.ColorMode.NEGATIVE:
                Logging.logd("Applying negative mode");
                scriptName = "negativeMode";
                setBackgroundColor(Color.BLACK);
                break;
            case RenderColorMode.ColorMode.INVERT:
                Logging.logd("Applying inverted mode");
                scriptName = "invertMode";
                setBackgroundColor(Color.WHITE);
                break;
            case RenderColorMode.ColorMode.GREYSCALE:
                Logging.logd("Applying greyscale");
                scriptName = "greyscaleMode";
                setBackgroundColor(Color.WHITE);
                break;
            default:
                Logging.logd("Warning: Unknown color mode " + cm.mode + ".");
                setBackgroundColor(Color.WHITE);
                break;
        }

        Logging.logd("Applying...");
        CornHandler.sendRawJSRequest(this,
                AssetHelper.getAssetString("appScripts/colormodes/" + scriptName + ".js",
                        getContext()));
    }

    public String resolveRelativeUrl(final String url) {
        String nUrl;
        if (url.startsWith("//"))
            nUrl = getUrl().substring(0, getUrl().lastIndexOf("://") + 1) + url;
        else if(url.startsWith("/"))
            nUrl = getUrlDomain() + url;
        else {
            Matcher urlRegExMatcher =
                    CornResourceClient.urlRegEx.matcher(url);
            Matcher urlSecRegExMatcher =
                    CornResourceClient.urlSecRegEx.matcher(url);
            if (urlRegExMatcher.matches()) nUrl = url;
            else if (urlSecRegExMatcher.matches())
                nUrl = HTTP_PREFIX + url;
            else {
                nUrl = getUrl().substring(0, getUrl().lastIndexOf("/")) + url;
            }
        }
        return nUrl;
    }

    public String resolveRelativeUrlXFriendly(final String url) {
        String nUrl;
        if (url.startsWith("//"))
            nUrl = getUrlAlt().substring(0, getUrlAlt().lastIndexOf("://") + 1) + url;
        else if(url.startsWith("/"))
            nUrl = getUrlDomainAlt() + url;
        else {
            Matcher urlRegExMatcher =
                    CornResourceClient.urlRegEx.matcher(url);
            Matcher urlSecRegExMatcher =
                    CornResourceClient.urlSecRegEx.matcher(url);
            if (urlRegExMatcher.matches()) nUrl = url;
            else if (urlSecRegExMatcher.matches())
                nUrl = HTTP_PREFIX + url;
            else {
                nUrl = getUrlAlt().substring(0, getUrlAlt().lastIndexOf("/")) + url;
            }
        }
        return nUrl;
    }

    private static final class LongPressDialogLinkItems {
        public static final int
                openNewTab  = 0,
                copyUrl     = 1,
                copyText    = 2,
                shareLink   = 3
                        ;
    }

    private static final class LongPressDialogImageItems {
        public static final int
                openNewTab  = 0,
                copyUrl     = 1,
                saveImage   = 2,
                shareImage  = 3
                        ;
    }

    public void onLongPressImage(final String url, final String title, AlertDialog.Builder b) {
        final Handler handler = new Handler();
        final Runnable tr = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContextAlt(),
                        getContextAlt().getString(
                                R.string.webrender_image_saved_message
                        ), Toast.LENGTH_LONG).show();
            }
        };
        final Runnable saveImageRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    FileUtils.saveBitmapAsImage(
                            BitmapFactory.decodeStream(DownloadUtils.getInputStreamForConnection
                                                    (resolveRelativeUrlXFriendly(url))),
                            Environment.getExternalStorageDirectory()
                                    .getAbsolutePath() + "/Pictures/",
                            title.replace(" ", (title.length() <= 1) ? "IMG" : "") + "_" +
                                    System.currentTimeMillis() + ".jpg");
                    handler.post(tr);
                } catch(Exception ex) {
                    StackTraceParser.logStackTrace(ex);
                }
            }
        };

        final String[] longPressDialogImage = new String[] {
                getContextAlt().getString(R.string.webrender_longpress_dialog_open_newtab),
                getContextAlt().getString(R.string.webrender_longpress_dialog_copy_url),
                getContextAlt().getString(R.string.webrender_longpress_dialog_save_image)/*,
                getContextAlt().getString(R.string.webrender_longpress_dialog_share_image)*/
        };
        b
                .setTitle(String.format(
                        getContextAlt().getString
                                (R.string.webrender_longpress_dialog_image_dblpnt),
                        title
                ))
                .setAdapter(XDListView.createLittle(getContextAlt(), longPressDialogImage),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which) {
                                    case LongPressDialogImageItems.openNewTab:
                                        CornBrowser.getTabSwitcher().addTab(resolveRelativeUrl(url),
                                                title);
                                        break;
                                    case LongPressDialogImageItems.copyUrl:
                                        ClipboardUtil.copyIntoClipboard(resolveRelativeUrl(url),
                                                getContextAlt());
                                        break;
                                    case LongPressDialogImageItems.saveImage:
                                        try {
                                            Thread thread = new Thread(saveImageRunnable);
                                            thread.start();
                                        } catch(Exception ex) {
                                            StackTraceParser.logStackTrace(ex);
                                        }
                                        break;
                                    case LongPressDialogImageItems.shareImage:
                                        break;
                                    default: break;
                                }
                                dialog.dismiss();
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isLongPressDialogAv = true;
                    }
                })
                .create().show();
    }

    public void onLongPressLink(final String url, final String title, AlertDialog.Builder b) {
        final String[] longPressDialogText = new String[] {
                getContextAlt().getString(R.string.webrender_longpress_dialog_open_newtab),
                getContextAlt().getString(R.string.webrender_longpress_dialog_copy_url),
                getContextAlt().getString(R.string.webrender_longpress_dialog_copy_text),
                getContextAlt().getString(R.string.webrender_longpress_dialog_share_link)
        };
        b
                .setTitle(
                        String.format(getContextAlt().getString(
                                        R.string.webrender_longpress_dialog_link_dblpnt
                                ), title))
                .setAdapter(XDListView.createLittle(getContextAlt(), longPressDialogText),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which) {
                                    case LongPressDialogLinkItems.openNewTab:
                                        CornBrowser.getTabSwitcher().addTab(resolveRelativeUrl(url),
                                                title);
                                        break;
                                    case LongPressDialogLinkItems.copyUrl:
                                        ClipboardUtil.copyIntoClipboard(resolveRelativeUrl(url),
                                                getContextAlt());
                                        break;
                                    case LongPressDialogLinkItems.copyText:
                                        ClipboardUtil.copyIntoClipboard(title, getContextAlt());
                                        break;
                                    case LongPressDialogLinkItems.shareLink:
                                        ShareUtil.shareText(
                                                getContextAlt().getString(
                                                        R.string.webrender_longpress_dialog_share_link),
                                                resolveRelativeUrl(url), CornBrowser.getActivity());
                                        break;
                                    default: break;
                                }
                                dialog.dismiss();
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isLongPressDialogAv = true;
                    }
                })
                .create().show();
    }

    public void onLongPress(final String url, final String title, final boolean isImage) {
        if(!isLongPressDialogAv) return;
        isLongPressDialogAv = false;
        AlertDialog.Builder b = new AlertDialog.Builder(CornBrowser.getActivity(),
                R.style.AlertDialogBlueRipple);
        if(isImage) onLongPressImage(url, title, b);
        else        onLongPressLink (url, title, b);
    }

    public void registerNavigation() {
        if(isIncognito) return;
        historyUpdateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isHistoryBeingWritten)
                    Sleeper.sleep(10);
                updateIsHistoryBeingUpdated(true);
                browsingHistory.putValue(getUrlAlt(), getTitleAlt(), true, false);
                CornBrowser.getBrowserStorage()
                        .putString(BrowserStorage.BPrefKeys.historyPref,
                                browsingHistory.toString());
                updateIsHistoryBeingUpdated(false);
            }
        });
        historyUpdateThread.start();
    }

    public void eraseHistory() {
        try {
            // Try to wait for the thread to finish
            if (historyUpdateThread != null && historyUpdateThread.isAlive())
                historyUpdateThread.join();
        } catch(Exception ex) {
            // Ignore
        }
        browsingHistory.createNew();
    }

    private synchronized void updateIsHistoryBeingUpdated(boolean condition) {
        isHistoryBeingWritten = condition;
    }

    @Override
    public Bitmap getFavicon() {
        return favicon;
    }

    public String getUrlAlt() {
        return currentUrl;
    }

    public String getTitleAlt() {
        return currentTitle;
    }

    public String getUrlDomain() {
        return StringManipulation.getDomainFromUrl(getUrl());
    }

    public String getUrlDomainAlt() {
        return StringManipulation.getDomainFromUrl(getUrlAlt());
    }

    public Context getContextAlt() {
        return CornBrowser.getContext();
    }

}