package io.xdevs23.cornowser.browser;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.net.DownloadUtils;
import org.xdevs23.rey.material.widget.ProgressView;
import org.xdevs23.threads.Sleeper;
import org.xdevs23.ui.utils.BarColors;
import org.xdevs23.ui.utils.DpUtil;
import org.xdevs23.ui.view.listview.XDListView;
import org.xdevs23.ui.widget.TastyOverflowMenu;
import org.xwalk.core.XWalkPreferences;

import io.xdevs23.cornowser.browser.activity.BgLoadActivity;
import io.xdevs23.cornowser.browser.activity.SettingsActivity;
import io.xdevs23.cornowser.browser.browser.BrowserStorage;
import io.xdevs23.cornowser.browser.browser.modules.ColorUtil;
import io.xdevs23.cornowser.browser.browser.modules.WebThemeHelper;
import io.xdevs23.cornowser.browser.browser.modules.tabs.BasicTabSwitcher;
import io.xdevs23.cornowser.browser.browser.modules.tabs.BlueListedTabSwitcher;
import io.xdevs23.cornowser.browser.browser.modules.tabs.TabStorage;
import io.xdevs23.cornowser.browser.browser.modules.tabs.TabSwitcherOpenButton;
import io.xdevs23.cornowser.browser.browser.modules.tabs.TabSwitcherWrapper;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxAnimations;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxControl;
import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;
import io.xdevs23.cornowser.browser.updater.UpdateActivity;
import io.xdevs23.cornowser.browser.updater.UpdaterStorage;

public class CornBrowser extends XquidCompatActivity {

    public static CrunchyWalkView publicWebRender = null;

    public static RelativeLayout
            omnibox,
            publicWebRenderLayout,
            omniboxControls,
            omniboxTinyItemsLayout
            ;

    public static EditText browserInputBar;

    public static ImageButton
            goForwardImgBtn
            ;

    public static TabSwitcherOpenButton openTabswitcherImgBtn;

    public static String readyToLoadUrl = "";

    public static boolean isBgBoot = false;

    private static final int
            PERMISSION_REQUEST_CODE = 1;

    private static PullRefreshLayout omniPtrLayout;

    private static View staticView;
    private static Context staticContext;
    private static Activity staticActivity;
    private static Window staticWindow;

    @SuppressWarnings("FieldCanBeLocal")
    private static RelativeLayout staticRootView;

    private static ProgressView webProgressBar;

    private static BrowserStorage browserStorage;

    private static TabSwitcherWrapper tabSwitcher;

    private static Handler mHandler;

    private static String newVersionAv = "";

    @SuppressWarnings("FieldCanBeLocal")
    private static String[] optionsMenuItems;

    private static boolean isBootstrapped = false;
    private static boolean isInitialized  = false;
    private static boolean isNewIntent    = false;

    private static final int
            aLeft   = RelativeLayout.ALIGN_PARENT_LEFT,
            aTop    = RelativeLayout.ALIGN_PARENT_TOP,
            aRight  = RelativeLayout.ALIGN_PARENT_RIGHT,
            aBottom = RelativeLayout.ALIGN_PARENT_BOTTOM;


    private AlertDialog optionsMenuDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bootstrap();
    }

    /**
     * Whole initialization is done here
     */
    protected void bootstrap() {
        if(isBgBoot) moveTaskToBack(true);
        if(!isBootstrapped
                && !isInitialized
                && staticContext == null
                && (!isNewIntent)) {
            if(!isBgBoot) checkMallowPermissions();

            XWalkPreferences.setValue(XWalkPreferences.ANIMATABLE_XWALK_VIEW, true);
            XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);
            XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);

            setContentView(R.layout.main_corn);

            initAll();

            isBootstrapped = true;
        } else if(isNewIntent) {
            handleStartupWebLoad();
            fastReloadComponents();
            return;
        } else if(!isInitialized) {
            initAll();
            getTabSwitcher().addTab(browserStorage.getUserHomePage());
            return;
        }

        handleStartupWebLoad();

        if( (!isBgBoot) && (!checkUpdate.isAlive()) || (!isNewIntent) )
            checkUpdate.start();

        if(!isBgBoot) fastReloadComponents();
    }

    /**
     * Permission check for Marshmallow
     */
    public void checkMallowPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED && (!isBgBoot)) {

            Toast.makeText(getApplicationContext(),
                    getString(R.string.permission_hint_write_storage), Toast.LENGTH_LONG).show();


            if(!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) ||

                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED)

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * For start-up web loading
     */
    protected void handleStartupWebLoad() {
        if (getIntent().getData() != null && (!getIntent().getDataString().isEmpty()))
            getTabSwitcher().addTab(getIntent().getDataString());

        else if (getIntent().getStringExtra(BgLoadActivity.bgLoadKey) != null &&
                (!getIntent().getStringExtra(BgLoadActivity.bgLoadKey).isEmpty()))
            getTabSwitcher().addTab(getIntent().getStringExtra(BgLoadActivity.bgLoadKey));

        else if (readyToLoadUrl.isEmpty())
            getTabSwitcher().addTab(browserStorage.getUserHomePage(), "");

        else {
            getTabSwitcher().addTab(readyToLoadUrl, null);
            readyToLoadUrl = "";
        }
    }

    /**
     * Initialize everything
     */
    public void initAll() {
        Logging.logd("Initialization started.");
        preInit();
        init();
        isInitialized = true;
    }

    /**
     * Initialize some stuff before getting started
     */
    public void preInit() {
        mHandler = new Handler();
        initStaticFields();
        BarColors.enableBarColoring(staticWindow, R.color.colorPrimaryDark);
        staticView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                handleFullscreenMode();
            }
        });
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if (browserStorage.getIsFullscreenEnabled() &&
                                (visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            handleFullscreenMode();
                        }
                    }
                });
    }

    // Pre init start

    /**
     * Initialize static fields
     */
    public void initStaticFields() {
        Logging.logd("Initializing static fields");
        staticActivity  = this;
        staticContext   = this.getApplicationContext();
        staticView      = findViewById(R.id.corn_root_view);
        staticWindow    = this.getWindow();
        staticRootView  = (RelativeLayout)staticView;

        // layout must be initialized before initializing the tab switcher
        if(!isNewIntent) publicWebRenderLayout = (RelativeLayout) findViewById(R.id.webrender_layout);

        if(!isNewIntent) initBrowsing();
    }

    // Pre init end

    /**
     * Main initialization
     */
    public void init() {
        Logging.logd("Initializing...");
        initOptionsMenu();
        initOmnibox();
        initWebXWalkEngine();
    }

    // Init start

    /**
     * Initialize web rendering stuff
     */
    public void initWebXWalkEngine() {
        Logging.logd("    Web engine and necessary components");

        // web render layout is initialized while initializing static fields

        initOmniboxPosition();
    }

    /**
     * Initialize essential browsing components
     */
    public static void initBrowsing() {
        if(browserStorage == null && tabSwitcher == null) {
            browserStorage = new BrowserStorage(getContext());

            tabSwitcher = new TabSwitcherWrapper(
                    new BlueListedTabSwitcher(getContext(), staticRootView)
                            .setTabStorage(new TabStorage()));
        }
    }

    /**
     * Initialize the omnibox
     */
    public void initOmnibox() {
        Logging.logd("    Omnibox");
        omnibox                 = (RelativeLayout)      findViewById(R.id.omnibox_layout);
        omniPtrLayout           = (PullRefreshLayout)   findViewById(R.id.omnibox_refresh_ptr);
        browserInputBar         = (EditText)            findViewById(R.id.omnibox_input_bar);
        omniboxTinyItemsLayout  = (RelativeLayout)      findViewById(R.id.omnibox_tiny_items_layout);
        webProgressBar          = (ProgressView)        findViewById(R.id.omnibox_progressbar);

        omniPtrLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        omniPtrLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWebEngine().reload(true);
            }
        });

        initOmniboxControls();
    }

    /**
     * Initialize omnibox controls
     */
    public void initOmniboxControls() {
        Logging.logd("      Controls");
        omniboxControls         = (RelativeLayout)        findViewById(R.id.omnibox_controls);
        openTabswitcherImgBtn   = (TabSwitcherOpenButton) findViewById(R.id.omnibox_control_open_tabswitcher);
        goForwardImgBtn         = (ImageButton)           findViewById(R.id.omnibox_control_forward);

        TastyOverflowMenu overflowMenuLayout;
        overflowMenuLayout      = (TastyOverflowMenu)   findViewById(R.id.omnibox_control_overflowmenu);


        browserInputBar.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    publicWebRender.load(((EditText) v).getText().toString(), null);
                }
                return false;
            }
        });

        overflowMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsMenu();
            }
        });

        openTabswitcherImgBtn.init(getContext());
        openTabswitcherImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTabSwitcher().switchSwitcher();
            }
        });

        goForwardImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getWebEngine().currentProgress != 100) getWebEngine().stopLoading();
                else getWebEngine().goForward();
            }
        });
        goForwardImgBtn.setVisibility(View.INVISIBLE);
        goForwardImgBtn.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

    }

    /**
     * Properly init omnibox position
     */
    public static void initOmniboxPosition() {
        Logging.logd("    Omnibox position");

        RelativeLayout.LayoutParams omniparams = new RelativeLayout.LayoutParams(
                omnibox.getLayoutParams().width,
                omnibox.getLayoutParams().height
        );
        RelativeLayout.LayoutParams webrparams = new RelativeLayout.LayoutParams(
                publicWebRenderLayout.getLayoutParams().width,
                publicWebRenderLayout.getLayoutParams().height
        );
        RelativeLayout.LayoutParams otilparams = new RelativeLayout.LayoutParams(
                omniboxTinyItemsLayout.getLayoutParams().width,
                omniboxTinyItemsLayout.getLayoutParams().height
        );

        omniparams.addRule(aLeft);
        omniparams.addRule(aRight);
        webrparams.addRule(aLeft);
        webrparams.addRule(aRight);
        otilparams.addRule(aLeft);
        otilparams.addRule(aRight);

        if(browserStorage.getOmniboxPosition()) {
            omniparams.addRule(aBottom);
            webrparams.addRule(aTop);
            otilparams.addRule(aTop);
        } else {
            omniparams.addRule(aTop);
            webrparams.addRule(aBottom);
            otilparams.addRule(aBottom);
        }

        omnibox.setLayoutParams(omniparams);
        publicWebRenderLayout.setLayoutParams(webrparams);
        omniboxTinyItemsLayout.setLayoutParams(otilparams);

        ((RelativeLayout.LayoutParams)browserInputBar.getLayoutParams()).setMargins(
                0,
                0,
                DpUtil.dp2px(getContext(), 3 * 32 + 2),
                0
        );

        getActivity().findViewById(R.id.omnibox_separator)
                .setTranslationY(OmniboxControl.isBottom() ? -webProgressBar.getHeight() : 0);

        // This is for proper refresh feature when omnibox is at bottom
        omniPtrLayout.setRotation(OmniboxControl.isBottom() ? 180f : 0f);
        getActivity().findViewById(R.id.omnibox_layout_inner)
                .setRotation(OmniboxControl.isBottom() ? 180f : 0);

        omnibox.bringToFront();
        omniboxTinyItemsLayout.bringToFront();
        omniboxControls.bringToFront();
        getActivity().findViewById(R.id.omnibox_separator).bringToFront();

        resetOmniPositionState();
    }

    /**
     * Init reloadable stuff
     */
    public void reloadComponents() {
        initOmniboxPosition();
    }

    /**
     * Show omnibox by resetting the translation values
     */
    public static void resetOmniPositionState() {
        if(!OmniboxAnimations.isBottom())
            publicWebRenderLayout.setTranslationY(omnibox.getHeight());
        else publicWebRenderLayout.setTranslationY(0);
        omnibox.setTranslationY(0);
        publicWebRenderLayout.setScrollY(0);
        publicWebRenderLayout.setScrollX(0);
    }

    /**
     * Show omnibox with animation
     * @param animate Animate it?
     */
    public static void resetOmniPositionState(boolean animate) {
        if(!animate) { resetOmniPositionState(); return; }
        OmniboxAnimations.resetOmni();
    }

    /**
     * Set the text inside the omnibox
     * @param url URL
     */
    public static void applyOnlyInsideOmniText(String url) {
        try {
            String eurl = url;
            eurl = eurl.replaceFirst("^([^ ]*)://", "");
            if(eurl.substring(eurl.length() - 2, eurl.length() - 1).equals("/"))
                eurl = eurl.substring(0, eurl.length() - 2);
            browserInputBar.setText(eurl);
            if(url.startsWith("https://")) {
                getActivity().findViewById(R.id.omnibox_separator)
                    .setBackgroundColor(ColorUtil.getColor(R.color.light_green_A700));
            } else getActivity().findViewById(R.id.omnibox_separator)
                    .setBackgroundColor(ColorUtil.getColor(R.color.dark_semi_more_transparent));
        } catch(Exception ex) {
            Logging.logd("Warning: Didn't succeed while applying inside omni text.");
        }

    }

    /**
     * Set the text of the address bar (and apply it in tab switcher)
     * @param url URL to set as text
     */
    public static void applyInsideOmniText(String url) {
        if(browserInputBar.hasFocus()) return;
        getTabSwitcher().changeCurrentTab(url, publicWebRender.getTitle());
        applyOnlyInsideOmniText(url);
    }

    /**
     * Items of options menu
     */
    private static class optMenuItems {
        public static final int
                UPDATER         = 0,
                SETTINGS        = 1,
                SHARE_PAGE      = 2
                        ;
    }

    /**
     * Initialize the options menu
     */
    public void initOptionsMenu() {
        optionsMenuItems = new String[] {
                getString(R.string.cornmenu_item_updater),
                getString(R.string.cornmenu_item_settings),
                getString(R.string.optmenu_share)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogBlueRipple);
        builder.setAdapter(XDListView.createLittle(getContext(), optionsMenuItems), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case optMenuItems.UPDATER:
                        startActivity(new Intent(getContext(), UpdateActivity.class));
                        break;
                    case optMenuItems.SETTINGS:
                        startActivity(new Intent(getContext(), SettingsActivity.class));
                        break;
                    case optMenuItems.SHARE_PAGE:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra
                                (Intent.EXTRA_TEXT, publicWebRender.getUrl());
                        startActivity(Intent.createChooser(shareIntent,
                                getString(R.string.optmenu_share)));
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
        optionsMenuDialog = builder.create();
    }

    // Init end

    /**
     * Open the options menu
     */
    @Override
    public void openOptionsMenu() {
        optionsMenuDialog.show();
    }

    /**
     * Hide/show go forward button
     */
    public static void handleGoForwardControlVisibility() {
        if(publicWebRender.currentProgress != 100) {
            goForwardImgBtn.setBackgroundResource(R.drawable.main_cross_rot_icon);
            goForwardImgBtn.setVisibility(View.VISIBLE);
        } else {
            if(publicWebRender.canGoForward()) {
                goForwardImgBtn.setBackgroundResource(R.drawable.ic_arrow_forward_black_48dp);
                goForwardImgBtn.setVisibility(View.VISIBLE);
            } else goForwardImgBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @return Static root view
     */
    public static View getView() {
        return staticView;
    }

    /**
     * @return Static context
     */
    public static Context getContext() {
        return staticContext;
    }

    /**
     * @return Static activity
     */
    public static Activity getActivity() {
        return staticActivity;
    }

    /**
     * @return Static window
     */
    public static Window getStaticWindow() {
        return staticWindow;
    }

    /**
     * @return The actual browser storage
     */
    public static BrowserStorage getBrowserStorage() {
        return browserStorage;
    }

    /**
     * @return Our delicious and crunchy web engine :D
     */
    public static CrunchyWalkView getWebEngine() {
        return publicWebRender;
    }

    /**
     * @return The requested string
     */
    public static String getRStr(int resId) {
        return staticContext.getString(resId);
    }

    /**
     * @return Progress bar (shows actual loading progress)
     */
    public static ProgressView getWebProgressBar() {
        return webProgressBar;
    }

    /**
     * @return Tab switcher
     */
    public static BasicTabSwitcher getTabSwitcher() {
        return tabSwitcher.getTabSwitcher();
    }

    /**
     * @return Pull to refresh layout
     */
    public static PullRefreshLayout getOmniPtrLayout() {
        return omniPtrLayout;
    }

    /**
     * Reset the color of status bar and navigation bar
     */
    public static void resetBarColor() {
        if (OmniboxControl.isTop()) {
            BarColors.updateBarsColor(getStaticWindow(), R.color.colorPrimaryDark, false, false, true);
            BarColors.updateBarsColor(getStaticWindow(), R.color.black, false, true, false);
        } else {
            BarColors.updateBarsColor(getStaticWindow(), R.color.black, false, true, false);
            BarColors.updateBarsColor(getStaticWindow(), R.color.black, false, false, true);
        }
    }

    /**
     * Quickly reload some components (e. g. after resume)
     */
    private void fastReloadComponents() {
        onPauseWebRender(true);
        onResumeWebRender();
    }

    /**
     * Pause the web rendering engine (reduces memory leaks etc.)
     */
    protected void onPauseWebRender() {
        onPauseWebRender(false);
    }

    /**
     * Pause the web rendering engine
     * @param fastPause Choose if it should be a fast pause (no difference in speed)
     */
    protected void onPauseWebRender(boolean fastPause) {
        if (publicWebRender != null) {
            publicWebRender.pauseTimers();
            publicWebRender.onHide();

            if(!fastPause) readyToLoadUrl = "";
        }
    }

    /**
     * Resume the web rendering engine after being paused (or reload)
     */
    protected void onResumeWebRender() {
        if (publicWebRender != null) {

            publicWebRender.resumeTimers();
            publicWebRender.onShow();

            resetBarColor();
            WebThemeHelper.tintNow(publicWebRender);

            if(!browserStorage.getOmniColoringEnabled())
                WebThemeHelper.resetWebThemeColor(omnibox);

            publicWebRender.drawWithColorMode();

            if(readyToLoadUrl.length() > 0) {
                publicWebRender.load(readyToLoadUrl, null);
                readyToLoadUrl = "";
            }

            if(publicWebRender.getTitle().isEmpty() || publicWebRender.getUrl().isEmpty())
                publicWebRender.loadWorkingUrl();
        }
    }

    @Override
    protected void onPause() {
        Logging.logd("Activity paused.");
        super.onPause();
        onPauseWebRender();
    }

    @Override
    protected void onResume() {
        Logging.logd("Activity resumed.");
        super.onResume();
        if(this.getWindow().isActive() && isBgBoot) {
            isBgBoot = false;
            checkUpdate.start();
            fastReloadComponents();
        } else onResumeWebRender();
        reloadComponents();

    }

    @Override
    protected void onDestroy() {
        Logging.logd("Activity destroyed.");
        super.onDestroy();
        if (publicWebRender != null)
            publicWebRender.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logging.logd("Activity result received.");
        if (publicWebRender != null)
            publicWebRender.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Logging.logd("New intent!");
        getIntent().setData(intent.getData());
        if(intent.getData() == null || intent.getDataString().isEmpty()) return;
        isNewIntent = true;
        bootstrap();
    }

    @Override
    public void onBackPressed() {
        if(browserInputBar != null && !browserInputBar.hasFocus()) {
            browserInputBar.clearFocus();
            applyInsideOmniText(publicWebRender.getUrl());
            return;
        }
        if(!publicWebRender.goBack()) endApplication();
    }

    /**
     * Handle fullscreen mode
     */
    public void handleFullscreenMode() {
        if (browserStorage.getIsFullscreenEnabled()) {
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        );
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
        } else {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        handleFullscreenMode();
    }

    /* Updater section */

    /**
     *  This Thread checks for Updates in the Background
     */
    private Thread checkUpdate = new Thread() {
        public void run() {
            try {
                Sleeper.sleep(1000); // Give the browser a second to get air xD
                String newVer = DownloadUtils.downloadString(UpdaterStorage.URL_VERSION_CODE);
                newVersionAv  = DownloadUtils.downloadString(UpdaterStorage.URL_VERSION_NAME);
                if(Integer.parseInt(newVer) > getContext().getPackageManager().getPackageInfo(
                        getApplicationContext().getPackageName(), 0
                ).versionCode) mHandler.post(showUpdate);
            } catch (Exception e) { /* Do nothing */ }
        }

    };

    /**
     * Show update dialog
     */
    private Runnable showUpdate = new Runnable() {
        public void run() {
            new AlertDialog.Builder(CornBrowser.this)
                    .setTitle(getContext().getString(R.string.update_available_title))
                    .setMessage(String.format(getContext().getString(R.string.update_available_message), newVersionAv))
                    .setPositiveButton(getContext().getString(R.string.answer_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            UpdateActivity.startUpdateImmediately(staticActivity, UpdaterStorage.URL_APK);
                        }
                    })
                    .setNegativeButton(getContext().getString(R.string.answer_no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    };

}
