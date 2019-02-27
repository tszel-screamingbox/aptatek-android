package com.aptatek.pkulab.view.test.dispose;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.main.MainHostActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DisposeActivity extends BaseActivity<DisposeView, DisposePresenter> {

    @Inject
    DisposePresenter presenter;

    @NonNull
    @Override
    public DisposePresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispose);

        ButterKnife.bind(this);
    }


    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @OnClick(R.id.dispose_done)
    public void onClickDone() {
        final Intent intent = new Intent(this, MainHostActivity.class);
        launchActivity(intent, true, Animation.RIGHT_TO_LEFT);
    }
}
