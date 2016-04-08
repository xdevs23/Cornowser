package io.xdevs23.cornowser.browser.browser.xwalk;


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
import android.widget.Toast;

import org.xdevs23.android.content.ClipboardUtil;
import org.xdevs23.android.content.res.AssetHelper;
import org.xdevs23.android.content.share.ShareUtil;
import org.xdevs23.annotation.DontUse;
import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;
import org.xdevs23.file.FileUtils;
import org.xdevs23.general.StringManipulation;
import org.xdevs23.general.URLEncode;
import org.xdevs23.net.DownloadUtils;
import org.xdevs23.ui.view.listview.XDListView;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkView;

import java.lang.reflect.Field;
import java.util.regex.Matcher;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.CornHandler;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxAnimations;
import io.xdevs23.cornowser.browser.browser.modules.ui.RenderColorMode;

/**
 * A delicious and crunchy XWalkView with awesome features
 */
public class CrunchyWalkView extends XWalkView {

    public static String userAgent =
            "Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + ") " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Cornowser/%s Chrome/51.0.2675.1 Mobile Safari/537.36";

    public int currentProgress = 0;

    protected Bitmap favicon;

    protected String currentUrl;

    protected boolean isLongPressDialogAv = true;

    private CornResourceClient resourceClient;
    private CornUIClient       uiClient;

    private int lastBgColor = 0;

    private void init() {
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

        setOnTouchListener(OmniboxAnimations.mainOnTouchListener);

        drawWithColorMode();

        Logging.logd("      Done!");
    }

    /* Don't use this! */
    @DontUse
    public CrunchyWalkView(Context context) {
        super(context);
        init();
    }

    public CrunchyWalkView(Context context, Activity activity) {
        super(context, activity);
        init();
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
        if(getResourceClient().checkIntentableUrl(
                (url.startsWith("intent") || url.startsWith("market"))
                        &&   url.contains("//")
                        && (!url.contains(":" )) ?
                    url.replace("//", "://") : url)
                ) return;
        Matcher urlRegExMatcher     = CornResourceClient.urlRegEx   .matcher(url);
        Matcher urlSecRegExMatcher  = CornResourceClient.urlSecRegEx.matcher(url);
        String nUrl = url;
        if(urlRegExMatcher.matches()) nUrl = url;
        else if(urlSecRegExMatcher.matches()) nUrl = "http://" + url;
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
                default: break;
            }

            nUrl = String.format(nUrl, URLEncode.encode(url));
        }
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

    public void evaluateJavascript(String script) {
        evaluateJavascript(script, null);
    }

    public void loadContent(String content) {
        load(null, content);
    }

    public boolean canGoForward() {
        return getNavigationHistory().canGoForward();
    }

    public boolean canGoBack() {
        return getNavigationHistory().canGoBack();
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

    public CrunchyWalkView immediatelyLoadUrl(String url) {
        load(url);
        return this;
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
        String nUrl = url;
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
                nUrl = "http://" + url;
            else {
                nUrl = getUrl().substring(0, getUrl().lastIndexOf("/")) + url;
            }
        }
        return nUrl;
    }

    public String resolveRelativeUrlXFriendly(final String url) {
        String nUrl = url;
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
                nUrl = "http://" + url;
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

    public void onLongPress(final String url, final String title, final boolean isImage) {
        if(!isLongPressDialogAv) return;
        isLongPressDialogAv = false;
        AlertDialog.Builder b = new AlertDialog.Builder(CornBrowser.getActivity(),
                R.style.AlertDialogBlueRipple);
        if(!isImage) {
            final String[] longPressDialogText = new String[] {
                    getContextAlt().getString(R.string.webrender_longpress_dialog_open_newtab),
                    getContextAlt().getString(R.string.webrender_longpress_dialog_copy_url),
                    getContextAlt().getString(R.string.webrender_longpress_dialog_copy_text),
                    getContextAlt().getString(R.string.webrender_longpress_dialog_share_link)
            };
            b
                    .setTitle(
                            String.format(getContextAlt().getString
                                    (
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
        } else {
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
                                                Runnable r = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            FileUtils.saveBitmapAsImage(
                                                                    BitmapFactory.decodeStream(
                                                                            DownloadUtils
                                                                            .getInputStreamForConnection
                                                                                (resolveRelativeUrlXFriendly(url))),
                                                                    Environment
                                                                            .getExternalStorageDirectory()
                                                                            .getAbsolutePath() + "/Pictures/",
                                                                    title.replace(" ",
                                                                            (title.length() <= 1) ?
                                                                                    "IMG" : "") + "_" +
                                                                            System.currentTimeMillis()
                                                                    + ".jpg");
                                                            handler.post(tr);
                                                        } catch(Exception ex) {
                                                            StackTraceParser.logStackTrace(ex);
                                                        }
                                                    }
                                                };
                                                Thread thread = new Thread(r);
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
    }

    @Override
    public Bitmap getFavicon() {
        return favicon;
    }

    public String getUrlAlt() {
        return currentUrl;
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

    /**
     * Get the private bridge
     * @return Bridge
     */
    protected Object getBridge() {
        Object bridge;
        try {
            Field field = getClass().getSuperclass().getDeclaredField("bridge");
            field.setAccessible(true);
            bridge = field.get(this);
            field.setAccessible(false);
        } catch(Exception ex) {
            StackTraceParser.logStackTrace(ex);
            bridge = null;
        }
        return bridge;
    }

}