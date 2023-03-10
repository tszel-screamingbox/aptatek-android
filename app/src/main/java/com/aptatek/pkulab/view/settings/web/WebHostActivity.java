package com.aptatek.pkulab.view.settings.web;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.settings.web.fragment.WebPageFragmentStarter;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.ButterKnife;
import timber.log.Timber;

public class WebHostActivity extends BaseActivity<WebHostView, WebHostPresenter> implements WebHostView {

    @Inject
    WebHostPresenter presenter;

    @Arg
    String title;

    @Arg
    String url;

    @Arg
    Boolean reportVisible;

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this, savedInstanceState);
        setContentView(R.layout.layout_content_frame);
        ButterKnife.bind(this);

        Timber.d("URL: %s", url);
        presenter.initFaqAnalytics(url.equals(Constants.URL_HELP));
        presenter.initPrivacyAnalytics(url.equals(Constants.URL_PRIVACY));
        switchToFragment(WebPageFragmentStarter.newInstance(title, url, reportVisible));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.container;
    }

    @NonNull
    @Override
    public WebHostPresenter createPresenter() {
        return presenter;
    }
}
