package com.aptatek.aptatek.view.settings.pkulevel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.model.AlertDialogModel;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.dialog.AlertDialogDecisions;
import com.aptatek.aptatek.view.dialog.AlertDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RangeSettingsActivity extends BaseActivity<RangeSettingsView, RangeSettingsPresenter> implements RangeSettingsView {

    private static final String TAG_CONFIRM_DIALOG = "aptatek.settings.range.confirmdialog";

    public static Intent starter(Context context) {
        return new Intent(context, RangeSettingsActivity.class);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rangeSettingsLowDescription)
    TextView tvLowDescription;
    @BindView(R.id.rangeSettingsHighDescription)
    TextView tvHighDescription;
    @BindView(R.id.rangeSettingsVeryHighDescription)
    TextView tvVeryHighDescription;
    @BindView(R.id.rangeSettingsUnitsGroup)
    RadioGroup rgUnits;

    @Inject
    RangeSettingsPresenter presenter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_settings);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle(R.string.settings_units_title);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rgUnits.setOnCheckedChangeListener((group, checkedId) ->
            presenter.changeUnit(getSelectedUnit())
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.refresh();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(TAG_CONFIRM_DIALOG) instanceof AlertDialogFragment) {
            return;
        }

        final AlertDialogFragment alertDialogFragment = AlertDialogFragment.create(
                AlertDialogModel.create(getString(R.string.settings_units_confirmation_title), getString(R.string.settings_units_confirmation_message)),
                decision -> {
                    if (decision == AlertDialogDecisions.POSITIVE) {
                        // TODO normal range UI + read values
                        presenter.saveNormalRange(100f, 250f, getSelectedUnit());
                    } else {
                        finish();
                    }
                });
        alertDialogFragment.show(getSupportFragmentManager(), TAG_CONFIRM_DIALOG);
    }

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        activityComponent.plus(new RangeInfoModule())
                .inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return 0;
    }

    @NonNull
    @Override
    public RangeSettingsPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void displayRangeSettings(@NonNull RangeSettingsModel model) {
        tvLowDescription.setText(model.getLowText());
        tvHighDescription.setText(model.getHighText());
        tvVeryHighDescription.setText(model.getVeryHighText());
        rgUnits.check(model.getSelectedUnit() == PkuLevelUnits.MICRO_MOL ? R.id.rangeSettingsUnitMicroMol : R.id.rangeSettingsUnitMilliGram);
    }

    @NonNull
    private PkuLevelUnits getSelectedUnit() {
        return rgUnits.getCheckedRadioButtonId() == R.id.rangeSettingsUnitMicroMol ? PkuLevelUnits.MICRO_MOL : PkuLevelUnits.MILLI_GRAM;
    }
}
