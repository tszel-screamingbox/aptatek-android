package com.aptatek.pkulab.view.main.weekly.pdf;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aptatek.pkulab.domain.model.PkuLevelUnits.*;
import static com.aptatek.pkulab.view.main.weekly.pdf.PdfExportInterval.*;

public class PdfExportDialog extends DialogFragment {

    public interface PdfExportDialogCallback {
        void onIntervalSelected(@NonNull PdfExportInterval pdfExportInterval, @NonNull PkuLevelUnits units);
    }

    public static PdfExportDialog create(@NonNull final PdfExportDialogCallback pdfExportDialogCallback,
                                         @NonNull final PkuLevelUnits defaultUnits) {
        final PdfExportDialog pdfExportDialog = new PdfExportDialog();
        pdfExportDialog.callback = pdfExportDialogCallback;
        pdfExportDialog.defaultUnit = defaultUnits;
        return pdfExportDialog;
    }

    @BindView(R.id.unitsGroup)
    RadioGroup unitRadioGroup;

    @Nullable
    private PdfExportDialogCallback callback;
    private PkuLevelUnits defaultUnit;

    @NonNull
    private PkuLevelUnits getSelectedUnit() {
        return unitRadioGroup.getCheckedRadioButtonId() == R.id.rangeSettingsUnitMicroMol ? MICRO_MOL : MILLI_GRAM;
    }

    @OnClick(R.id.btnLastMonth)
    public void onLastMonthClicked() {
        if (callback != null) {
            callback.onIntervalSelected(LAST_MONTH, getSelectedUnit());
        }
        dismiss();
    }

    @OnClick(R.id.btnLastThreeMonth)
    public void onLastThreeMonthClicked() {
        if (callback != null) {
            callback.onIntervalSelected(LAST_THREE_MONTHS, getSelectedUnit());
        }
        dismiss();
    }

    @OnClick(R.id.btnLastHalfYear)
    public void onLastHalfYearClicked() {
        if (callback != null) {
            callback.onIntervalSelected(LAST_HALF_YEAR, getSelectedUnit());
        }
        dismiss();
    }

    @OnClick(R.id.btnLastYear)
    public void onLastYearClicked() {
        if (callback != null) {
            callback.onIntervalSelected(ALL, getSelectedUnit());
        }
        dismiss();
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
        final View view = inflater.inflate(R.layout.dialog_pdf_export, container, false);
        ButterKnife.bind(this, view);

        final int radioButtonId = defaultUnit == MICRO_MOL ?
                R.id.rangeSettingsUnitMicroMol :
                R.id.rangeSettingsUnitMilliGram;
        unitRadioGroup.check(radioButtonId);
        return view;
    }
}
