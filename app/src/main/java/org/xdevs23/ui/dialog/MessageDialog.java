package org.xdevs23.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;

import org.xdevs23.ui.dialog.templates.DismissDialogButton;

import io.xdevs23.cornowser.browser.R;

public class MessageDialog {
	
	private static DialogInterface.OnClickListener defaultOnClickListener = new DismissDialogButton();
	
	public static void showDialog(String title, String message, Context context, DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder
                .setTitle   (title  )
				.setMessage (message)
				.setPositiveButton(context.getString(R.string.answer_ok), onClickListener);
		
		AppCompatDialog alertDialog = alertDialogBuilder.create();
		
		alertDialog.show();
	}
	
	public static void showDialog(String title, String message, Context context) {
		showDialog(title, message, context, defaultOnClickListener);
	}
	
}
