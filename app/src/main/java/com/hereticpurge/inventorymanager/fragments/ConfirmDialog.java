package com.hereticpurge.inventorymanager.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialogFragment;

import com.hereticpurge.inventorymanager.R;

public class ConfirmDialog extends AppCompatDialogFragment {

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

    public static ConfirmDialog createDialog(@Nullable ConfirmDialogCallback confirmDialogCallback,
                                             @StringRes int actionString,
                                             @StringRes int confirmString,
                                             @StringRes int cancelString) {

        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.mConfirmDialogCallback = confirmDialogCallback;
        confirmDialog.mActionStringId = actionString;
        confirmDialog.mConfirmStringId = confirmString;
        confirmDialog.mCancelStringId = cancelString;

        return confirmDialog;
    }

    public static ConfirmDialog createDialog(@Nullable ConfirmDialogCallback confirmDialogCallback) {
        return createDialog(confirmDialogCallback,
                ACTION_STRING_DEFAULT,
                CONFIRM_STRING_DEFAULT,
                CANCEL_STRING_DEFAULT);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
        return getResources().getString(resource);
    }

    public boolean getResult() {
        return mResult;
    }

    public interface ConfirmDialogCallback {
        public void onConfirm();

        public void onCancel();
    }
}
