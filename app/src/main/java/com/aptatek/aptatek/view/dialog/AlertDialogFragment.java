package com.aptatek.aptatek.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.model.AlertDialogModel;

public class AlertDialogFragment extends DialogFragment {

    private static final String KEY_MODEL = "com.aptatek.alertdialog.model";

    private AlertDialogDecisionListener listener;
    private AlertDialogDecisions decision;

    public static AlertDialogFragment create(@NonNull final AlertDialogModel model, @Nullable final AlertDialogDecisionListener listener) {
        final AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        final Bundle args = new Bundle();
        args.putParcelable(KEY_MODEL, model);
        alertDialogFragment.setArguments(args);
        alertDialogFragment.setListener(listener);

        return alertDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialogModel alertDialogModel;

        if (getArguments() != null) {
            alertDialogModel = getArguments().getParcelable(KEY_MODEL);
        } else {
            alertDialogModel = null;
        }

        if (alertDialogModel == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        final DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: {
                        decision = AlertDialogDecisions.POSITIVE;
                        break;
                    }
                    case DialogInterface.BUTTON_NEGATIVE: {
                        decision = AlertDialogDecisions.NEGATIVE;
                        break;
                    }
                    default: {
                        decision = null;
                        break;
                    }
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle(alertDialogModel.getTitle())
                .setMessage(alertDialogModel.getMessage())
                .setNegativeButton(R.string.alertdialog_button_no, clickListener)
                .setPositiveButton(R.string.alertdialog_button_yes, clickListener)
                .create();
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        if (listener != null && decision != null) {
            listener.onDecision(decision);
        }

        super.onDismiss(dialog);
    }

    public void setListener(@Nullable final AlertDialogDecisionListener listener) {
        this.listener = listener;
    }
}
