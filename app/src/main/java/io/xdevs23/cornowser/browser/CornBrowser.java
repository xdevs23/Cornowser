package io.xdevs23.cornowser.browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.net.DownloadUtils;
import org.xdevs23.rey.material.widget.ProgressView;
import org.xdevs23.threads.Sleeper;
import org.xdevs23.ui.utils.BarColors;
import org.xdevs23.ui.view.listview.XDListView;
import org.xdevs23.ui.widget.TastyOverflowMenu;
import org.xwalk.core.XWalkNavigationHistory;

import io.xdevs23.cornowser.browser.activity.SettingsActivity;
import io.xdevs23.cornowser.browser.browser.BrowserStorage;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxAnimations;
import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;
import io.xdevs23.cornowser.browser.updater.UpdateActivity;
import io.xdevs23.cornowser.browser.updater.UpdaterStorage;

public class CornBrowser extends XquidCompatActivity {

    public static CrunchyWalkView publicWebRender = null;

    public static RelativeLayout
            omnibox                 = null,
            publicWebRenderLayout   = null,
            omniboxControls         = null,
            omniboxTinyItemsLayout  = null
            ;

    public  static EditText browserInputBar = null;

    public  static ImageButton goForwardImgBtn = null;

    public  static String readyToLoadUrl = "";

    private static View staticView;
    private static Context staticContext;
    private static Activity staticActivity;
    private static Window staticWindow;

    private static ProgressView webProgressBar;

    private static BrowserStorage browserStorage;

    private static TastyOverflowMenu overflowMenuLayout;

    private static Handler mHandler;

    private static String newVersionAv = "";

    private static String[] optionsMenuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_corn);

        initAll();

        if(getIntent().getData() != null && (!getIntent().getData().toString().isEmpty()))
            publicWebRender.load(getIntent().getData().toString());
        else if(readyToLoadUrl.isEmpty())
            publicWebRender.load(browserStorage.getUserHomePage(), null);
        else {
            publicWebRender.load(readyToLoadUrl, null);
            readyToLoadUrl = "";
        }

        checkUpdate.start();
    }

    /**
     * Initialize everything
     */
    public void initAll() {
        Logging.logd("Initialization started.");
        preInit();
        init();
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

        browserStorage = new BrowserStorage(getContext());
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
     * Initialize the XWalkView and its parent layout,
     * then add the crunchy web engine to the layout :D
     */
    public void initWebXWalkEngine() {
        Logging.logd("    Our crunchy web engine");
        publicWebRenderLayout   = (RelativeLayout)    findViewById(R.id.webrender_layout);
        publicWebRender         = new CrunchyWalkView(getContext(), getActivity());
        publicWebRenderLayout.addView(publicWebRender);

        initOmniboxPosition();

        publicWebRender.setOnTouchListener(OmniboxAnimations.mainOnTouchListener);
    }

    /**
     * Initialize the omnibox
     */
    public void initOmnibox() {
        Logging.logd("    Omnibox");
        omnibox                 = (RelativeLayout)      findViewById(R.id.omnibox_layout);
        browserInputBar         = (EditText)            findViewById(R.id.omnibox_input_bar);
        omniboxControls         = (RelativeLayout)      findViewById(R.id.omnibox_controls);
        omniboxTinyItemsLayout  = (RelativeLayout)      findViewById(R.id.omnibox_tiny_items_layout);
        webProgressBar          = (ProgressView)        findViewById(R.id.omnibox_progressbar);
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


        goForwardImgBtn = (ImageButton) findViewById(R.id.omnibox_control_forward);
        goForwardImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWebEngine().goForward();
            }
        });
        goForwardImgBtn.setVisibility(View.INVISIBLE);

    }

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
                omniboxControls.getWidth(),
                0
        );

        omniboxTinyItemsLayout.findViewById(R.id.omnibox_separator)
            .setTranslationY(browserStorage.getOmniboxPosition() ? -3 : 0);

        omnibox.bringToFront();
        omniboxTinyItemsLayout.bringToFront();
        omniboxControls.bringToFront();

        resetOmniPositionState();
    }

    public static void resetOmniPositionState() {
        if(!OmniboxAnimations.isBottom())
            publicWebRenderLayout.setTranslationY(omnibox.getHeight());
        else publicWebRenderLayout.setTranslationY(0);
        omnibox.setTranslationY(0);
        publicWebRenderLayout.setScrollY(0);
        publicWebRenderLayout.setScrollX(0);
    }

    public static void resetOmniPositionState(boolean animate) {
        if(!animate) { resetOmniPositionState(); return; }
        OmniboxAnimations.animateOmni(0);
    }


    public static void applyInsideOmniText(String url) {
        browserInputBar.setText(url
                .replaceFirst("^(.*)://", ""));
    }

    /**
     * Initialize the options menu
     */
    public void initOptionsMenu() {
        optionsMenuItems = new String[] {
                getString(R.string.cornmenu_item_updater),
                getString(R.string.cornmenu_item_settings)
        };
    }

    // Init end

    private static class optMenuItems {
        public static final int
            UPDATER         = 0,
            SETTINGS        = 1

                    ;
    }

    @Override
    public void openOptionsMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(XDListView.createLittle(getContext(), optionsMenuItems),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                    }
                });
        builder.create().show();
    }

    public static void toggleGoForwardControlVisibility(boolean visible) {
        if(visible) goForwardImgBtn.setVisibility(View.VISIBLE);
        else        goForwardImgBtn.setVisibility(View.INVISIBLE);
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
     * @return The tasty overflow menu
     */
    public static TastyOverflowMenu getOverflowMenu() {
        return overflowMenuLayout;
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
     * Reset the color of status bar
     */
    public static void resetBarColor() {
        BarColors.updateBarsColor(getStaticWindow(), R.color.colorPrimaryDark, false);
    }

    @Override
    protected void onPause() {
        Logging.logd("Activity paused.");
        super.onPause();
        if (publicWebRender != null) {
            publicWebRender.pauseTimers();
            publicWebRender.onHide();

            readyToLoadUrl = "";
        }
    }

    @Override
    protected void onResume() {
        Logging.logd("Activity resumed.");
        super.onResume();
        if (publicWebRender != null) {
            initOmniboxPosition();

            publicWebRender.resumeTimers();
            publicWebRender.onShow();

            if(readyToLoadUrl.length() > 0) {
                publicWebRender.load(readyToLoadUrl, null);
                readyToLoadUrl = "";
            }

            if(publicWebRender.getTitle().isEmpty() || publicWebRender.getUrl().isEmpty())
                publicWebRender.loadWorkingUrl();
        }
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
        if (publicWebRender != null)
            publicWebRender.onNewIntent(intent);

    }

    @Override
    public void onBackPressed() {
        if(getWebEngine().getNavigationHistory().canGoBack())
            getWebEngine().getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
        else endApplication();
    }

    /* Updater section */

    /* This Thread checks for Updates in the Background */
    private Thread checkUpdate = new Thread() {
        public void run() {
            try {
                Sleeper.sleep(1000); // Give the browser a second to get air xD
                String newVer = DownloadUtils.downloadString(UpdaterStorage.URL_VERSION_CODE);
                newVersionAv  = DownloadUtils.downloadString(UpdaterStorage.URL_VERSION_NAME);
                if(Integer.parseInt(newVer) > getContext().getPackageManager().getPackageInfo(
                        getApplicationContext().getPackageName(), 0
                ).versionCode)
                    mHandler.post(showUpdate);

            } catch (Exception e) { /* Do nothing */ }
        }

    };

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
