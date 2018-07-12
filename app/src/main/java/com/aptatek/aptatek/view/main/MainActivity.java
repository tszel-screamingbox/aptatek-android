package com.aptatek.aptatek.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.rangeinfo.RangeInfoActivity;
import com.aptatek.aptatek.view.settings.basic.SettingsActivity;
import com.aptatek.aptatek.view.test.TestActivity;

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
        activityComponent.inject(this);
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
        // final Intent intent = new Intent(this, ToggleActivity.class);
        // launchActivity(intent, false, Animation.FADE);

        launchActivity(RangeInfoActivity.starter(this), false, Animation.FADE);
    }

    @OnClick(R.id.newTestButton)
    public void onNewTestButtonClicked() {
        launchActivity(TestActivity.createStarter(this), false, Animation.FADE);
    }

    @OnClick(R.id.settingsButton)
    public void onSettingsButtonClicked() {
        launchActivity(SettingsActivity.starter(this), false, Animation.FADE);
    }
}
