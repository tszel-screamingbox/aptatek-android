package com.aptatek.pkulab.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;

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

        final AlertDialog.Builder alertDialogBuilder;

        if (alertDialogModel.isAlertHeader()) {
            final View content = LayoutInflater.from(requireActivity()).inflate(R.layout.layout_alert_error, null);
            final TextView errorCode = content.findViewById(R.id.error_code);
            final TextView title = content.findViewById(R.id.error_title);
            final TextView message = content.findViewById(R.id.error_message);

            errorCode.setText(alertDialogModel.getErrorCode());
            title.setText(alertDialogModel.getTitle());
            message.setText(alertDialogModel.getMessage());

            alertDialogBuilder = new AlertDialog.Builder(requireActivity(), alertDialogModel.getTheme())
                    .setCancelable(alertDialogModel.isCancelable())
                    .setView(content);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(requireActivity(), alertDialogModel.getTheme())
                    .setCancelable(alertDialogModel.isCancelable())
                    .setTitle(alertDialogModel.getTitle())
                    .setMessage(alertDialogModel.getMessage());
        }

        final AlertDialog alertDialog = alertDialogBuilder
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

            final TextView dialogTitle = alertDialog.findViewById(R.id.alertTitle);
            if (dialogTitle != null) {
                final Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.nunito_black);
                dialogTitle.setTypeface(typeface);
            }
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
