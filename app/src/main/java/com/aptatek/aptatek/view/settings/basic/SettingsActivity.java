package com.aptatek.aptatek.view.settings.basic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.settings.reminder.ReminderSettings;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity<SettingsView, SettingsPresenter> {

    @Inject
    SettingsPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_settings;
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
    }

    @OnClick(R.id.textViewDailyReminders)
    public void onTextViewDailyRemindersClicked() {
        final Intent intent = new Intent(this, ReminderSettings.class);
        launchActivity(intent, false, Animation.FADE);
    }
}
