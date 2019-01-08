package com.aptatek.pkulab.view.settings.web;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aptatek.pkulab.R;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WebPageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;

    @Arg
    String title;
    @Arg
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this, savedInstanceState);

        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setSupportActionBar(toolbar);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
