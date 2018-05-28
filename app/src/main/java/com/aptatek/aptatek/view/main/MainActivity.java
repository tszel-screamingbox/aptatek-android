package com.aptatek.aptatek.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainActivityView, MainActivityPresenter> implements MainActivityView {

    @Inject
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        activityComponent().inject(this);
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
        Toast.makeText(this, "Toggle should load", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.newTestButton)
    public void onNewTestButtonClicked() {
        Toast.makeText(this, "New test should load", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.settingsButton)
    public void onSettingsButtonClicked() {
        Toast.makeText(this, "Settings should load", Toast.LENGTH_SHORT).show();
    }

}
