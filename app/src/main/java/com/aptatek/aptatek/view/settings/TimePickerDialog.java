package com.aptatek.aptatek.view.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.aptatek.aptatek.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimePickerDialog extends DialogFragment {

    private static final String hourArgumentKey = "hour";
    private static final String minuteArgumentKey = "minute";

    interface TimePickerDialogCallback {
        void done(int hourOfDay, int minute);

        void delete();
    }

    public static TimePickerDialog starter(@NonNull TimePickerDialogCallback callback) {
        TimePickerDialog dialog = new TimePickerDialog();
        dialog.callback = callback;
        return dialog;
    }

    public static TimePickerDialog starter(int hour, int minute, @NonNull TimePickerDialogCallback callback) {
        Bundle bundle = new Bundle();
        bundle.putInt(hourArgumentKey, hour);
        bundle.putInt(minuteArgumentKey, minute);

        TimePickerDialog dialog = new TimePickerDialog();
        dialog.setArguments(bundle);
        dialog.callback = callback;
        return dialog;
    }

    @BindView(R.id.timePicker)
    TimePicker timePicker;

    @BindView(R.id.buttonDone)
    Button buttonDone;

    @BindView(R.id.buttonDelete)
    Button buttonDelete;

    @Nullable
    private TimePickerDialogCallback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time_picker, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timePicker.setIs24HourView(false);

        if (getArguments() != null) {
            buttonDelete.setText(R.string.reminder_time_picker_delete);
            timePicker.setCurrentHour(getArguments().getInt(hourArgumentKey));
            timePicker.setCurrentMinute(getArguments().getInt(minuteArgumentKey));
        } else {
            buttonDelete.setText(R.string.reminder_time_picker_cancel);
        }

        buttonDone.setOnClickListener(v -> {
            if (callback != null && getArguments() == null) {
                callback.done(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                dismiss();
            } else if (callback != null && getArguments() != null) {
                if (getArguments().getInt(hourArgumentKey) != timePicker.getCurrentHour()
                        || getArguments().getInt(minuteArgumentKey) != timePicker.getCurrentMinute()) {
                    callback.done(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                }
                dismiss();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            if (callback != null && getArguments() == null) {
                dismiss();
            } else if (callback != null && getArguments() != null) {
                callback.delete();
                dismiss();
            }
        });
    }
}
