package com.aptatek.pkulab.view.settings.web.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.ReportIssueType;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportIssueDialog extends DialogFragment {

    public interface ReportIssueDialogCallback {
        void onIssueTypeSelected(ReportIssueType reportIssueType);
    }

    public static ReportIssueDialog create(@NonNull final ReportIssueDialogCallback reportIssueDialogCallback) {
        final ReportIssueDialog reportIssueDialog = new ReportIssueDialog();
        reportIssueDialog.reportIssueDialogCallback = reportIssueDialogCallback;
        return reportIssueDialog;
    }

    private ReportIssueDialogCallback reportIssueDialogCallback;

    @OnClick(R.id.btnDataCorruption)
    public void onDataCorruptionClicked() {
        if (reportIssueDialogCallback != null) {
            reportIssueDialogCallback.onIssueTypeSelected(ReportIssueType.DATA_CORRUPTION);
        }
    }

    @OnClick(R.id.btnConnectionError)
    public void onConnectionIssueClicked() {
        if (reportIssueDialogCallback != null) {
            reportIssueDialogCallback.onIssueTypeSelected(ReportIssueType.CONNECTION_ERROR);
        }
    }

    @OnClick(R.id.btnOther)
    public void onOtherClicked() {
        if (reportIssueDialogCallback != null) {
            reportIssueDialogCallback.onIssueTypeSelected(ReportIssueType.OTHER);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_report_issue, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
