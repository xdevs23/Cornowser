package io.xdevs23.cornowser.browser;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

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

    private AlertDialog optionsMenuDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bootstrap();
    }

    protected void bootstrap() {
        if(isBgBoot) moveTaskToBack(true);
        if(!isBootstrapped
                && !isInitialized
                && staticContext == null
                && (!isNewIntent)) {
            setContentView(R.layout.main_corn);

            initAll();

            isBootstrapped = true;
        } else {
            if(!isInitialized) {
                Logging.logd("Oops... Seems like we ran into an irregular situaion. Let me initialize the stuff");
                initAll();
                getTabSwitcher().addTab(browserStorage.getUserHomePage());
                return;
            }
        }

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

        if( (!isBgBoot) && (!checkUpdate.isAlive()) || (!isNewIntent) ) checkUpdate.start();

        if(!isBgBoot) fastReloadComponents();
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
                getWebEngine().goForward();
            }
        });
        goForwardImgBtn.setVisibility(View.INVISIBLE);

    }

    /**
     * Properly init omnibox position
     */
    public void initOmniboxPosition() {
        Logging.logd("    Omnibox position");

        int
                aLeft   = RelativeLayout.ALIGN_PARENT_LEFT,
                aTop    = RelativeLayout.ALIGN_PARENT_TOP,
                aRight  = RelativeLayout.ALIGN_PARENT_RIGHT,
                aBottom = RelativeLayout.ALIGN_PARENT_BOTTOM;

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
                DpUtil.dp2px(getContext(),
                        3 * 24 + 2),
                0
        );

        omniboxTinyItemsLayout.findViewById(R.id.omnibox_separator)
            .setTranslationY(browserStorage.getOmniboxPosition() ? -DpUtil.dp2px(getContext(), 3) : 0);

        omnibox.bringToFront();
        omniboxTinyItemsLayout.bringToFront();
        omniboxControls.bringToFront();

        resetOmniPositionState();
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

    public static void applyOnlyInsideOmniText(String url) {
        try {
            browserInputBar.setText(url
                    .replaceFirst("^(.*)://", ""));
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
     * Initialize the options menu
     */
    public void initOptionsMenu() {
        optionsMenuItems = new String[] {
                getString(R.string.cornmenu_item_updater),
                getString(R.string.cornmenu_item_settings)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogBlueRipple);
        ListView lv = new ListView(getContext());
        lv.setAdapter(XDListView.createLittle(getContext(), optionsMenuItems));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int which, long id) {
                switch (which) {
                    case optMenuItems.UPDATER:
                        startActivity(new Intent(getContext(), UpdateActivity.class));
                        break;
                    case optMenuItems.SETTINGS:
                        startActivity(new Intent(getContext(), SettingsActivity.class));
                        break;
                    default:
                        break;
                }
                optionsMenuDialog.hide();
            }
        });
        lv.setBackgroundColor(ColorUtil.getColor(R.color.grey_50));
        builder.setView(lv);
        optionsMenuDialog = builder.create();
    }

    // Init end

    /**
     * Items of options menu
     */
    private static class optMenuItems {
        public static final int
            UPDATER         = 0,
            SETTINGS        = 1
                    ;
    }

    /**
     * Open the options menu
     */
    @Override
    public void openOptionsMenu() {
        optionsMenuDialog.show();
    }

    /**
     * Hide/show go forward button
     * @param visible True for showing, false for hiding
     */
    public static void toggleGoForwardControlVisibility(boolean visible) {
        if(visible && goForwardImgBtn.getVisibility() == View.INVISIBLE)
            goForwardImgBtn.setVisibility(View.VISIBLE);
        else if(goForwardImgBtn.getVisibility() == View.VISIBLE)
            goForwardImgBtn.setVisibility(View.INVISIBLE);
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

    private void fastReloadComponents() {
        onPauseWebRender(true);
        onResumeWebRender();
    }

    protected void onPauseWebRender() {
        onPauseWebRender(false);
    }

    protected void onPauseWebRender(boolean fastPause) {
        if (publicWebRender != null) {
            publicWebRender.pauseTimers();
            publicWebRender.onHide();

            if(!fastPause) readyToLoadUrl = "";
        }
    }

    protected void onResumeWebRender() {
        if (publicWebRender != null) {
            initOmniboxPosition();

            publicWebRender.resumeTimers();
            publicWebRender.onShow();

            resetBarColor();
            WebThemeHelper.tintNow(publicWebRender);

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
        super.onNewIntent(intent);
        isNewIntent = true;
        bootstrap();
    }

    @Override
    public void onBackPressed() {
        if(!publicWebRender.goBack()) endApplication();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
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
            } else if(Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
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
