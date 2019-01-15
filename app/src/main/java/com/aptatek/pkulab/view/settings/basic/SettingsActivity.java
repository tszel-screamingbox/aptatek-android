package com.aptatek.pkulab.view.settings.basic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsActivity;
import com.aptatek.pkulab.view.settings.reminder.ReminderSettingsActivity;
import com.aptatek.pkulab.view.settings.web.WebPageActivityStarter;

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

    @BindView(R.id.textViewAppVersion)
    TextView tvAppVersion;

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

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        switchFingerPrint.setOnCheckedChangeListener((buttonView, isChecked) ->
            presenter.setFingerprintEnabled(isChecked)
        );

        presenter.getAppVersion();
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

    @OnClick(R.id.settingsUnitsLabel)
    public void onUnitsClicked() {
        launchActivity(RangeSettingsActivity.starter(this), false, Animation.FADE);
    }

    @OnClick(R.id.textViewPrivacyPolicy)
    public void onPrivacyClicked() {
        WebPageActivityStarter.start(this, getString(R.string.settings_privacy), "http://www.google.com"); // TODO get proper url
    }

    @OnClick(R.id.textViewFaq)
    public void onFaqClicked() {
        WebPageActivityStarter.start(this, getString(R.string.settings_faq), "http://www.google.com"); // TODO get proper url
    }

    @Override
    public void showFingerprintAuthChecked(final boolean isChecked) {
        switchFingerPrint.setChecked(isChecked);
    }

    @Override
    public void showFingerprintAuthEnabled(final boolean isEnabled) {
        switchFingerPrint.setEnabled(isEnabled);
    }

    @Override
    public void showAppVersion(final String version) {
        tvAppVersion.setText(getString(R.string.settings_app_version, version));
    }
}
