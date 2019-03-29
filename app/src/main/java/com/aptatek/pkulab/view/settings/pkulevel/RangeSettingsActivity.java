package com.aptatek.pkulab.view.settings.pkulevel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisions;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.pin.auth.AuthPinHostActivityStarter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aptatek.pkulab.util.Constants.DEFAULT_PKU_INCREASED_CEIL;
import static com.aptatek.pkulab.util.Constants.DEFAULT_PKU_INCREASED_FLOOR;
import static com.aptatek.pkulab.view.base.BaseActivity.Animation.FADE;
import static com.aptatek.pkulab.view.base.BaseActivity.Animation.LEFT_TO_RIGHT;

public class RangeSettingsActivity extends BaseActivity<RangeSettingsView, RangeSettingsPresenter> implements RangeSettingsView {

    private static final String TAG_CONFIRM_DIALOG = "aptatek.settings.range.confirmdialog";
    private static final int AUTH_REQUEST = 101;

    public static Intent starter(final Context context) {
        return new Intent(context, RangeSettingsActivity.class);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rangeSettingsLowDescription)
    TextView tvStandardDescription;
    @BindView(R.id.rangeSettingsHighDescription)
    TextView tvHighDescription;
    @BindView(R.id.rangeSettingsVeryHighDescription)
    TextView tvVeryHighDescription;
    @BindView(R.id.rangeSettingsIncreasedDescription)
    TextView tvIncreasedDescription;
    @BindView(R.id.rangeSettingsUnitsGroup)
    RadioGroup rgUnits;

    private RangeSettingsModel lastModel;

    @Inject
    EditTextPkuLevelLengthFilter floorFilter;

    @Inject
    EditTextPkuLevelLengthFilter ceilFilter;

    @Inject
    RangeSettingsPresenter presenter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_settings);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(R.string.settings_units_title);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rgUnits.setOnCheckedChangeListener((group, checkedId) -> {
            if (lastModel != null && lastModel.getSelectedUnit() != getSelectedUnit()) {
                presenter.changeValues(DEFAULT_PKU_INCREASED_FLOOR, DEFAULT_PKU_INCREASED_CEIL, getSelectedUnit());
            }
        });

        presenter.refresh();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(TAG_CONFIRM_DIALOG) instanceof AlertDialogFragment) {
            return;
        }

        presenter.onBackPressed(getSelectedUnit());
    }

    @Override
    public void finish() {
        super.finish();
        setTransitionAnimation(LEFT_TO_RIGHT);
    }

    @Override
    public void showSaveChangesDialog() {
        final AlertDialogModel model = AlertDialogModel.builder()
                .setCancelable(false)
                .setTitle(getString(R.string.settings_units_confirmation_title))
                .setMessage(getString(R.string.settings_units_confirmation_message))
                .setNegativeButtonText(getString(R.string.settings_units_confirmation_button_cancel))
                .setPositiveButtonText(getString(R.string.settings_units_confirmation_button_save))
                .build();
        final AlertDialogFragment alertDialogFragment = AlertDialogFragment.create(
                model,
                decision -> {
                    if (decision == AlertDialogDecisions.POSITIVE) {
                        requestPin();
                    } else {
                        finish();
                    }
                });
        alertDialogFragment.show(getSupportFragmentManager(), TAG_CONFIRM_DIALOG);
    }

    @Override
    public void showSettingsUpdateMessage() {
        Toast.makeText(this, R.string.settings_message_saved, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
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
    public void displayRangeSettings(final @NonNull RangeSettingsModel model) {
        lastModel = model;

        tvStandardDescription.setText(model.getLowText());
        tvIncreasedDescription.setText(model.getIncreasedText());
        tvHighDescription.setText(model.getHighText());
        tvVeryHighDescription.setText(model.getVeryHighText());


        final int radioButtonId = model.getSelectedUnit() == PkuLevelUnits.MICRO_MOL ? R.id.rangeSettingsUnitMicroMol : R.id.rangeSettingsUnitMilliGram;
        rgUnits.check(radioButtonId);

        floorFilter.setCurrentUnit(model.getSelectedUnit());
        ceilFilter.setCurrentUnit(model.getSelectedUnit());
    }

    @NonNull
    private PkuLevelUnits getSelectedUnit() {
        return rgUnits.getCheckedRadioButtonId() == R.id.rangeSettingsUnitMicroMol ? PkuLevelUnits.MICRO_MOL : PkuLevelUnits.MILLI_GRAM;
    }

    private void requestPin() {
        final Intent intent = AuthPinHostActivityStarter.getIntent(this, true);
        launchActivityForResult(intent, FADE, AUTH_REQUEST);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AUTH_REQUEST) {
            presenter.saveValues(getSelectedUnit());
        }
    }
}
