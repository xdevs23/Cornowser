package org.xdevs23.ui.dialog.templates;

import android.content.DialogInterface;

/**
 * An OnClickListener to kill the application
 */
public class KillAppMessageDialogButton implements DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface dialog, int id) {
		dialog.dismiss();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

}
