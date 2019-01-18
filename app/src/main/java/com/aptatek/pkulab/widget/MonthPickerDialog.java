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

public class MonthPickerDialog extends DialogFragment {

    public static MonthPickerDialog create() {
        return new MonthPickerDialog();
    }

    @BindView(R.id.numberPickerMonth)
    NumberPicker monthPicker;

    @BindView(R.id.numberPickerYear)
    NumberPicker yearPicker;

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

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        yearPicker.setMinValue(1);
        yearPicker.setMaxValue(Calendar.getInstance().get(Calendar.YEAR));
    }
}
