package com.aptatek.pkulab.view.settings.web;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aptatek.pkulab.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportIssueDialog extends DialogFragment {

    public static ReportIssueDialog create(){
        return new ReportIssueDialog();
    }

    @OnClick(R.id.btnDataCorruption)
    public void onDataCorruptionClicked() {

    }

    @OnClick(R.id.btnConnectionError)
    public void onConnectionIssueClicked() {

    }

    @OnClick(R.id.btnOther)
    public void onOtherClicked() {

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
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
