package com.aptatek.pkulab.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.main.home.HomeFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainHostActivity extends BaseActivity<MainHostActivityView, MainHostActivityPresenter> implements MainHostActivityView {


    @Inject
    MainHostActivityPresenter presenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        switchToFragment(new HomeFragment());
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @NonNull
    @Override
    public MainHostActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.mainFrame;
    }
}
