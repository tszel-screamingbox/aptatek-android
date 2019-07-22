package com.aptatek.pkulab.view.settings.web.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation.ReportIssue;
import com.aptatek.pkulab.domain.model.ReportIssueType;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation.ReportIssue.ReportType.CONNECTION;
import static com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation.ReportIssue.ReportType.DATA;
import static com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation.ReportIssue.ReportType.OTHER;

public class ReportIssueDialog extends DialogFragment {

    @Inject
    IAnalyticsManager analyticsManager;

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
        analyticsManager.logEvent(new ReportIssue(DATA));
        if (reportIssueDialogCallback != null) {
            reportIssueDialogCallback.onIssueTypeSelected(ReportIssueType.DATA_CORRUPTION);
        }
    }

    @OnClick(R.id.btnConnectionError)
    public void onConnectionIssueClicked() {
        analyticsManager.logEvent(new ReportIssue(CONNECTION));
        if (reportIssueDialogCallback != null) {
            reportIssueDialogCallback.onIssueTypeSelected(ReportIssueType.CONNECTION_ERROR);
        }
    }

    @OnClick(R.id.btnOther)
    public void onOtherClicked() {
        analyticsManager.logEvent(new ReportIssue(OTHER));
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
