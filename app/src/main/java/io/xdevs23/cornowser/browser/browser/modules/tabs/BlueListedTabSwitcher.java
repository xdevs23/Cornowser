package io.xdevs23.cornowser.browser.browser.modules.tabs;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.xdevs23.android.widget.XquidLinearLayout;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.ui.touch.BluePressOnTouchListener;
import org.xdevs23.ui.touch.PressHoverTouchListener;
import org.xdevs23.ui.utils.DpUtil;
import org.xdevs23.ui.widget.SimpleSeparator;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.ColorUtil;
import io.xdevs23.cornowser.browser.browser.modules.WebThemeHelper;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxControl;

public class BlueListedTabSwitcher extends BasicTabSwitcher {

    private MeasuringScrollView mainView;
    private XquidLinearLayout tabsLayout;

    private TabSwitchListener tabSwitchListener = new TabSwitchListener() {
        private void updateStuff() {
            final Handler handler = new Handler();
            final Runnable doUpdateStuff = new Runnable() {
                @Override
                public void run() {
                    WebThemeHelper.tintNow(CornBrowser.getWebEngine());
                    CornBrowser.applyOnlyInsideOmniText(CornBrowser.getWebEngine().getUrl());
                    CornBrowser.openTabswitcherImgBtn.setTabCount(getTabStorage().getTabCount());
                }
            };
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.post(doUpdateStuff);
                }
            }, 200);
        }

        @Override
        public void onTabAdded(Tab tab) {
            updateStuff();
            CornBrowser.publicWebRender.load(tab.getUrl());
        }

        @Override
        public void onTabRemoved(Tab tab) {
            updateStuff();
        }

        @Override
        public void onTabSwitched(Tab tab) {
            onTabChanged(tab);
        }

        @Override
        public void onTabChanged(Tab tab) {
            ((TextView)((XquidLinearLayout)tabsLayout.getChildAt(tab.tabId))
                    .getChildAt(0)).setText(tab.getTitle());
            ((TextView)((XquidLinearLayout)tabsLayout.getChildAt(tab.tabId))
                    .getChildAt(1)).setText(tab.getUrl());
            updateStuff();
        }
    };

    private int mainColor;

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

        mainColor = ColorUtil.getColor(R.color.blue_800);

        mainView = new MeasuringScrollView(getContext());
        mainView.setOnMeasureListener(new MeasuringScrollView.OnMeasureListener() {
            @Override
            public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                animateShowSwitcher();
            }
        });

        mainView.setBackgroundColor(ColorUtil.getColor(R.color.white_semi_less_transparent));

        mainView.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        mainView.getLayoutParams().height = DpUtil.dp2px(getContext(), 200);

        XquidLinearLayout mainLayout = new XquidLinearLayout(getContext());

        XquidLinearLayout switcherLayout = getNewChildLayout(4, 8, 4, 8);

        tabsLayout = getNewChildLayout(1, 1, 1, 2);

        XquidLinearLayout footerLayout = getNewChildLayout(false);
        footerLayout.setGravity(Gravity.RIGHT | Gravity.TOP);

        final int minWh  = DpUtil.dp2px(getContext(), 24);
        final int bpd    = DpUtil.dp2px(getContext(), 2);
        final int minWhP = minWh - bpd;

        RelativeLayout button = new RelativeLayout(getContext());
        button.setBackgroundColor(mainColor);
        button.setMinimumWidth(minWh);
        button.setMinimumHeight(minWh);

        ImageView img = new ImageView(getContext());
        img.setImageResource(R.drawable.main_cross_plus_icon);
        img.setMinimumWidth(minWhP);
        img.setMinimumHeight(minWhP);

        button.addView(img);

        button.setOnTouchListener(new PressHoverTouchListener(mainColor, ColorUtil.getColor(R.color.blue_600)));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTab(new Tab(CornBrowser.getBrowserStorage().getUserHomePage()));
            }
        });

        button.setPadding(bpd, bpd, bpd, bpd);

        footerLayout.addView(button);

        switcherLayout.addView(footerLayout);
        switcherLayout.addView(tabsLayout);

        mainLayout.addView(switcherLayout);

        mainView.addView(mainLayout);

        mainView.setVisibility(View.INVISIBLE);

        getRootView().addView(mainView);

        mainView.setTranslationY(yPos - mainView.getHeight());
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
        CornBrowser.publicWebRenderLayout.removeAllViews();
        CornBrowser.publicWebRenderLayout.addView(
                tab.webView
        );
        CornBrowser.publicWebRender = tab.webView;
        currentTab = tab.tabId;
        setLayoutTabId(currentTab, tab.tabId);
        CornBrowser.applyInsideOmniText(tab.webView.getUrl());
        tabSwitchListener.onTabSwitched(tab);
    }

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab.initWithWebRender(getContext(), CornBrowser.getActivity()));

        XquidLinearLayout l = new XquidLinearLayout(getContext());
        l.setVerticalOrientation(true);

        TextView titleView = getNewItemText();
        titleView.setText(tab.getTitle());

        TextView urlView = getNewItemText();
        urlView.setText(tab.getUrl());

        TabCounterView counterView = new TabCounterView(getContext());
        counterView.setTabIndex(currentTab);

        l.addView(titleView);
        l.addView(urlView);
        l.addView(counterView);
        l.addView(new SimpleSeparator(getContext()).setSeparatorColor(mainColor));

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTab(tabStorage.getTab(tabsLayout.indexOfChild(v)));
            }
        });
        l.setOnTouchListener(new BluePressOnTouchListener(Color.TRANSPARENT));
        l.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.setOnClickListener(null);
                if (tabsLayout.getChildCount() > 1) removeTab(tabsLayout.indexOfChild(v));
                else Toast.makeText(getContext(), "(°o°)", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        l.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                // Do nothing!!
            }
        });

        tabsLayout.addView(l);
        showTab(getCurrentTab());
        tabSwitchListener.onTabAdded(tab);
    }

    @Override
    public void removeTab(Tab tab) {
        CornBrowser.publicWebRenderLayout.removeAllViews();
        tabsLayout.removeViewAt(tabStorage.getTabIndex(tab));
        switchTab(0);
        super.removeTab(tab);
        tabSwitchListener.onTabRemoved(tab);
    }

    @Override
    public void removeTab(int tabId) {
        removeTab(tabStorage.getTab(tabId));
    }

    private void animateShowSwitcher() {
        mainView.setTranslationY(yPos - mainView.getHeight());
    }

    @Override
    public void showSwitcher() {
        if(switcherStatus == SwitcherStatus.VISIBLE) return;
        super.showSwitcher();
        Logging.logd("Showing tab switcher");
        yPos =
                CornBrowser.getView().getHeight() -
                        (OmniboxControl.isBottom() ? CornBrowser.omnibox.getHeight() : 0);
        mainView.setVisibility(View.VISIBLE);
        mainView.bringToFront();

        animateShowSwitcher();
        /* mainView.animate().setDuration(320)
                .translationY(yPos - mainView.getHeight()).start(); */
    }

    @Override
    public void hideSwitcher() {
        if(switcherStatus == SwitcherStatus.HIDDEN) return;
        super.hideSwitcher();
        Logging.logd("Hiding tab switcher");
        /*mainView.animate().setDuration(320)
                .translationY(yPos)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        // Not needed
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) { */
        mainView.setVisibility(View.INVISIBLE);
        mainView.clearFocus();
        /*
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
                */
    }

    @Override
    public void changeCurrentTab(String url, String title) {
        if(CornBrowser.publicWebRender != null && url != null && title != null
                && (!url.isEmpty())) {
            try {
                if (url.equals(CornBrowser.publicWebRender.getUrl()))
                    super.changeCurrentTab(url, title);
                else super.changeCurrentTab(CornBrowser.publicWebRender.getUrl(),
                        CornBrowser.publicWebRender.getTitle());
                tabSwitchListener.onTabChanged(getCurrentTab());
            } catch (NullPointerException ex) {
                Logging.logd("Umm... Are you jumping on a null? ~changeCurrentTab");
            }
        }
    }

    protected int getLayoutTabId(int index) {
        return ((TabCounterView)((XquidLinearLayout) tabsLayout.getChildAt(index))
                .getChildAt(2)).getTabIndex();
    }

    private void setLayoutTabId(int index, int id) {
        try {
            ((TabCounterView) ((XquidLinearLayout) tabsLayout.getChildAt(index))
                    .getChildAt(2)).setTabIndex(id);
        } catch(Exception ex) {
            Logging.logd("Something went wrong... ~setLayoutTabId (Tab switcher)");
        }
    }

    protected static class MeasuringScrollView extends ScrollView {

        public static interface OnMeasureListener {
            public abstract void onMeasure(int widthMeasureSpec, int heightMeasureSpec);
        }

        private OnMeasureListener measureListener = new OnMeasureListener() {
            @Override
            public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                // Nothing
            }
        };

        public MeasuringScrollView(Context context) {
            super(context);
        }

        public MeasuringScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public MeasuringScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public MeasuringScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public MeasuringScrollView setOnMeasureListener(OnMeasureListener ml) {
            measureListener = ml;
            return this;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            measureListener.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
