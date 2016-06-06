package org.xdevs23.ui.dialog.templates;

import android.content.DialogInterface;

/**
 * An OnClickListener to dismiss the dialog, simple as that!
 */
public class DismissDialogButton implements DialogInterface.OnClickListener {

    @Override
    public void onClick(DialogInterface dialog, int id) {
        dialog.dismiss();
    }

}