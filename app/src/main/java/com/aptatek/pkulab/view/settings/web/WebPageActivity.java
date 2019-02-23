package com.aptatek.pkulab.view.settings.web;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.settings.reminder.ReminderSettingsPresenter;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebPageActivity extends BaseActivity<WebPageView, WebPagePresenter> implements WebPageView {

    @Inject
    WebPagePresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.webView)
    WebView webView;

    @Arg
    String title;

    @Arg
    String url;

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @NonNull
    @Override
    public WebPagePresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this, savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        webView.setWebViewClient(new WebViewClient());
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @OnClick(R.id.btnReport)
    public void onReportButtonClicked() {
        ReportIssueDialog.create().show(getSupportFragmentManager(), "");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
