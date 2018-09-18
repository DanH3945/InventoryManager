package com.hereticpurge.inventorymanager.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialogFragment;

import com.hereticpurge.inventorymanager.R;

public class ConfirmDialog extends AppCompatDialogFragment {

    // Creates a confirmation dialog with confirm / cancel buttons and stores the result.
    // I tried to make this class as generic as possible to re-use in future projects.

    private static final int ACTION_STRING_DEFAULT = R.string.dialog_action_default;
    private static final int CONFIRM_STRING_DEFAULT = R.string.dialog_confirm;
    private static final int CANCEL_STRING_DEFAULT = R.string.dialog_cancel;

    private static final int DIALOG_BODY_START = R.string.dialog_body_start;
    private static final int DIALOG_BODY_END = R.string.dialog_body_end;

    private ConfirmDialogCallback mConfirmDialogCallback;

    private int mActionStringId;
    private int mConfirmStringId;
    private int mCancelStringId;

    private boolean mResult;

    // Static creation classes for the Dialog since Android prefers we use the no arg constructor.
    // if the ConfirmDialogCallback is null you can use get result to get the click result.
    public static ConfirmDialog createDialog(@Nullable ConfirmDialogCallback confirmDialogCallback,
                                             @StringRes int actionString,
                                             @StringRes int confirmString,
                                             @StringRes int cancelString) {
        // Use string resources to specify a question and possible responses or use the default
        // creator below for a generic confirm / cancel.

        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.mConfirmDialogCallback = confirmDialogCallback;
        confirmDialog.mActionStringId = actionString;
        confirmDialog.mConfirmStringId = confirmString;
        confirmDialog.mCancelStringId = cancelString;

        return confirmDialog;
    }

    public static ConfirmDialog createDialog(@Nullable ConfirmDialogCallback confirmDialogCallback) {
        // Default object creator will use the values stored in the class when creating the dialog
        // question and possible responses.
        return createDialog(confirmDialogCallback,
                ACTION_STRING_DEFAULT,
                CONFIRM_STRING_DEFAULT,
                CANCEL_STRING_DEFAULT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Assemble the question to ask the user and activate the callbacks if the callback is not
        // null.
        String messageString = getStringFromResource(DIALOG_BODY_START) +
                getStringFromResource(mActionStringId) +
                getStringFromResource(DIALOG_BODY_END);

        String confirmString = getStringFromResource(mConfirmStringId);
        String cancelString = getStringFromResource(mCancelStringId);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(messageString)
                .setPositiveButton(confirmString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mResult = true;
                        if (mConfirmDialogCallback != null) {
                            mConfirmDialogCallback.onConfirm();
                        }
                    }
                })
                .setNegativeButton(cancelString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mResult = false;
                        if (mConfirmDialogCallback != null) {
                            mConfirmDialogCallback.onCancel();
                        }
                    }
                });
        return builder.create();
    }

    private String getStringFromResource(@StringRes int resource) {
        // Helper method to pull a String from a given @StringRes id.
        return getResources().getString(resource);
    }

    public boolean getResult() {
        return mResult;
    }

    public interface ConfirmDialogCallback {
        // Callback to perform actions based on button pressed.  Needs to be instantiated in the
        // calling class and sent in via the helper creation methods above.
        void onConfirm();

        void onCancel();
    }
}
