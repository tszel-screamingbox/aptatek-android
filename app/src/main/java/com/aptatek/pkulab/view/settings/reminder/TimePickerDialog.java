package com.aptatek.pkulab.view.settings.reminder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.ReminderScheduleType;
import com.aptatek.pkulab.widget.CustomRadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimePickerDialog extends DialogFragment {

    private static final String TIME_PICKER_DIALOG_HOUR_ARGUMENT_KEY = "com.aptatek.remider.dialog.hour";
    private static final String TIME_PICKER_DIALOG_MINUTE_ARGUMENT_KEY = "com.aptatek.remider.dialog.minute";
    private static final String TIME_PICKER_DIALOG_REMINDER_SCHEDULE_TYPE_KEY = "com.aptatek.remider.dialog.schedule.type";
    private static final String TIME_PICKER_DIALOG_EDIT_KEY = "com.aptatek.remider.dialog.edit";
    public static final float GUIDELINE_EXPANDED_PERCENT = 0.7f;
    public static final float GUIDELINE_COLLAPSED_PERCENT = 0.5f;
    public static final int ANIMATION_DURATION = 300;
    public static final float ALPHA_MIN = 0f;
    public static final float ALPHA_MAX = 1f;

    interface TimePickerDialogCallback {
        void done(int hourOfDay, int minute, ReminderScheduleType reminderScheduleType);

        void delete();
    }

    public static TimePickerDialog create(@NonNull final TimePickerDialogCallback callback) {
        final TimePickerDialog dialog = new TimePickerDialog();
        dialog.callback = callback;
        return dialog;
    }

    public static TimePickerDialog createForEdit(final int hour,
                                                 final int minute,
                                                 final ReminderScheduleType reminderScheduleType,
                                                 @NonNull final TimePickerDialogCallback callback) {
        final Bundle bundle = new Bundle();
        bundle.putInt(TIME_PICKER_DIALOG_HOUR_ARGUMENT_KEY, hour);
        bundle.putInt(TIME_PICKER_DIALOG_MINUTE_ARGUMENT_KEY, minute);
        bundle.putBoolean(TIME_PICKER_DIALOG_EDIT_KEY, true);
        bundle.putSerializable(TIME_PICKER_DIALOG_REMINDER_SCHEDULE_TYPE_KEY, reminderScheduleType);

        final TimePickerDialog dialog = new TimePickerDialog();
        dialog.setArguments(bundle);
        dialog.callback = callback;
        return dialog;
    }

    @BindView(R.id.timePicker)
    TimePicker timePicker;

    @BindView(R.id.layoutDone)
    FrameLayout layoutDone;

    @BindView(R.id.layoutDelete)
    FrameLayout layoutDelete;

    @BindView(R.id.textViewDone)
    TextView textViewDone;

    @BindView(R.id.textViewDelete)
    TextView textViewDelete;

    @BindView(R.id.textViewConfirm)
    TextView textViewConfirm;

    @BindView(R.id.textViewCancel)
    TextView textViewCancel;

    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.radioGroupSchedule)
    CustomRadioGroup radioGroupSchedule;

    @Nullable
    private TimePickerDialogCallback callback;

    private final ValueAnimator anim = new ValueAnimator();


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
            textViewDelete.setText(R.string.reminder_time_picker_delete);
            textViewDone.setText(getArguments().getBoolean(TIME_PICKER_DIALOG_EDIT_KEY, false)
                    ? R.string.reminder_time_picker_update
                    : R.string.reminder_time_picker_add);

            timePicker.setCurrentHour(getArguments().getInt(TIME_PICKER_DIALOG_HOUR_ARGUMENT_KEY));
            timePicker.setCurrentMinute(getArguments().getInt(TIME_PICKER_DIALOG_MINUTE_ARGUMENT_KEY));
            radioGroupSchedule.check(getSelectedRadioGroup());
        } else {
            textViewDelete.setText(R.string.reminder_time_picker_cancel);
        }

        layoutDone.setOnClickListener(v -> {
            if (callback != null) {
                if (getArguments() == null) {
                    callback.done(timePicker.getCurrentHour(), timePicker.getCurrentMinute(), getSelectedReminderScheduleType());
                    dismiss();
                } else if (getArguments() != null) {
                    if (textViewCancel.getVisibility() == View.VISIBLE && textViewConfirm.getVisibility() == View.VISIBLE) {
                        animateGuideline(GUIDELINE_COLLAPSED_PERCENT);

                        animateDoneButtonBackgroundColor(R.color.applicationLightGray, R.color.applicationGreen);

                        showAnimation(textViewDone);
                        showAnimation(textViewDelete);
                        hideAnimation(textViewCancel);
                        hideAnimation(textViewConfirm);
                    } else {
                        if (getArguments().getInt(TIME_PICKER_DIALOG_HOUR_ARGUMENT_KEY) != timePicker.getCurrentHour()
                                || getArguments().getInt(TIME_PICKER_DIALOG_MINUTE_ARGUMENT_KEY) != timePicker.getCurrentMinute()
                                || radioGroupSchedule.getCheckedRadioButtonId() != getSelectedRadioGroup()) {
                            callback.done(timePicker.getCurrentHour(), timePicker.getCurrentMinute(), getSelectedReminderScheduleType());
                        }
                        dismiss();
                    }
                }
            }
        });

        layoutDelete.setOnClickListener(v -> {
            if (getArguments() != null) {
                if (textViewCancel.getVisibility() == View.VISIBLE && textViewConfirm.getVisibility() == View.VISIBLE) {
                    if (callback != null) {
                        callback.delete();
                        dismiss();
                    }
                } else {
                    animateGuideline(GUIDELINE_EXPANDED_PERCENT);

                    animateDoneButtonBackgroundColor(R.color.applicationGreen, R.color.applicationLightGray);

                    showAnimation(textViewCancel);
                    showAnimation(textViewConfirm);
                    hideAnimation(textViewDone);
                    hideAnimation(textViewDelete);
                }
            } else {
                dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        textViewCancel.clearAnimation();
        textViewConfirm.clearAnimation();
        textViewDone.clearAnimation();
        textViewDelete.clearAnimation();
        anim.removeAllUpdateListeners();
        super.onDestroyView();
    }

    private void animateDoneButtonBackgroundColor(@ColorRes final int applicationGreen, @ColorRes final int applicationLightGray) {
        anim.setIntValues(
                ContextCompat.getColor(requireContext(), applicationGreen),
                ContextCompat.getColor(requireContext(), applicationLightGray));
        anim.setEvaluator(new ArgbEvaluator());
        anim.removeAllUpdateListeners();
        anim.addUpdateListener(valueAnimator -> layoutDone.setBackgroundColor((Integer) valueAnimator.getAnimatedValue()));
        anim.setDuration(ANIMATION_DURATION);
        anim.start();
    }

    private void animateGuideline(final float percent) {
        final ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.setGuidelinePercent(R.id.guideline2, percent);
        TransitionManager.beginDelayedTransition(constraintLayout);
        constraintSet.applyTo(constraintLayout);
    }

    private void showAnimation(final View view) {
        view.animate()
                .alpha(ALPHA_MAX)
                .setDuration(ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void hideAnimation(final View view) {
        view.animate()
                .alpha(ALPHA_MIN)
                .setDuration(ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });
    }

    private ReminderScheduleType getSelectedReminderScheduleType() {
        if (radioGroupSchedule.getCheckedRadioButtonId() == R.id.radioButtonWeekly) {
            return ReminderScheduleType.WEEKLY;
        } else if (radioGroupSchedule.getCheckedRadioButtonId() == R.id.radioButtonMonthly) {
            return ReminderScheduleType.MONTHLY;
        } else {
            return ReminderScheduleType.BIWEEKLY;
        }
    }

    private int getSelectedRadioGroup() {
        final ReminderScheduleType type = (ReminderScheduleType) getArguments().getSerializable(TIME_PICKER_DIALOG_REMINDER_SCHEDULE_TYPE_KEY);
        if (type == ReminderScheduleType.WEEKLY) {
            return R.id.radioButtonWeekly;
        } else if (type == ReminderScheduleType.MONTHLY) {
            return R.id.radioButtonMonthly;
        } else {
            return R.id.radioButtonBiWeekly;
        }
    }
}
