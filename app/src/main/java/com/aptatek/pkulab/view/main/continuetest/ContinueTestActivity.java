package com.aptatek.pkulab.view.main.continuetest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;

import javax.inject.Inject;

public class ContinueTestActivity extends BaseActivity<ContinueTestView, ContinueTestPresenter>
        implements ContinueTestView {

    public static final int CONTINUE_TEST_ACTIVITY_REQUEST_CODE = 9921;

    public static Intent starter(final Context context) {
        return new Intent(context, ContinueTestActivity.class);
    }

    @Inject
    ContinueTestPresenter presenter;

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.container;
    }

    @NonNull
    @Override
    public ContinueTestPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_content_frame);
        switchToFragment(new TurnReaderOnContinueTestFragment());
    }
}
