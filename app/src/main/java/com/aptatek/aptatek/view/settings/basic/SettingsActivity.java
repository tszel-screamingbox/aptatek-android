package com.aptatek.aptatek.view.settings.basic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.settings.reminder.ReminderSettingsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity<SettingsView, SettingsPresenter> implements SettingsView {

    public static Intent starter(@NonNull final Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Inject
    SettingsPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.switchFingerprint)
    SwitchCompat switchFingerPrint;

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @NonNull
    @Override
    public SettingsPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        switchFingerPrint.setOnCheckedChangeListener((buttonView, isChecked) ->
            presenter.setFingerprintEnabled(isChecked)
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.checkFingerprintSettings();
    }

    @OnClick(R.id.textViewDailyReminders)
    public void onTextViewDailyRemindersClicked() {
        launchActivity(ReminderSettingsActivity.starter(this), false, Animation.FADE);
    }

    @Override
    public void showFingerprintAuthChecked(final boolean isChecked) {
        switchFingerPrint.setChecked(isChecked);
    }

    @Override
    public void showFingerprintAuthEnabled(final boolean isEnabled) {
        switchFingerPrint.setEnabled(isEnabled);
    }
}
