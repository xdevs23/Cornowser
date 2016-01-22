package io.xdevs23.cornowser.browser.browser.modules.tabs;

import android.animation.Animator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.xdevs23.android.widget.XquidLinearLayout;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.ui.utils.DpUtil;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.ColorUtil;

public class BlueListedTabSwitcher extends BasicTabSwitcher {

    private ScrollView mainView;
    private XquidLinearLayout tabsLayout;

    private TabStorage tabStorage;

    private final int mainColor = ContextCompat.getColor(getContext(), R.color.blue_800);

    private int yPos;

    public BlueListedTabSwitcher(Context context, RelativeLayout rootView) {
        super(context, rootView);
    }

    public BlueListedTabSwitcher(RelativeLayout rootView) {
        super(rootView);
    }

    public BlueListedTabSwitcher setTabStorage(TabStorage storage) {
        tabStorage = storage;
        return this;
    }

    private XquidLinearLayout getNewChildLayout(int l, int t, int r, int b, boolean v) {
        XquidLinearLayout layout = new XquidLinearLayout(getContext());
        XquidLinearLayout.LayoutParams params = new XquidLinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMarginsDp(l, t, r, b, getContext());

        layout.setLayoutParams(params);
        layout.setVerticalOrientation(v);

        return layout;
    }

    private XquidLinearLayout getNewChildLayout(int l, int t, int r, int b) {
        return getNewChildLayout(l, t, r, b, true);
    }

    private XquidLinearLayout getNewChildLayout(boolean v) {
        return getNewChildLayout(0, 0, 0, 0, v);
    }

    private TextView getNewItemText() {
        TextView t = new TextView(getContext());
        t.setTextColor(mainColor);
        XquidLinearLayout.LayoutParams p = new XquidLinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        p.setMarginsDp(1, 1, 1, 2, getContext());

        t.setLayoutParams(p);

        return t;
    }

    @Override
    public void init() {
        yPos = CornBrowser.getStaticWindow().getDecorView().getHeight();

        mainView = new ScrollView(getContext());

        mainView.setBackgroundColor(ColorUtil.getColor(R.color.white));

        mainView.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        mainView.getLayoutParams().height = DpUtil.dp2px(getContext(), 160);

        XquidLinearLayout mainLayout = new XquidLinearLayout(getContext());

        XquidLinearLayout switcherLayout = getNewChildLayout(4, 8, 4, 8);

        tabsLayout = getNewChildLayout(1, 1, 1, 2);

        XquidLinearLayout footerLayout = getNewChildLayout(false);
        footerLayout.setGravity(Gravity.RIGHT);

        Button button = new Button(getContext());
        button.setText("new");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTab(new Tab(CornBrowser.getBrowserStorage().getUserHomePage()));
            }
        });

        footerLayout.addView(button);

        switcherLayout.addView(tabsLayout);
        switcherLayout.addView(footerLayout);

        mainLayout.addView(switcherLayout);

        mainView.addView(mainLayout);

        mainView.setVisibility(View.INVISIBLE);

        getRootView().addView(mainView);

        yPos += mainView.getHeight();

        mainView.setTranslationY(yPos);
        mainView.bringToFront();
    }

    @Override
    public void switchTab(int tab) {
        int tabIndex = tab;
        if(tab < 0) tabIndex += 2;
        showTab(tabStorage.getTab(tabIndex));
    }

    @Override
    public void showTab(Tab tab) {
        CornBrowser.publicWebRenderLayout.removeViewAt(0);
        CornBrowser.publicWebRenderLayout.addView(
                tab.webView
        );
        currentTab = tabStorage.getTabIndex(tab);
        CornBrowser.applyInsideOmniText(tab.webView.getUrl());
    }

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab);
        XquidLinearLayout l = new XquidLinearLayout(getContext());
        l.setVerticalOrientation(true);

        TextView titleView = getNewItemText();
        titleView.setText(tab.getTitle());

        TextView urlView = getNewItemText();
        urlView.setText(tab.getUrl());

        TabCounterView counterView = new TabCounterView(getContext());
        counterView.setTabIndex(tabStorage.getTabCountIndex());

        l.addView(titleView);
        l.addView(urlView);
        l.addView(counterView);

        tabsLayout.addView(l);
    }

    @Override
    public void removeTab(Tab tab) {
        tabsLayout.removeViewAt(tabStorage.getTabIndex(tab));
        switchTab(tabStorage.getTabIndex(tab) - 1);
        tabStorage.removeTab(tab);
    }

    @Override
    public void showSwitcher() {
        super.showSwitcher();
        Logging.logd("Showing tab switcher");
        yPos = CornBrowser.getStaticWindow().getDecorView().getHeight();
        mainView.setVisibility(View.VISIBLE);
        mainView.bringToFront();

        mainView.animate().setDuration(320)
                .translationY(yPos - mainView.getHeight()).start();
    }

    @Override
    public void hideSwitcher() {
        super.hideSwitcher();
        Logging.logd("Hiding tab switcher");
        mainView.animate().setDuration(320)
                .translationY(yPos)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        // Not needed
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mainView.setVisibility(View.INVISIBLE);
                        mainView.clearFocus();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // Not needed
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        // Not needed
                    }
                }).start();
    }

}
