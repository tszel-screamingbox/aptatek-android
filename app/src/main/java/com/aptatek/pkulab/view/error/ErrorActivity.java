package com.aptatek.pkulab.view.error;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.test.dispose.DisposeActivity;
import com.aptatek.pkulab.widget.HeaderView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ErrorActivity extends BaseActivity<ErrorView, ErrorPresenter> implements ErrorView {

    @Inject
    ErrorPresenter presenter;

    @NonNull
    @Override
    public ErrorPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @BindView(R.id.error_header)
    HeaderView headerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_error);

        ButterKnife.bind(this);

        final ErrorModel model = getModel();
        if (model != null) {
            headerView.setTitle(model.getTitle());
            headerView.setSubtitle(model.getMessage());
        }
    }

    public static Intent starter(@NonNull Context context, @NonNull final ErrorModel errorModel) {
        final Intent intent = new Intent(context, ErrorActivity.class);
        final Bundle extras = new Bundle();
        extras.putParcelable("model", errorModel);
        intent.putExtras(extras);
        return intent;
    }

    @Nullable
    private ErrorModel getModel() {
        return getIntent().getParcelableExtra("model");
    }


    @OnClick(R.id.error_done)
    public void onClickDone() {
        presenter.done();
    }

    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @Override
    public void doneFinished() {
        finish();
        final Intent intent = new Intent(this, DisposeActivity.class);
        launchActivity(intent, true, Animation.RIGHT_TO_LEFT);
    }

}
