package io.xdevs23.cornowser.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.rey.material.widget.ProgressView;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.debugUtils.Logging;
import org.xdevs23.ui.utils.BarColors;

import io.xdevs23.cornowser.browser.browser.BrowserStorage;
import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

public class CornBrowser extends XquidCompatActivity {

    public static CrunchyWalkView publicWebRender = null;

    public static RelativeLayout
            omnibox                 = null,
            publicWebRenderLayout   = null
            ;

    public static EditText browserInputBar = null;

    private static View staticView;
    private static Context staticContext;
    private static Activity staticActivity;
    private static Window staticWindow;

    private static BrowserStorage browserStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_corn);

        initAll();

        publicWebRender.load(browserStorage.getUserHomePage(), null);
    }

    /**
     * Initialize everything
     */
    public void initAll() {
        Logging.logd("Initializing...");
        preInit();
        init();
    }

    /**
     * Initialize some stuff before getting started
     */
    public void preInit() {
        initStaticFields();
        BarColors.enableBarColoring(staticWindow, R.color.colorPrimaryDark);
    }

    /**
     * Main initialization
     */
    public void init() {
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
        publicWebRenderLayout   = (RelativeLayout)  findViewById(R.id.webrender_layout);
        publicWebRender         = new CrunchyWalkView(getContext(), getActivity());
        publicWebRenderLayout.addView(publicWebRender);
    }

    /**
     * Initialize the omnibox
     */
    public void initOmnibox() {
        Logging.logd("  ... Omnibox");
        omnibox                 = (RelativeLayout)  findViewById(R.id.omnibox_layout);
        browserInputBar         = (EditText) findViewById(R.id.omnibox_input_bar);


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
        return ((ProgressView) staticView.findViewById(R.id.omnibox_progressbar));
    }

    @Override
    protected void onPause() {
        Logging.logd("Activity paused.");
        super.onPause();
        if (publicWebRender != null) {
            publicWebRender.pauseTimers();
            publicWebRender.onHide();
        }
    }

    @Override
    protected void onResume() {
        Logging.logd("Activity resumed.");
        super.onResume();
        if (publicWebRender != null) {
            publicWebRender.resumeTimers();
            publicWebRender.onShow();
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
}
