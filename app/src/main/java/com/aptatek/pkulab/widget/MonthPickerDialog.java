package com.aptatek.pkulab.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.aptatek.pkulab.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MonthPickerDialog extends DialogFragment {

    private static final int MIN_MONTH_VALUE = 1;
    private static final int MAX_MONTH_VALUE = 12;
    private static final int MIN_YEAR_VALUE = 1;

    public interface MonthPickerDialogCallback {
        void done(int year, int month);
    }

    public static MonthPickerDialog create(@Nullable final MonthPickerDialogCallback callback) {
        final MonthPickerDialog monthPickerDialog = new MonthPickerDialog();
        monthPickerDialog.callback = callback;
        return monthPickerDialog;
    }

    @Nullable
    private MonthPickerDialogCallback callback;

    @BindView(R.id.numberPickerMonth)
    NumberPicker monthPicker;

    @BindView(R.id.numberPickerYear)
    NumberPicker yearPicker;

    @OnClick(R.id.textViewCancel)
    public void onCancelClicked() {
        dismiss();
    }

    @OnClick(R.id.textViewOk)
    public void onOkClicked() {
        if (callback != null) {
            callback.done(yearPicker.getValue(), monthPicker.getValue());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_month_picker, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Calendar calendar = Calendar.getInstance();

        monthPicker.setMinValue(MIN_MONTH_VALUE);
        monthPicker.setMaxValue(MAX_MONTH_VALUE);

        yearPicker.setMinValue(MIN_YEAR_VALUE);
        yearPicker.setMaxValue(calendar.get(Calendar.YEAR));

        yearPicker.setValue(yearPicker.getMaxValue());
        monthPicker.setValue(calendar.get(Calendar.MONTH) + 1);
    }
}
