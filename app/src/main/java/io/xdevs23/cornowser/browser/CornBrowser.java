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
import android.widget.RelativeLayout;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.net.DownloadUtils;
import org.xdevs23.ui.utils.BarColors;
import org.xdevs23.ui.view.listview.XDListView;
import org.xdevs23.ui.widget.ProgressBarView;
import org.xdevs23.ui.widget.TastyOverflowMenu;

import io.xdevs23.cornowser.browser.activity.SettingsActivity;
import io.xdevs23.cornowser.browser.browser.BrowserStorage;
import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;
import io.xdevs23.cornowser.browser.updater.UpdateActivity;
import io.xdevs23.cornowser.browser.updater.UpdaterStorage;

public class CornBrowser extends XquidCompatActivity {

    public static CrunchyWalkView publicWebRender = null;

    public static RelativeLayout
            omnibox                 = null,
            publicWebRenderLayout   = null,
            omniboxControls         = null,
            browserInputBarLayout   = null
            ;

    public  static EditText browserInputBar = null;

    public  static String readyToLoadUrl = "";

    private static View staticView;
    private static Context staticContext;
    private static Activity staticActivity;
    private static Window staticWindow;

    private static ProgressBarView webProgressBar;

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

        checkUpdate.start();

        if(readyToLoadUrl.length() == 0)
            publicWebRender.load(browserStorage.getUserHomePage(), null);
        else if(readyToLoadUrl.length() > 0) {
            publicWebRender.load(readyToLoadUrl, null);
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
    }

    /**
     * Initialize some stuff before getting started
     */
    public void preInit() {
        mHandler = new Handler();
        initStaticFields();
        BarColors.enableBarColoring(staticWindow, R.color.colorPrimaryDark);
    }

    /**
     * Main initialization
     */
    public void init() {
        Logging.logd("Initializing...");
        initOmnibox();
        initWebXWalkEngine();
    }

    /**
     * Initialize static fields
     */
    public void initStaticFields() {
        Logging.logd("Initializing static fields");
        staticActivity  = this;
        staticContext   = this.getApplicationContext();
        staticView      = findViewById(R.id.corn_root_view);
        staticWindow    = this.getWindow();

        browserStorage = new BrowserStorage();

    }

    /**
     * Initialize the XWalkView and its parent layout,
     * then add the crunchy web engine to the layout :D
     */
    public void initWebXWalkEngine() {
        Logging.logd("    Our crunchy web engine");
        publicWebRenderLayout   = (RelativeLayout)  findViewById(R.id.webrender_layout);
        publicWebRender         = new CrunchyWalkView(getContext(), getActivity());
        publicWebRenderLayout.addView(publicWebRender);
    }

    /**
     * Initialize the omnibox
     */
    public void initOmnibox() {
        Logging.logd("    Omnibox");
        omnibox                 = (RelativeLayout)      findViewById(R.id.omnibox_layout);
        browserInputBarLayout   = (RelativeLayout)      findViewById(R.id.omnibox_input_bar_layout);
        browserInputBar         = (EditText)            findViewById(R.id.omnibox_input_bar);
        omniboxControls         = (RelativeLayout)      findViewById(R.id.omnibox_controls);
        webProgressBar          = (ProgressBarView)     findViewById(R.id.omnibox_progressbar);
        overflowMenuLayout      = (TastyOverflowMenu)   findViewById(R.id.omnibox_control_overflowmenu);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) browserInputBarLayout.getLayoutParams();

        params.width = browserInputBarLayout.getWidth() - omniboxControls.getWidth();

        browserInputBarLayout.setLayoutParams(params);

        browserInputBar.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    publicWebRender.load(((EditText) v).getText().toString(), null);
                } else Logging.logd(KeyEvent.keyCodeToString(keyCode));
                return false;
            }
        });

        overflowMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsMenu();
            }
        });

        webProgressBar.setOnCompletedAutoProgressFinish(true);
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

    private static class optMenuItems {
        public static final int
            UPDATER         = 0,
            SETTINGS        = 1,

            NONE            = Integer.MIN_VALUE

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
    public static ProgressBarView getWebProgressBar() {
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
            publicWebRender.resumeTimers();
            publicWebRender.onShow();

            if(readyToLoadUrl.length() > 0) {
                publicWebRender.load(readyToLoadUrl, null);
                readyToLoadUrl = "";
            }
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


    /* Updater section */


    /* This Thread checks for Updates in the Background */
    private Thread checkUpdate = new Thread() {
        public void run() {
            try {
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
