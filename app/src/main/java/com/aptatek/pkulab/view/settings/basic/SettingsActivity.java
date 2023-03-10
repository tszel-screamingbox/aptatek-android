package com.aptatek.pkulab.view.settings.basic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.main.MainHostActivity;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsActivity;
import com.aptatek.pkulab.view.settings.reminder.ReminderSettingsActivity;
import com.aptatek.pkulab.view.settings.web.WebHostActivityStarter;
import com.aptatek.pkulab.widget.HeaderView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SettingsActivity extends BaseActivity<SettingsView, SettingsPresenter> implements SettingsView {

    private static final String TAG_NETWORK_DIALOG = "aptatek.settings.network.dialog";

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

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
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
                        showScreen(WebHostActivityStarter.getIntent(SettingsActivity.this, getString(R.string.settings_help), Constants.URL_HELP, true));
                        break;
                    case PRIVACY_POLICY:
                        showScreen(WebHostActivityStarter.getIntent(SettingsActivity.this, getString(R.string.settings_privacy), Constants.URL_PRIVACY, false));
                        break;
                    case TERMS_AND_CONDITIONS:
                        showScreen(WebHostActivityStarter.getIntent(SettingsActivity.this, getString(R.string.settings_terms), Constants.URL_TERMS, false));
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

    private void showScreen(final Intent intent) {
        if (presenter.isNetworkAvailable()) {
            launchActivity(intent);
        } else {
            final AlertDialogModel model = AlertDialogModel.builder()
                    .setCancelable(true)
                    .setTitle(getString(R.string.settings_network_title))
                    .setMessage(getString(R.string.settings_network_message))
                    .setPositiveButtonText(getString(R.string.alertdialog_button_ok))
                    .build();
            final AlertDialogFragment alertDialogFragment = AlertDialogFragment.create(model, null);
            alertDialogFragment.show(getSupportFragmentManager(), TAG_NETWORK_DIALOG);
        }
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
    public void updateFingerprintSetting(final boolean isEnabled, final boolean isChecked) {
        settingsItemAdapter.updateFingerprintItem(isEnabled, isChecked);
    }

    @Override
    public void showAppVersion(final String version) {
        tvAppVersion.setSubtitle(getString(R.string.settings_app_version, version));
    }

    @Override
    public void populateAdapter(List<SettingsAdapterItem> data) {
        settingsItemAdapter.setData(data);
    }
}
