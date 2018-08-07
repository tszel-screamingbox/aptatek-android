package com.aptatek.pkuapp.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.aptatek.pkuapp.domain.model.AlertDialogModel;

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

        final DialogInterface.OnClickListener clickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {
                    decision = AlertDialogDecisions.POSITIVE;
                    break;
                }
                case DialogInterface.BUTTON_NEGATIVE: {
                    decision = AlertDialogDecisions.NEGATIVE;
                    break;
                }
                case DialogInterface.BUTTON_NEUTRAL: {
                    decision = AlertDialogDecisions.NEUTRAL;
                    break;
                }
                default: {
                    decision = null;
                    break;
                }
            }
        };

        final AlertDialog alertDialog = new AlertDialog.Builder(requireActivity(), alertDialogModel.getTheme())
                .setCancelable(alertDialogModel.isCancelable())
                .setTitle(alertDialogModel.getTitle())
                .setMessage(alertDialogModel.getMessage())
                .setNegativeButton(alertDialogModel.getNegativeButtonText(), clickListener)
                .setPositiveButton(alertDialogModel.getPositiveButtonText(), clickListener)
                .setNeutralButton(alertDialogModel.getNeutralButtonText(), clickListener)
                .create();

        alertDialog.setOnShowListener(dialog -> {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), alertDialogModel.getNegativeButtonTextColor()));
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(requireContext(), alertDialogModel.getPositiveButtonTextColor()));
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
                    .setTextColor(ContextCompat.getColor(requireContext(), alertDialogModel.getNeutralButtonTextColor()));
        });
        return alertDialog;
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
