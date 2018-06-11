package com.aptatek.aptatek.view.fingerprint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FingerprintActivity extends BaseActivity<FingerprintActivityView, FingerprintActivityPresenter> implements FingerprintActivityView {

    @Inject
    FingerprintActivityPresenter presenter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        ButterKnife.bind(this);
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @NonNull
    @Override
    public FingerprintActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_fingerprint;
    }


    @OnClick(R.id.disableButton)
    public void onEnableButtonClicked() {
        Toast.makeText(this, "Enable clicked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.disableButton)
    public void onDisableButtonClicked() {
        Toast.makeText(this, "Disable clicked", Toast.LENGTH_SHORT).show();
    }
}
