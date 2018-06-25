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

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimePickerDialog extends DialogFragment {

    private static final String TIME_PICKER_DIALOG_HOUR_ARGUMENT_KEY = "com.aptatek.remider.dialog.hour";
    private static final String TIME_PICKER_DIALOG_MINUTE_ARGUMENT_KEY = "com.aptatek.remider.dialog.minute";
    private static final String TIME_PICKER_DIALOG_EDIT_KEY = "com.aptatek.remider.dialog.edit";

    interface TimePickerDialogCallback {
        void done(int hourOfDay, int minute);

        void delete();
    }

    public static TimePickerDialog create(@NonNull final TimePickerDialogCallback callback) {
        final TimePickerDialog dialog = new TimePickerDialog();
        dialog.callback = callback;
        return dialog;
    }

    public static TimePickerDialog createForEdit(final int hour, final int minute, @NonNull final TimePickerDialogCallback callback) {
        final Bundle bundle = new Bundle();
        bundle.putInt(TIME_PICKER_DIALOG_HOUR_ARGUMENT_KEY, hour);
        bundle.putInt(TIME_PICKER_DIALOG_MINUTE_ARGUMENT_KEY, minute);
        bundle.putBoolean(TIME_PICKER_DIALOG_EDIT_KEY, true);

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
            buttonDone.setText(getArguments().getBoolean(TIME_PICKER_DIALOG_EDIT_KEY, false)
                    ? R.string.reminder_time_picker_update
                    : R.string.reminder_time_picker_add);

            timePicker.setCurrentHour(getArguments().getInt(TIME_PICKER_DIALOG_HOUR_ARGUMENT_KEY));
            timePicker.setCurrentMinute(getArguments().getInt(TIME_PICKER_DIALOG_MINUTE_ARGUMENT_KEY));
        } else {
            buttonDelete.setText(R.string.reminder_time_picker_cancel);
        }

        buttonDone.setOnClickListener(v -> {
            // TODO this IF could be simpler: move callback != null outside
            if (callback != null && getArguments() == null) {
                callback.done(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                dismiss();
            } else if (callback != null && getArguments() != null) {
                if (getArguments().getInt(TIME_PICKER_DIALOG_HOUR_ARGUMENT_KEY) != timePicker.getCurrentHour()
                        || getArguments().getInt(TIME_PICKER_DIALOG_MINUTE_ARGUMENT_KEY) != timePicker.getCurrentMinute()) {
                    callback.done(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                }
                dismiss();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            // TODO this IF could be simpler: move callback != null outside
            if (callback != null && getArguments() == null) {
                dismiss();
            } else if (callback != null && getArguments() != null) {
                callback.delete();
                dismiss();
            }
        });
    }
}
