package com.aptatek.aptatek.view.settings.pkulevel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.model.AlertDialogModel;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.dialog.AlertDialogDecisions;
import com.aptatek.aptatek.view.dialog.AlertDialogFragment;

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
    private static final float FLOAT_ERROR_LIMIT = 0.001f;
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
            presenter.formatValue(presenter.convertValue(getValueFromRangeBar(tickIndex), PkuLevelUnits.MICRO_MOL, getSelectedUnit()), getSelectedUnit())
        );

        rbRange.setOnRangeBarChangeListener((rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue) -> {
            Timber.d("rangeBarChange: " + leftPinIndex + ", " + rightPinIndex);

            etNormalFloor.setText(leftPinValue);
            etNormalCeil.setText(rightPinValue);

            final float mmolFloor = getValueFromRangeBar(leftPinIndex);
            final float mmolCeil = getValueFromRangeBar(rightPinIndex);

            if (rangeSet && lastModel != null
                    && (Math.abs(lastModel.getNormalFloorMMolValue() - mmolFloor) > FLOAT_ERROR_LIMIT
                    || Math.abs(lastModel.getNormalCeilMMolValue() - mmolCeil) > FLOAT_ERROR_LIMIT)) {

                changeProcessor.onNext(new Object());
            }
        });
        rbRange.setDrawTicks(false);

        etNormalFloor.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                final float newValue = Float.parseFloat(v.getText().toString());
                final float mmolValue = presenter.convertValue(newValue, getSelectedUnit(), PkuLevelUnits.MICRO_MOL);
                rbRange.setRangePinsByValue(mmolValue, getValueFromRangeBar(rbRange.getRightIndex()));

                return true;
            }

            return false;
        });

        etNormalCeil.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                final float newValue = Float.parseFloat(v.getText().toString());
                final float mmolValue = presenter.convertValue(newValue, getSelectedUnit(), PkuLevelUnits.MICRO_MOL);
                rbRange.setRangePinsByValue(getValueFromRangeBar(rbRange.getLeftIndex()), mmolValue);

                return true;
            }

            return false;
        });
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

        final AlertDialogFragment alertDialogFragment = AlertDialogFragment.create(
                AlertDialogModel.create(getString(R.string.settings_units_confirmation_title), getString(R.string.settings_units_confirmation_message)),
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

        tvLowDescription.setText(model.getLowText());
        tvHighDescription.setText(model.getHighText());
        tvVeryHighDescription.setText(model.getVeryHighText());

        final int radioButtonId = model.getSelectedUnit() == PkuLevelUnits.MICRO_MOL ? R.id.rangeSettingsUnitMicroMol : R.id.rangeSettingsUnitMilliGram;
        rgUnits.check(radioButtonId);

        rbRange.setTickStart(model.getNormalAbsoluteFloorMMolValue());
        rbRange.setTickEnd(model.getNormalAbsoluteCeilMMolValue());

        if (!rangeSet) {
            rangeSet = true;
            rbRange.setRangePinsByValue(model.getNormalFloorMMolValue(), model.getNormalCeilMMolValue());
        }

        etNormalFloor.setText(presenter.formatValue(presenter.convertValue(model.getNormalFloorMMolValue(), PkuLevelUnits.MICRO_MOL, model.getSelectedUnit()), model.getSelectedUnit()));
        etNormalCeil.setText(presenter.formatValue(presenter.convertValue(model.getNormalCeilMMolValue(), PkuLevelUnits.MICRO_MOL, model.getSelectedUnit()), model.getSelectedUnit()));

    }

    @NonNull
    private PkuLevelUnits getSelectedUnit() {
        return rgUnits.getCheckedRadioButtonId() == R.id.rangeSettingsUnitMicroMol ? PkuLevelUnits.MICRO_MOL : PkuLevelUnits.MILLI_GRAM;
    }
}
