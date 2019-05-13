package com.aptatek.pkulab.widget;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.MonthPickerDialogModel;

import org.w3c.dom.Text;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MonthPickerDialog extends DialogFragment {

    private static final String KEY_MODEL = "com.aptatek.month.picker.dialog.model";
    private static final int MAX_MONTH_VALUE = 12;

    public interface MonthPickerDialogCallback {
        void onPick(int year, int month);
    }

    public static MonthPickerDialog create(@NonNull final MonthPickerDialogModel monthPickerDialogModel, @Nullable final MonthPickerDialogCallback callback) {
        final MonthPickerDialog monthPickerDialog = new MonthPickerDialog();
        monthPickerDialog.callback = callback;
        final Bundle args = new Bundle();
        args.putParcelable(KEY_MODEL, monthPickerDialogModel);
        monthPickerDialog.setArguments(args);
        return monthPickerDialog;
    }

    @Nullable
    private MonthPickerDialogCallback callback;

    @BindView(R.id.numberPickerMonth)
    NumberPicker monthPicker;

    @BindView(R.id.numberPickerYear)
    NumberPicker yearPicker;

    @BindView(R.id.textViewError)
    TextView error;

    @OnClick(R.id.textViewCancel)
    public void onCancelClicked() {
        dismiss();
    }

    @OnClick(R.id.textViewOk)
    public void onOkClicked() {
        if (callback != null) {
            callback.onPick(yearPicker.getValue(), monthPicker.getValue());
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

        final MonthPickerDialogModel monthPickerDialogModel;

        if (getArguments() != null) {
            monthPickerDialogModel = getArguments().getParcelable(KEY_MODEL);
        } else {
            monthPickerDialogModel = null;
        }

        if (monthPickerDialogModel != null) {
            monthPicker.setMinValue(1);
            yearPicker.setMinValue(monthPickerDialogModel.getMinYear());
        }

        final Calendar calendar = Calendar.getInstance();

        monthPicker.setMaxValue(MAX_MONTH_VALUE);
        yearPicker.setMaxValue(calendar.get(Calendar.YEAR));

        yearPicker.setValue(yearPicker.getMaxValue());
        monthPicker.setValue(calendar.get(Calendar.MONTH) + 1);
    }

    public void testNotFound() {
        error.setVisibility(View.VISIBLE);
    }
}
