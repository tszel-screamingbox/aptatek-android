package com.aptatek.aptatek.view.toggle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToggleActivity extends BaseActivity<ToggleActivityView, ToggleActivityPresenter> implements ToggleActivityView {

    @Inject
    ToggleActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle);
        ButterKnife.bind(this);
    }

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        activityComponent().inject(this);
    }

    @NonNull
    @Override
    public ToggleActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_main;
    }


    @OnClick(R.id.toggleButton)
    public void onToggleButtonClicked() {
        onBackPressed();
        setTransitionAnimation(Animation.FADE);
    }

    @OnClick(R.id.settingsButton)
    public void onSettingsButtonClicked() {
        Toast.makeText(this, "Settings should load", Toast.LENGTH_SHORT).show();
    }
}