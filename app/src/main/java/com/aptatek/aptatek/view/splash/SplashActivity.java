package com.aptatek.aptatek.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.main.MainActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity<SplashActivityView, SplashActivityPresenter> implements SplashActivityView {

    @Inject
    SplashActivityPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        presenter.initView();
    }

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        activityComponent().inject(this);
    }

    @NonNull
    @Override
    public SplashActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void mainActivityShouldLoad() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
