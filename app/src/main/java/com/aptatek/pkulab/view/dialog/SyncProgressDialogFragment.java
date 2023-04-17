package com.aptatek.pkulab.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.view.connect.turnreaderon.SyncProgress;

import java.util.Locale;

public class SyncProgressDialogFragment extends DialogFragment {

    private static final String KEY_MODEL = "syncProgressModel";

    public static SyncProgressDialogFragment create(@NonNull final SyncProgress syncProgress) {
        final SyncProgressDialogFragment syncProgressDialogFragment = new SyncProgressDialogFragment();
        final Bundle args = new Bundle();
        args.putParcelable(KEY_MODEL, syncProgress);
        syncProgressDialogFragment.setArguments(args);

        return syncProgressDialogFragment;
    }

    private ProgressBar syncProgressBar;
    private TextView syncText;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final SyncProgress syncProgress;

        if (getArguments() != null) {
            syncProgress = getArguments().getParcelable(KEY_MODEL);
        } else {
            syncProgress = null;
        }

        if (syncProgress == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        final View content = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_sync_progress, null);
        syncProgressBar = content.findViewById(R.id.syncProgress);
        syncText = content.findViewById(R.id.syncText);

        internalUpdateProgress(syncProgress);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity())
                .setCancelable(false)
                .setView(content);

        return alertDialogBuilder.create();
    }

    public void updateSyncProgress(SyncProgress syncProgress) {
        if (isAdded()) {
            internalUpdateProgress(syncProgress);
        }
    }

    private void internalUpdateProgress(SyncProgress syncProgress) {
        if (syncProgressBar != null && syncText != null) {
            syncProgressBar.setProgress((int) ((syncProgress.getCurrent() + syncProgress.getFailed()) / (float) syncProgress.getTotal() * 100));
            if (syncProgress.getFailed() != 0) {
                syncText.setText(
                        String.format(Locale.getDefault(), "%d/%d (%d failed)", syncProgress.getCurrent(), syncProgress.getTotal(), syncProgress.getFailed())
                );
                syncText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.applicationRed));
            } else {
                syncText.setText(String.format(Locale.getDefault(), "%d/%d", syncProgress.getCurrent(), syncProgress.getTotal()));
                syncText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.applicationGreen));
            }
        }
    }
}
