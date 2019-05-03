package com.aptatek.pkulab.view.main.weekly.pdf;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aptatek.pkulab.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PdfExportDialog extends DialogFragment {

    public interface PdfExportDialogCallback{
        void onIntervalSelected(@NonNull PdfExportInterval pdfExportInterval);
    }

    public static PdfExportDialog create(@NonNull final PdfExportDialogCallback pdfExportDialogCallback){
        final PdfExportDialog pdfExportDialog = new PdfExportDialog();
        pdfExportDialog.callback = pdfExportDialogCallback;
        return pdfExportDialog;
    }

    @Nullable
    private PdfExportDialogCallback callback;

    @OnClick(R.id.btnLastMonth)
    public void onLastMonthClicked() {
        if(callback != null){
            callback.onIntervalSelected(PdfExportInterval.LAST_MONTH);
        }
        dismiss();
    }

    @OnClick(R.id.btnLastThreeMonth)
    public void onLastThreeMonthClicked() {
        if(callback != null){
            callback.onIntervalSelected(PdfExportInterval.LAST_THREE_MONTHS);
        }
        dismiss();
    }

    @OnClick(R.id.btnLastHalfYear)
    public void onLastHalfYearClicked() {
        if(callback != null){
            callback.onIntervalSelected(PdfExportInterval.LAST_HALF_YEAR);
        }
        dismiss();
    }

    @OnClick(R.id.btnLastYear)
    public void onLastYearClicked() {
        if(callback != null){
            callback.onIntervalSelected(PdfExportInterval.LAST_YEAR);
        }
        dismiss();
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
        final View view = inflater.inflate(R.layout.dialog_pdf_export, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
