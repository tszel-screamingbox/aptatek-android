package com.aptatek.pkulab.view.settings.basic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.main.MainHostActivity;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsActivity;
import com.aptatek.pkulab.view.settings.reminder.ReminderSettingsActivity;
import com.aptatek.pkulab.view.settings.web.WebPageActivityStarter;
import com.aptatek.pkulab.widget.HeaderView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SettingsActivity extends BaseActivity<SettingsView, SettingsPresenter> implements SettingsView {

    public static Intent starter(@NonNull final Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Inject
    SettingsPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.header)
    HeaderView tvAppVersion;

    @BindView(R.id.settings_items)
    RecyclerView recyclerView;

    @Inject
    SettingsItemAdapter settingsItemAdapter;

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @NonNull
    @Override
    public SettingsPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        settingsItemAdapter.setSettingsItemClickListener(new SettingsItemAdapter.SettingsItemClickListener() {
            @Override
            public void onFingerprintAuthToggled(final boolean isChecked) {
                presenter.setFingerprintEnabled(isChecked);
            }

            @Override
            public void onSettingsItemClicked(final SettingsItem item) {
                switch (item) {
                    case DAILY_REMINDERS:
                        launchActivity(ReminderSettingsActivity.starter(SettingsActivity.this));
                        break;
                    case PHE_PREFERENCES:
                        launchActivity(RangeSettingsActivity.starter(SettingsActivity.this));
                        break;
                    case HELP:
                        launchActivity(WebPageActivityStarter.getIntent(SettingsActivity.this, getString(R.string.settings_help), "https://pkulab.webflow.io/help"));
                        break;
                    case PRIVACY_POLICY:
                        launchActivity(WebPageActivityStarter.getIntent(SettingsActivity.this, getString(R.string.settings_privacy), "https://pkulab.webflow.io/privacy"));
                        break;
                    default:
                        Timber.d("Unhandled settings item clicked: %s", item);
                        break;
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(settingsItemAdapter);

        presenter.getAppVersion();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final Intent intent = new Intent(this, MainHostActivity.class);
        launchActivity(intent, true, Animation.FADE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.checkFingerprintSettings();
    }

    @Override
    public void updateFingerprintSetting(boolean isEnabled, boolean isChecked) {
        settingsItemAdapter.updateFingerprintItem(isEnabled, isChecked);
    }

    @Override
    public void showAppVersion(final String version) {
        tvAppVersion.setSubtitle(getString(R.string.settings_app_version, version));
    }
}
