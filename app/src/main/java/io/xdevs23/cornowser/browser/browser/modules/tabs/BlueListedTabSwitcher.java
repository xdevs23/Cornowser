package io.xdevs23.cornowser.browser.browser.modules.tabs;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.xdevs23.android.widget.XquidLinearLayout;
import org.xdevs23.android.widget.XquidRelativeLayout;
import org.xdevs23.debugutils.Logging;
import org.xdevs23.threads.Sleeper;
import org.xdevs23.ui.touch.BluePressOnTouchListener;
import org.xdevs23.ui.utils.DpUtil;
import org.xdevs23.ui.widget.SimpleSeparator;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.browser.modules.ColorUtil;
import io.xdevs23.cornowser.browser.browser.modules.WebThemeHelper;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxAnimations;
import io.xdevs23.cornowser.browser.browser.modules.ui.OmniboxControl;
import io.xdevs23.cornowser.browser.browser.xwalk.CrunchyWalkView;

@SuppressWarnings("deprecation")
public class BlueListedTabSwitcher extends BasicTabSwitcher {

    public XquidRelativeLayout mainView;
    protected ScrollView scrollView;
    private XquidLinearLayout tabsLayout;

    private int currentY = 0;

    private int mainColor;

    private TabSwitchListener tabSwitchListener = new TabSwitchListener() {
        private void updateStuff() {
            final Handler handler = new Handler();
            final Runnable doUpdateStuff = new Runnable() {
                @Override
                public void run() {
                    WebThemeHelper.tintNow();
                    CornBrowser.applyOnlyInsideOmniText();
                    CornBrowser.openTabswitcherImgBtn.setTabCount(getTabStorage().getTabCount());
                    CornBrowser.getWebEngine().drawWithColorMode();
                }
            };
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.post(doUpdateStuff);
                }
            }, 120);
        }

        @Override
        public void onTabAdded(Tab tab) {
            updateStuff();
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
            onTabChanged(tab, true);
        }

        @Override
        public void onTabChanged(Tab tab, boolean forcing) {
            ((TextView)((XquidLinearLayout)tabsLayout.getChildAt(tab.tabId))
                    .getChildAt(0)).setText(tab.getTitle());
            ((TextView)((XquidLinearLayout)tabsLayout.getChildAt(tab.tabId))
                    .getChildAt(1)).setText(tab.getUrl());
            if(forcing) updateStuff();
        }
    };


    public BlueListedTabSwitcher(Context context, RelativeLayout rootView) {
        super(context, rootView);
    }

    @SuppressWarnings("unused")
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

    public void initViews() {
        RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        mainParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mainParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mainParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        mainView.setLayoutParams(mainParams);

        mainView.getLayoutParams().height = DpUtil.dp2px(getContext(), 200);

        XquidLinearLayout mainLayout = new XquidLinearLayout(getContext());
        XquidLinearLayout switcherLayout = getNewChildLayout(4, 8, 4, 8);
        XquidLinearLayout footerLayout = getNewChildLayout(false);

        tabsLayout = getNewChildLayout(1, 1, 1, 2);

        footerLayout.setGravity(Gravity.END | Gravity.BOTTOM);

        final FloatingActionButton button = new FloatingActionButton(CornBrowser.getActivity());
        button.setCompatElevation(12f);
        button.setImageResource(R.drawable.main_cross_plus_icon);
        //noinspection all
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && false) {
            ColorStateList csl = ColorStateList.valueOf(mainColor);
            RippleDrawable d = new RippleDrawable(csl, null, null);

            ColorStateList otherCsl =
                    ColorStateList.valueOf(ColorUtil.getColor(R.color.white_semi_transparent));
            d.setColor(otherCsl);

            button.setBackground(d);
        } else { // Dirty fix
            button.setRippleColor(ColorUtil.getColor(R.color.white_semi_transparent));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTab(new Tab(CornBrowser.getBrowserStorage().getUserHomePage()));
            }
        });

        XquidRelativeLayout.LayoutParams fbtnlp = new XquidRelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        fbtnlp.addRule(XquidRelativeLayout.ALIGN_PARENT_BOTTOM);
        fbtnlp.addRule(XquidRelativeLayout.ALIGN_PARENT_RIGHT);
        fbtnlp.setMarginsDp(0, 0, 12, 12, getContext());
        mainView.addView(button, fbtnlp);

        TextView infLpTv = new TextView(getContext());
        infLpTv.setText(getContext().getString(R.string.tabswitch_blue_longpress_to_remove));
        infLpTv.setTextColor(mainColor);
        infLpTv.setGravity(Gravity.CENTER_VERTICAL);

        footerLayout.addView(infLpTv);

        switcherLayout.addView(footerLayout);
        switcherLayout.addView(tabsLayout);

        mainLayout.addView(switcherLayout);

        scrollView.addView(mainLayout);
    }

    @Override
    public void init() {
        mainColor       = ColorUtil.getColor(R.color.blue_800);
        int mainBgColor = ColorUtil.getColor(R.color.white_unnoticeable_opaque);

        mainView = new XquidRelativeLayout(getContext());
        scrollView = new ScrollView(getContext());
        XquidRelativeLayout.LayoutParams lp = new XquidRelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mainView.addView(scrollView, lp);

        mainView.setBackgroundColor(mainBgColor);

        initViews();

        mainView.setVisibility(View.INVISIBLE);

        // Add view to root view of CornBrowser
        getRootView().addView(mainView);
    }

    @Override
    public void switchTab(int tab) {
        super.switchTab(tab);
    }

    @Override
    public void showTab(Tab tab) {
        Logging.logd("Showing tab " + tab.tabId);
        CornBrowser.publicWebRenderLayout.removeAllViews();
        CornBrowser.publicWebRenderLayout.addView(tab.webView);
        CornBrowser.publicWebRender = tab.webView;

        currentTab = tab.tabId;

        setLayoutTabId(currentTab, tab.tabId);

        CornBrowser.applyInsideOmniText();
        tabSwitchListener.onTabSwitched(tab);
    }

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab.initWithWebRender(getContext(), CornBrowser.getActivity()));

        XquidLinearLayout l = new XquidLinearLayout(getContext());
        l.setVerticalOrientation(true);

        TextView titleView = getNewItemText();
        titleView.setText(tab.getTitle());
        titleView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        titleView.setSingleLine(true);

        TextView urlView = getNewItemText();
        urlView.setText(tab.getUrl());
        urlView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        urlView.setSingleLine(true);

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
                if (tabStorage.getTabCount() > 1) {
                    v.setOnClickListener(null);
                    removeTab(tabsLayout.indexOfChild(v));
                }
                else Toast.makeText(getContext(),
                        getContext().getString(R.string.tabswitch_no_removable_tabs),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        l.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                // Do nothing
            }
        });

        tabsLayout.addView(l);
        showTab(getCurrentTab());
        tabSwitchListener.onTabAdded(tab);
    }

    @Override
    public void removeTab(Tab tab) {
        Logging.logd("Removing tab " + tab.tabId);
        CornBrowser.publicWebRenderLayout.removeAllViews();
        tabsLayout.removeViewAt(tab.tabId);
        super.removeTab(tab);
        for ( int i = 0; i < tabsLayout.getChildCount(); i++ )
            setLayoutTabId(i, i);
        for ( int i = 0; i < tabStorage.getTabCount();   i++ )
            tabStorage.getTab(i).setId(i);

        switchTab(0);

        tabSwitchListener.onTabRemoved(tab);
    }

    @Override
    public void removeTab(int tabId) {
        removeTab(tabStorage.getTab(tabId));
    }

    private void animateShowSwitcher() {
        for ( int i = 0; i < mainView.getChildCount(); i++ )
            mainView.getChildAt(i).bringToFront();
        final int beginY  = (int)mainView.getTranslationY(),
                  wantedY = (OmniboxControl.isBottom() ? -CornBrowser.omnibox.getHeight() : 0);
        final Handler handler = new Handler();
        final Runnable setTranslationR = new Runnable() {
            @Override
            public void run() {
                mainView.setTranslationY(currentY);
                mainView.invalidate();
                mainView.bringToFront();
            }
        };
        final Runnable onAnimationEnd = new Runnable() {
            @Override
            public void run() {
                mainView.bringToFront();
            }
        };
        Thread animatorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                currentY = beginY;
                int samewait =
                        (int)((OmniboxAnimations.DEFAULT_ANIMATION_DURATION / (double)beginY + 0.5d)
                                * 100000);
                if(samewait < 1) samewait = (beginY >= 600 ? 6 : 3);
                Logging.logd("Samewait is " + samewait);
                for( int y = currentY; currentY > wantedY; y--) {
                    currentY = y;
                    handler.post(setTranslationR);
                    Sleeper.sleep(0, samewait);
                }
                handler.postDelayed(onAnimationEnd, 1);
            }
        });
        mainView.bringToFront();
        animatorThread.start();
    }

    @Override
    public void showSwitcher() {
        if(switcherStatus == SwitcherStatus.VISIBLE) return;
        super.showSwitcher();
        Logging.logd("Showing tab switcher");

        mainView.setTranslationY(mainView.getHeight());

        mainView.setVisibility(View.VISIBLE);

        animateShowSwitcher();
    }

    @Override
    public void hideSwitcher() {
        if(switcherStatus == SwitcherStatus.HIDDEN) return;
        super.hideSwitcher();
        Logging.logd("Hiding tab switcher");
        mainView.animate().setDuration(OmniboxAnimations.DEFAULT_ANIMATION_DURATION)
                .translationY(mainView.getHeight())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        // Not necessary
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mainView.setVisibility(View.INVISIBLE);
                        mainView.clearFocus();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mainView.setVisibility(View.INVISIBLE);
                        mainView.clearFocus();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        // Not necessary
                    }
                }).start();

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

    @SuppressWarnings("unused")
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

    public void updateAllTabs() {
        for ( Tab t : tabStorage.getTabList() ) {
            t.setUrl(t.webView.getUrl());
            t.setTitle(t.webView.getTitle());
            tabSwitchListener.onTabChanged(t, false);
        }
    }

    public static BlueListedTabSwitcher cast(BasicTabSwitcher switcher) {
        return (BlueListedTabSwitcher)switcher;
    }

}
