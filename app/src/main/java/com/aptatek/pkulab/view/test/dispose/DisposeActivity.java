package com.aptatek.pkulab.view.test.dispose;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.main.MainHostActivity;
import com.aptatek.pkulab.widget.HeaderView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DisposeActivity extends BaseActivity<DisposeView, DisposePresenter> implements DisposeView {

    @Inject
    DisposePresenter presenter;

    @BindView(R.id.dispose_header)
    HeaderView headerView;

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

        headerView.setSubtitleSpanned(Html.fromHtml(getString(R.string.dispose_message)));
    }


    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @OnClick(R.id.dispose_done)
    public void onClickDone() {
        presenter.done();
    }

    @Override
    public void doneFinished() {
        final Intent intent = new Intent(this, MainHostActivity.class);
        launchActivity(intent, true, Animation.RIGHT_TO_LEFT);
    }
}
