package org.xdevs23.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.SizeF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import org.xdevs23.android.app.XquidCompatActivity;
import org.xdevs23.android.widget.XquidLinearLayout;
import org.xdevs23.android.widget.XquidRelativeLayout;
import org.xdevs23.general.ExtendedAndroidClass;
import org.xdevs23.ui.dialog.templates.NegativeButtonCancel;
import org.xdevs23.ui.widget.SimpleSeparator;

import io.xdevs23.cornowser.browser.R;

public class EditTextDialog extends ExtendedAndroidClass {

    private DialogInterface.OnClickListener onClickListener;

    private AppCompatEditText mEditText = null;

    private String mTitle = "", mDefaultText = "";

    private AppCompatActivity mActivity;

    public EditTextDialog(Context context) {
        super(context);
        init();
    }

    public EditTextDialog(Context context, AppCompatActivity activity, String title, String defaultText) {
        super(context);
        setContext(activity);
        setPrivateParams(title, defaultText);
        setPrivateActivity(activity);
        init();
    }

    private void setPrivateParams(String title, String defaultText) {
        mTitle = title; mDefaultText = defaultText;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder
                .setCancelable(true)
                .setNegativeButton(R.string.answer_cancel, new NegativeButtonCancel())
                .setPositiveButton(R.string.answer_ok, onClickListener)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mEditText.clearFocus();
                        InputMethodManager imm =
                                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                        if(imm.isActive()) imm.toggleSoftInputFromWindow(mEditText.getWindowToken(),
                                InputMethodManager.SHOW_IMPLICIT, 0);
                    }
                })
                .setView(mEditText);
        builder.create().show();
        mEditText.requestFocus();
        InputMethodManager imm =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

}
