package com.aptatek.aptatek.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<MainActivityView, MainActivityPresenter> implements MainActivityView {

    @Inject
    MainActivityPresenter presenter;

    @BindView(R.id.mainText)
    TextView mainTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter.initView();
    }

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        activityComponent().inject(this);
    }

    @NonNull
    @Override
    public MainActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void mainText(String text) {
        mainTextView.setText(text);
    }
}
