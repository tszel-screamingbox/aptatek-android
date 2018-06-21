package com.aptatek.aptatek.view.settings.reminder;

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
import com.aptatek.aptatek.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimePickerDialog extends DialogFragment {

    interface TimePickerDialogCallback {
        void done(int hourOfDay, int minute);

        void delete();
    }

    public static TimePickerDialog starter(@NonNull final TimePickerDialogCallback callback) {
        final TimePickerDialog dialog = new TimePickerDialog();
        dialog.callback = callback;
        return dialog;
    }

    public static TimePickerDialog starter(final int hour, final int minute, @NonNull final TimePickerDialogCallback callback) {
        final Bundle bundle = new Bundle();
        bundle.putInt(Constants.TIME_PICKER_DIALOG_HOUR_ARGUMENT_KEY, hour);
        bundle.putInt(Constants.TIME_PCIKER_DIALOG_MINUTE_ARGUMENT_KEY, minute);

        final TimePickerDialog dialog = new TimePickerDialog();
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
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_time_picker, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timePicker.setIs24HourView(false);

        if (getArguments() != null) {
            buttonDelete.setText(R.string.reminder_time_picker_delete);
            timePicker.setCurrentHour(getArguments().getInt(Constants.TIME_PICKER_DIALOG_HOUR_ARGUMENT_KEY));
            timePicker.setCurrentMinute(getArguments().getInt(Constants.TIME_PCIKER_DIALOG_MINUTE_ARGUMENT_KEY));
        } else {
            buttonDelete.setText(R.string.reminder_time_picker_cancel);
        }

        buttonDone.setOnClickListener(v -> {
            if (callback != null && getArguments() == null) {
                callback.done(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                dismiss();
            } else if (callback != null && getArguments() != null) {
                if (getArguments().getInt(Constants.TIME_PICKER_DIALOG_HOUR_ARGUMENT_KEY) != timePicker.getCurrentHour()
                        || getArguments().getInt(Constants.TIME_PCIKER_DIALOG_MINUTE_ARGUMENT_KEY) != timePicker.getCurrentMinute()) {
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
