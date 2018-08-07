package com.aptatek.pkuapp.view.toggle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.view.base.BaseActivity;

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

    @BindView(R.id.statusText)
    TextView statusTextView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle);
        ButterKnife.bind(this);

        presenter.initView();
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @NonNull
    @Override
    public ToggleActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_toggle;
    }


    @OnClick(R.id.toggleButton)
    public void onToggleButtonClicked() {
        onBackPressed();
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
        statusTextView.setText(getString(R.string.toggle_cube_connected));
        statusTextView.setTextColor(getResources().getColor(R.color.applicationGreen));
    }

    @Override
    public void cubeNotAvailable() {
        connectCubeButton.setVisibility(View.VISIBLE);
        newTestButton.setVisibility(View.GONE);
        statusTextView.setText(getString(R.string.toggle_cube_disconnected));
        statusTextView.setTextColor(getResources().getColor(R.color.applicationPink));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setTransitionAnimation(Animation.FADE);
    }
}
