package io.xdevs23.cornowser.browser.browser.modules.tabs;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

    public XquidRelativeLayout mainView, fabLayout;
    protected ScrollView scrollView;
    private XquidLinearLayout tabsLayout;

    private int currentY = 0;

    private int mainColor, darkColor;

    private boolean isNewTabExpanded = false;
    private View.OnClickListener fabButtonListener;
    private LinearLayout mainFabL, normalTabBtnL, incognitoBtnL;
    private FloatingActionButton mainFab, normalTabBtn, incognitoBtn;
    private TextView incognitoLb;

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

    private TextView getNewItemText(Tab tab) {
        TextView t = new TextView(getContext());
        t.setTextColor(tab.isIncognito ? darkColor : mainColor);
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

        fabLayout = new XquidRelativeLayout(getContext());
        final XquidRelativeLayout.LayoutParams fllp = new XquidRelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        fllp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        fllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        fllp.setMarginsDp(0, 0, 10, 10, getContext());

        final LinearLayout.LayoutParams mflp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        mflp.gravity = Gravity.END;

        final LinearLayout.LayoutParams cvlp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        cvlp.gravity = Gravity.CENTER_VERTICAL;
        cvlp.rightMargin = DpUtil.dp2px(getContext(), 4);

        AppCompatActivity ca = (AppCompatActivity)CornBrowser.getActivity();

        int rc = ColorUtil.getColor(R.color.white_semi_transparent);
        final FloatingActionButton button = new FloatingActionButton(ca);
        mainFab = button;
        button.setBackgroundColor(ColorUtil.getColor(R.color.blue_600));
        button.setRippleColor(rc);
        button.setCompatElevation(8f);
        //button.setSize(FloatingActionButton.SIZE_NORMAL);
        button.setImageResource(R.drawable.main_cross_plus_icon);

        mainFabL = new LinearLayout(getContext());
        mainFabL.setGravity(Gravity.END);
        mainFabL.addView(mainFab, mflp);

        normalTabBtn = new FloatingActionButton(ca);
        normalTabBtn.setBackgroundColor(ColorUtil.getColor(R.color.blue_700));
        normalTabBtn.setRippleColor(rc);
        normalTabBtn.setCompatElevation(6f);
        //normalTabBtn.setSize(FloatingActionButton.SIZE_MINI);
        normalTabBtn.setImageResource(R.drawable.main_cross_plus_icon);
        normalTabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTab(new Tab(CornBrowser.getBrowserStorage().getUserHomePage()));
            }
        });

        normalTabBtnL = new LinearLayout(getContext());
        final TextView normalTabLb = new TextView(getContext());
        normalTabLb.setText(getContext().getString(R.string.tabswitch_new_tab));
        normalTabLb.setTextColor(ColorUtil.getColor(R.color.white));
        normalTabLb.setBackgroundResource(R.drawable.fab_label_bg);
        normalTabLb.setTextSize(18);
        normalTabLb.setSingleLine();
        normalTabLb.setTypeface(null, Typeface.BOLD);
        normalTabLb.setGravity(Gravity.CENTER_VERTICAL);
        normalTabBtnL.setOrientation(LinearLayout.HORIZONTAL);
        normalTabBtnL.setDividerPadding(DpUtil.dp2px(getContext(), 4));
        normalTabBtnL.setGravity(Gravity.END);
        normalTabBtnL.addView(normalTabLb, cvlp);
        normalTabBtnL.addView(normalTabBtn);

        incognitoBtn = new FloatingActionButton(ca);
        incognitoBtn.setBackgroundColor(ColorUtil.getColor(R.color.blue_700));
        incognitoBtn.setRippleColor(rc);
        incognitoBtn.setCompatElevation(4f);
        //incognitoBtn.setSize(FloatingActionButton.SIZE_MINI);
        incognitoBtn.setImageResource(R.drawable.main_cross_plus_icon);
        incognitoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTab(new Tab(true));
            }
        });

        incognitoBtnL = new LinearLayout(getContext());
        incognitoLb = new TextView(getContext());
        incognitoLb.setText(getContext().getString(R.string.tabswitch_new_incognito_tab));
        incognitoLb.setTextColor(ColorUtil.getColor(R.color.white));
        incognitoLb.setBackgroundResource(R.drawable.fab_label_bg);
        incognitoLb.setTextSize(18);
        incognitoLb.setSingleLine();
        incognitoLb.setTypeface(null, Typeface.BOLD);
        incognitoLb.setGravity(Gravity.CENTER_VERTICAL);
        incognitoBtnL.setOrientation(LinearLayout.HORIZONTAL);
        incognitoBtnL.setDividerPadding(DpUtil.dp2px(getContext(), 4));
        incognitoBtnL.setGravity(Gravity.END);
        incognitoBtnL.addView(incognitoLb, cvlp);
        incognitoBtnL.addView(incognitoBtn, mflp);

        fabLayout.addView(mainFabL, fllp);
        fabLayout.addView(normalTabBtnL, fllp);
        fabLayout.addView(incognitoBtnL, fllp);

        normalTabBtnL.bringToFront();
        mainFabL.bringToFront();

        incognitoBtnL.setVisibility(View.INVISIBLE);
        normalTabBtnL.setVisibility(View.INVISIBLE);

        final int goodFabMargin = DpUtil.dp2px(getContext(), 4);

        fabButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Logging.logd("Tab switcher fab clicked!");
                if(!isNewTabExpanded) {
                    fabLayout.getLayoutParams().height =
                            incognitoBtn.getHeight() +
                                    normalTabBtn.getHeight() +
                                    button.getHeight()
                                    + goodFabMargin * 5;
                    incognitoLb.setAlpha(0f);
                    normalTabLb.setAlpha(0f);
                    incognitoBtnL.setVisibility(View.VISIBLE);
                    normalTabBtnL.setVisibility(View.VISIBLE);
                    final int nby = -(normalTabBtn.getHeight() + goodFabMargin);
                    normalTabBtnL.animate()
                            .setDuration(320)
                            .translationY(nby)
                            .start();
                    final int iby = -(incognitoBtn.getHeight() + goodFabMargin +
                            normalTabBtn.getHeight() + goodFabMargin);
                    incognitoBtnL.animate()
                            .setDuration(320)
                            .translationY(iby)
                            .start();
                    normalTabLb.animate()
                            .setStartDelay(10)
                            .setDuration(100)
                            .alpha(1f)
                            .start();
                    incognitoLb.animate()
                            .setStartDelay(10)
                            .setDuration(100)
                            .alpha(1f)
                            .start();
                    mainFab.animate()
                            .setDuration(322)
                            .rotationBy(45)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    incognitoBtnL.invalidate();
                                    normalTabBtnL.invalidate();
                                    mainFabL.invalidate();
                                    normalTabBtnL.bringToFront();
                                    mainFabL.bringToFront();
                                    incognitoBtnL.setTranslationY(0);
                                    normalTabBtnL.setTranslationY(0);
                                    mainFab.setRotation(0);
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    normalTabBtnL.setTranslationY(nby);
                                    incognitoBtnL.setTranslationY(iby);
                                    mainFab.setRotation(45);
                                    normalTabBtnL.invalidate();
                                    incognitoBtnL.invalidate();
                                    mainFabL.invalidate();
                                    normalTabBtnL.bringToFront();
                                    mainFabL.bringToFront();
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {
                                    onClick(view);
                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {
                                    // Not needed
                                }
                            })
                            .start();
                    isNewTabExpanded = true;
                } else {
                    incognitoLb.setAlpha(1f);
                    normalTabLb.setAlpha(1f);
                    incognitoBtnL.animate()
                            .setDuration(320)
                            .translationY(0)
                            .start();
                    normalTabBtnL.animate()
                            .setDuration(320)
                            .translationY(0)
                            .start();
                    normalTabLb.animate()
                            .setStartDelay(160)
                            .setDuration(140)
                            .alpha(0f)
                            .start();
                    incognitoLb.animate()
                            .setStartDelay(160)
                            .setDuration(140)
                            .alpha(0f)
                            .start();
                    mainFab.animate()
                            .setDuration(320)
                            .rotationBy(-45)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    normalTabBtnL.clearFocus();
                                    normalTabBtnL.refreshDrawableState();
                                    incognitoBtnL.clearFocus();
                                    incognitoBtnL.refreshDrawableState();
                                    normalTabBtnL.invalidate();
                                    incognitoBtnL.invalidate();
                                    mainFabL.invalidate();
                                    normalTabBtnL.bringToFront();
                                    mainFabL.bringToFront();
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    incognitoBtnL.setVisibility(View.GONE);
                                    normalTabBtnL.setVisibility(View.GONE);
                                    normalTabBtnL.invalidate();
                                    incognitoBtnL.invalidate();
                                    mainFabL.invalidate();
                                    normalTabBtnL.bringToFront();
                                    mainFabL.bringToFront();
                                    fabLayout.getLayoutParams().height = button.getHeight()
                                            + DpUtil.dp2px(getContext(), 8);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {
                                    // Not needed
                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {
                                    // Not needed
                                }
                            })
                            .start();
                    isNewTabExpanded = false;
                }

            }
        };

        XquidRelativeLayout.LayoutParams fbtnlp = new XquidRelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        fbtnlp.addRule(XquidRelativeLayout.ALIGN_PARENT_BOTTOM);
        fbtnlp.addRule(XquidRelativeLayout.ALIGN_PARENT_RIGHT);
        fbtnlp.setMarginsDp(0, 0, 0, 0, getContext());
        mainView.addView(fabLayout, fbtnlp);

        fabLayout.bringToFront();

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
        darkColor       = ColorUtil.getColor(R.color.dark_semi_bitless_transparent);
        int mainBgColor = ColorUtil.getColor(R.color.white_semi_min_transparent);

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
    public void showTab(Tab tab, boolean isNew) {
        Logging.logd("Showing tab " + tab.tabId);
        super.showTab(tab);
        CornBrowser.publicWebRenderLayout.removeAllViews();
        CornBrowser.publicWebRenderLayout.addView(tab.webView);
        CornBrowser.publicWebRender = tab.webView;

        currentTab = tab.tabId;

        setLayoutTabId(currentTab, tab.tabId);

        CornBrowser.applyOnlyInsideOmniText();
        if(!isNew) tabSwitchListener.onTabSwitched(tab);
    }

    @Override
    public void showTab(Tab tab) {
        showTab(tab, tab.isNew);
    }

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab.initWithWebRender(getContext(), CornBrowser.getActivity()));

        XquidLinearLayout l = new XquidLinearLayout(getContext());
        l.setVerticalOrientation(true);

        TextView titleView = getNewItemText(tab);
        titleView.setText(tab.getTitle().isEmpty() ?
                (tab.isIncognito ? getContext().getString(R.string.tabswitch_new_incognito_tab)
                                 : getContext().getString(R.string.tabswitch_new_tab))
                : tab.getTitle());
        Logging.logd("titleView text set to " + titleView.getText());
        titleView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        titleView.setSingleLine(true);

        TextView urlView = getNewItemText(tab);
        urlView.setText(tab.getUrl());
        urlView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        urlView.setSingleLine(true);

        TabCounterView counterView = new TabCounterView(getContext());
        counterView.setTabIndex(currentTab);

        l.addView(titleView);
        l.addView(urlView);
        l.addView(counterView);
        l.addView(new SimpleSeparator(getContext()).setSeparatorColor(
                tab.isIncognito ? darkColor : mainColor));

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
        showTab(getCurrentTab(), true);
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
                fabLayout.invalidate();
                fabLayout.bringToFront();
                mainFab.setOnClickListener(fabButtonListener);
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
        fabLayout.getLayoutParams().width =
                incognitoBtnL.getWidth() + incognitoLb.getWidth() + 2;
        mainFab.animate()
                .setDuration(1)
                .rotationBy(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        normalTabBtnL.clearFocus();
                        normalTabBtnL.refreshDrawableState();
                        incognitoBtnL.clearFocus();
                        incognitoBtnL.refreshDrawableState();
                        normalTabBtnL.invalidate();
                        incognitoBtnL.invalidate();
                        mainFabL.invalidate();
                        normalTabBtnL.bringToFront();
                        mainFabL.bringToFront();
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        incognitoBtnL.setVisibility(View.GONE);
                        normalTabBtnL.setVisibility(View.GONE);
                        normalTabBtnL.invalidate();
                        incognitoBtnL.invalidate();
                        mainFabL.invalidate();
                        normalTabBtnL.bringToFront();
                        mainFabL.bringToFront();
                        fabLayout.getLayoutParams().height = mainFabL.getHeight()
                                + DpUtil.dp2px(getContext(), 8);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        // Not needed
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                        // Not needed
                    }
                })
                .start();
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
        if(isNewTabExpanded) mainFab.performClick();
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
            if(t.isNew) continue;
            t.setUrl(t.webView.getUrl());
            t.setTitle(t.webView.getTitle());
            tabSwitchListener.onTabChanged(t, false);
        }
    }

    public static BlueListedTabSwitcher cast(BasicTabSwitcher switcher) {
        return (BlueListedTabSwitcher)switcher;
    }

}
