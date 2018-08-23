package com.aptatek.pkuapp.view.settings.pkulevel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkuapp.domain.model.AlertDialogModel;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuLevelUnits;
import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.util.Constants;
import com.aptatek.pkuapp.view.base.BaseActivity;
import com.aptatek.pkuapp.view.dialog.AlertDialogDecisions;
import com.aptatek.pkuapp.view.dialog.AlertDialogFragment;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
import timber.log.Timber;

public class RangeSettingsActivity extends BaseActivity<RangeSettingsView, RangeSettingsPresenter> implements RangeSettingsView {

    private static final String TAG_CONFIRM_DIALOG = "aptatek.settings.range.confirmdialog";
    private static final long DEBOUNCE = 500L;

    public static Intent starter(final Context context) {
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
    @BindView(R.id.rangeSettingsNormalFloor)
    EditText etNormalFloor;
    @BindView(R.id.rangeSettingsNormalCeil)
    EditText etNormalCeil;
    @BindView(R.id.rangeSettingsRangeBar)
    RangeBar rbRange;
    @BindView(R.id.rangeSettingsUnitsGroup)
    RadioGroup rgUnits;

    private final FlowableProcessor<Object> changeProcessor = BehaviorProcessor.create();
    private Disposable disposable;

    private boolean rangeSet;
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
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle(R.string.settings_units_title);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rgUnits.setOnCheckedChangeListener((group, checkedId) -> {
            if (lastModel != null && lastModel.getSelectedUnit() != getSelectedUnit()) {
                changeProcessor.onNext(new Object());
            }
        });

        rbRange.setPinTextListener((rangeBar, tickIndex) ->
            presenter.formatValue(PkuLevelConverter.convertTo(PkuLevel.create(getValueFromRangeBar(tickIndex), PkuLevelUnits.MICRO_MOL), getSelectedUnit()))
        );

        rbRange.setOnRangeBarChangeListener((rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue) -> {
            // called when the range bar's values change.
            // we convert MicoMol values from the pin indexes, update the editTexts, and emit a signal to update other texts on screen.
            Timber.d("rangeBarChange: " + leftPinIndex + ", " + rightPinIndex);

            final float mmolFloor = getValueFromRangeBar(leftPinIndex);
            final float mmolCeil = getValueFromRangeBar(rightPinIndex);

            etNormalFloor.setText(leftPinValue);
            etNormalCeil.setText(rightPinValue);

            if (rangeSet && lastModel != null
                    && (Math.abs(lastModel.getNormalFloorMMolValue() - mmolFloor) > Constants.FLOAT_COMPARISION_ERROR_MARGIN
                    || Math.abs(lastModel.getNormalCeilMMolValue() - mmolCeil) > Constants.FLOAT_COMPARISION_ERROR_MARGIN)) {

                changeProcessor.onNext(new Object());
            }
        });
        rbRange.setDrawTicks(false);

        etNormalFloor.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                final float newValue = Float.parseFloat(v.getText().toString());
                final float mmolValue = PkuLevelConverter.convertTo(PkuLevel.create(newValue, getSelectedUnit()), PkuLevelUnits.MICRO_MOL).getValue();
                final float topLimit = getValueFromRangeBar(rbRange.getRightIndex());
                final float validValue;

                if (mmolValue < rbRange.getTickStart()) {
                    validValue = rbRange.getTickStart();
                } else if (mmolValue >= topLimit) {
                    validValue = topLimit - 1;
                } else {
                    validValue = mmolValue;
                }

                rbRange.setRangePinsByValue(validValue, topLimit);

                if (Math.abs(validValue - mmolValue) > Constants.FLOAT_COMPARISION_ERROR_MARGIN) {
                    etNormalFloor.setText(presenter.formatValue(PkuLevelConverter.convertTo(PkuLevel.create(validValue, PkuLevelUnits.MICRO_MOL), getSelectedUnit())));
                }

                final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                v.clearFocus();

                return true;
            }

            return false;
        });
        etNormalFloor.setFilters(new InputFilter[] { floorFilter });

        etNormalCeil.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                final float newValue = Float.parseFloat(v.getText().toString());
                final float mmolValue = PkuLevelConverter.convertTo(PkuLevel.create(newValue, getSelectedUnit()), PkuLevelUnits.MICRO_MOL).getValue();
                final float bottomLimit = getValueFromRangeBar(rbRange.getLeftIndex());
                final float validValue;

                if (mmolValue <= bottomLimit) {
                    validValue = bottomLimit + 1;
                } else if (mmolValue > rbRange.getTickEnd()) {
                    validValue = rbRange.getTickEnd();
                } else {
                    validValue = mmolValue;
                }

                rbRange.setRangePinsByValue(bottomLimit, validValue);

                if (Math.abs(validValue - mmolValue) > Constants.FLOAT_COMPARISION_ERROR_MARGIN) {
                    etNormalCeil.setText(presenter.formatValue(PkuLevelConverter.convertTo(PkuLevel.create(validValue, PkuLevelUnits.MICRO_MOL), getSelectedUnit())));
                }

                final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                v.clearFocus();

                return true;
            }

            return false;
        });
        etNormalCeil.setFilters(new InputFilter[] { ceilFilter });
    }

    private float getValueFromRangeBar(final int tickIndex) {
        return (float) ((rbRange.getTickStart() + tickIndex) * rbRange.getTickInterval());
    }

    @Override
    protected void onStart() {
        super.onStart();

        rangeSet = false;
        presenter.refresh();
        disposable = changeProcessor.debounce(DEBOUNCE, TimeUnit.MILLISECONDS)
            .subscribe(tick -> {
                final float mmolFloor = getValueFromRangeBar(rbRange.getLeftIndex());
                final float mmolCeil = getValueFromRangeBar(rbRange.getRightIndex());

                presenter.changeValues(mmolFloor, mmolCeil, getSelectedUnit());
            }
        );
    }

    @Override
    protected void onStop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(TAG_CONFIRM_DIALOG) instanceof AlertDialogFragment) {
            return;
        }

        presenter.onBackPressed(getValueFromRangeBar(rbRange.getLeftIndex()),
                getValueFromRangeBar(rbRange.getRightIndex()));
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
                        presenter.saveNormalRange(
                                getValueFromRangeBar(rbRange.getLeftIndex()),
                                getValueFromRangeBar(rbRange.getRightIndex())
                        );
                    } else {
                        finish();
                    }
                });
        alertDialogFragment.show(getSupportFragmentManager(), TAG_CONFIRM_DIALOG);
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.plus(new RangeInfoModule(), new TestModule())
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

        tvLowDescription.setText(model.getLowText());
        tvHighDescription.setText(model.getHighText());
        tvVeryHighDescription.setText(model.getVeryHighText());

        final int radioButtonId = model.getSelectedUnit() == PkuLevelUnits.MICRO_MOL ? R.id.rangeSettingsUnitMicroMol : R.id.rangeSettingsUnitMilliGram;
        rgUnits.check(radioButtonId);

        rbRange.setTickStart(model.getNormalAbsoluteFloorMMolValue());
        rbRange.setTickEnd(model.getNormalAbsoluteCeilMMolValue());

        floorFilter.setCurrentUnit(model.getSelectedUnit());
        ceilFilter.setCurrentUnit(model.getSelectedUnit());

        if (!rangeSet) {
            rangeSet = true;
            rbRange.setRangePinsByValue(model.getNormalFloorMMolValue(), model.getNormalCeilMMolValue());
        }

        etNormalFloor.setText(presenter.formatValue(PkuLevelConverter.convertTo(PkuLevel.create(model.getNormalFloorMMolValue(), PkuLevelUnits.MICRO_MOL), model.getSelectedUnit())));
        etNormalCeil.setText(presenter.formatValue(PkuLevelConverter.convertTo(PkuLevel.create(model.getNormalCeilMMolValue(), PkuLevelUnits.MICRO_MOL), model.getSelectedUnit())));
    }

    @NonNull
    private PkuLevelUnits getSelectedUnit() {
        return rgUnits.getCheckedRadioButtonId() == R.id.rangeSettingsUnitMicroMol ? PkuLevelUnits.MICRO_MOL : PkuLevelUnits.MILLI_GRAM;
    }
}
