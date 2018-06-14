package com.aptatek.aptatek.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.settings.ReminderSettingsActivity;
import com.aptatek.aptatek.view.toggle.ToggleActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainActivityView, MainActivityPresenter> implements MainActivityView {

    @Inject
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @NonNull
    @Override
    public MainActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.toggleButton)
    public void onToggleButtonClicked() {
        final Intent intent = new Intent(this, ToggleActivity.class);
        launchActivity(intent, false, Animation.FADE);
    }

    @OnClick(R.id.newTestButton)
    public void onNewTestButtonClicked() {
        Toast.makeText(this, "New test should load", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.settingsButton)
    public void onSettingsButtonClicked() {
        final Intent intent = new Intent(this, ReminderSettingsActivity.class);
        launchActivity(intent, false, Animation.FADE);
    }
}
