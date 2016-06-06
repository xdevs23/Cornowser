package org.xdevs23.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.android.widget.XquidRelativeLayout;
import org.xdevs23.general.ExtendedAndroidClass;
import org.xdevs23.ui.dialog.templates.DismissDialogButton;
import org.xdevs23.ui.utils.DpUtil;

import io.xdevs23.cornowser.browser.R;

public class EditTextDialog extends ExtendedAndroidClass {

    private DialogInterface.OnClickListener onClickListener;

    private AppCompatEditText mEditText = null;

    private String mTitle = "", mDefaultText = "", mDescText = "";

    private AppCompatActivity mActivity;

    public EditTextDialog(Context context) {
        super(context);
        init();
    }

    public EditTextDialog(Context context, AppCompatActivity activity, String title, String defaultText) {
        this(context, activity, title, defaultText, "");
    }


    public EditTextDialog(Context context, AppCompatActivity activity, String title,
                          String defaultText, String descText) {
        super(context);
        setContext(activity);
        setPrivateParams(title, defaultText, descText);
        setPrivateActivity(activity);
        init();
    }

    private void setPrivateParams(String title, String defaultText, String descText) {
        mTitle = title; mDefaultText = defaultText; mDescText = descText;
    }

    private void init() {
        mEditText = (AppCompatEditText)
                LayoutInflater.from(getContext()).inflate(R.layout.widget_ac_edittext_xquid, null);
        mEditText.setSingleLine();
        if(!mTitle.isEmpty()) mEditText.setHint(mTitle);
        if(!mTitle.isEmpty()) mEditText.getEditableText().insert(0, mDefaultText);
        mEditText.setHighlightColor(ContextCompat.getColor(getContext(), R.color.blue_600));
        mEditText.setTextColor(Color.BLACK);

        onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
    }

    public EditTextDialog setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public EditTextDialog setPrivateActivity(AppCompatActivity activity) {
        this.mActivity = activity;
        return this;
    }

    public EditTextDialog setPrivateActivity(XquidCompatActivity activity) {
        this.mActivity = activity;
        return this;
    }

    public AppCompatEditText getEditText() {
        return mEditText;
    }

    public String getEnteredText() {
        return mEditText.getEditableText().toString();
    }

    public void showDialog() {
        XquidRelativeLayout dialogContent = new XquidRelativeLayout(getContext());
        final TextView descTextView = new TextView(getContext());
        descTextView.setText(mDescText);
        descTextView.setTextColor(Color.BLACK);
        dialogContent.addView(descTextView);
        dialogContent.addView(mEditText);
        XquidRelativeLayout.LayoutParams lp = new XquidRelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (mDescText.isEmpty() ? 0 : DpUtil.dp2px(getContext(), 140))
        );
        lp.setMarginsDp(2, 2, 2, 2, getContext());
        dialogContent.setLayoutParams(lp);
        dialogContent.setMinimumHeight(lp.height);
        XquidRelativeLayout.LayoutParams lpe = new XquidRelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mEditText.setLayoutParams(lpe);
        // Make sure mEditText is below descTextView
        descTextView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getEditText().setTranslationY((mDescText.isEmpty() ? 0 :
                        descTextView.getHeight() + DpUtil.dp2px(getContext(), 2)));
                if( Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
                    descTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder
                .setCancelable(true)
                .setNegativeButton(R.string.answer_cancel, new DismissDialogButton())
                .setPositiveButton(R.string.answer_ok, onClickListener)
                .setTitle(mTitle)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mEditText.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getContext()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                    }
                })
                .setView(dialogContent).create().show();

        mEditText.requestFocus();
        InputMethodManager imm =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

}
