package com.aptatek.aptatek.view.settings.pkulevel;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.aptatek.aptatek.util.Constants;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RangeSettingsPresenter extends MvpBasePresenter<RangeSettingsView> {

    private static final float RANGE_BORDER = 2f;
    private static final float RATIO_MG = 0.1f;
    private static final float RATIO_MMOL = 1f;

    private final PkuRangeInteractor pkuRangeInteractor;
    private final RangeSettingsValueFormatter rangeSettingsValueFormatter;

    private CompositeDisposable compositeDisposable;

    @Inject
    public RangeSettingsPresenter(final PkuRangeInteractor pkuRangeInteractor, final RangeSettingsValueFormatter rangeSettingsValueFormatter) {
        this.pkuRangeInteractor = pkuRangeInteractor;
        this.rangeSettingsValueFormatter = rangeSettingsValueFormatter;
    }

    @Override
    public void attachView(final @NonNull RangeSettingsView view) {
        super.attachView(view);

        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

        super.detachView();
    }

    public void refresh() {
        compositeDisposable.add(pkuRangeInteractor.getInfo()
                .map(this::buildSettingsModel)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> ifViewAttached(attachedView ->
                        attachedView.displayRangeSettings(model)
                )));
    }

    private RangeSettingsModel buildSettingsModel(final PkuRangeInfo info) {
        return RangeSettingsModel.builder()
                .setLowText(rangeSettingsValueFormatter.getFormattedLow(info))
                .setHighText(rangeSettingsValueFormatter.getFormattedHigh(info))
                .setVeryHighText(rangeSettingsValueFormatter.getFormattedVeryHigh(info))
                .setNormalFloorMMolValue(convertValue(info.getNormalFloorValue(), info.getPkuLevelUnit(), PkuLevelUnits.MICRO_MOL))
                .setNormalCeilMMolValue(convertValue(info.getNormalCeilValue(), info.getPkuLevelUnit(), PkuLevelUnits.MICRO_MOL))
                .setNormalAbsoluteFloorMMolValue(RANGE_BORDER * getRangeOffsetForUnit(PkuLevelUnits.MICRO_MOL))
                .setNormalAbsoluteCeilMMolValue(convertValue(info.getHighCeilValue(), info.getPkuLevelUnit(), PkuLevelUnits.MICRO_MOL) - (RANGE_BORDER * getRangeOffsetForUnit(PkuLevelUnits.MICRO_MOL)))
                .setSelectedUnit(info.getPkuLevelUnit())
                .build();
    }

    public void changeValues(final float mmolFloor, final float mmolCeil, final @NonNull PkuLevelUnits displayUnit) {
        compositeDisposable.add(
                Single.just(PkuRangeInfo.builder()
                        .setHighCeilValue(convertValue(Constants.DEFAULT_PKU_HIGH_CEIL, PkuLevelUnits.MICRO_MOL, displayUnit))
                        .setNormalFloorValue(convertValue(mmolFloor, PkuLevelUnits.MICRO_MOL, displayUnit))
                        .setNormalCeilValue(convertValue(mmolCeil, PkuLevelUnits.MICRO_MOL, displayUnit))
                        .setPkuLevelUnit(displayUnit)
                        .build()
                )
                .map(this::buildSettingsModel)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> ifViewAttached(attachedView ->
                        attachedView.displayRangeSettings(model)
                )));
    }

    public void saveNormalRange(final float floor, final float ceil) {
        compositeDisposable.add(pkuRangeInteractor.saveNormalRangeMMol(floor, ceil)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    public float convertValue(final float value, final PkuLevelUnits originalUnit, final PkuLevelUnits target) {
        return pkuRangeInteractor.getValueInUnit(value, originalUnit, target);
    }

    public String formatValue(final float value, final PkuLevelUnits unit) {
        return rangeSettingsValueFormatter.formatRegularValue(value, unit);
    }

    private float getRangeOffsetForUnit(final PkuLevelUnits units) {
        return units == PkuLevelUnits.MICRO_MOL ? RATIO_MMOL : RATIO_MG;
    }

}
