package com.aptatek.aptatek.view.toggle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToggleActivity extends BaseActivity<ToggleActivityView, ToggleActivityPresenter> implements ToggleActivityView {

    @Inject
    ToggleActivityPresenter presenter;

    @BindView(R.id.newTestButton)
    Button newTestButton;

    @BindView(R.id.connectCubeButton)
    Button connectCubeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle);
        ButterKnife.bind(this);

        presenter.initView();
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

    @OnClick(R.id.newTestButton)
    public void onNewTestButtonClicked() {
        Toast.makeText(this, "New test clicked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.connectCubeButton)
    public void onConnectButtonClicked() {
        Toast.makeText(this, "Connect clicked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.resultsButton)
    public void onResultButtonClicked() {
        Toast.makeText(this, "Result clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cubeAvailable() {
        connectCubeButton.setVisibility(View.GONE);
        newTestButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void cubeNotAvailable() {
        connectCubeButton.setVisibility(View.VISIBLE);
        newTestButton.setVisibility(View.GONE);
    }
}