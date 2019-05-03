package com.aptatek.pkulab.view.settings.pkulevel;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.util.Constants;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RangeSettingsPresenter extends MvpBasePresenter<RangeSettingsView> {

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
                .setIncreasedText(rangeSettingsValueFormatter.getFormattedIncreased(info))
                .setNormalFloorMMolValue(convertValue(info.getNormalFloorValue(), info.getPkuLevelUnit(), PkuLevelUnits.MICRO_MOL))
                .setNormalCeilMMolValue(convertValue(info.getNormalCeilValue(), info.getPkuLevelUnit(), PkuLevelUnits.MICRO_MOL))
                .setNormalAbsoluteFloorMMolValue(convertValue(info.getNormalAbsoluteMinValue(), info.getPkuLevelUnit(), PkuLevelUnits.MICRO_MOL))
                .setNormalAbsoluteCeilMMolValue(convertValue(info.getNormalAbsoluteMaxValue(), info.getPkuLevelUnit(), PkuLevelUnits.MICRO_MOL))
                .setSelectedUnit(info.getPkuLevelUnit())
                .build();
    }

    public void changeValues(final float mmolFloor, final float mmolCeil, final @NonNull PkuLevelUnits displayUnit) {
        compositeDisposable.add(
                Single.just(PkuRangeInfo.builder()
                        .setHighCeilValue(convertValue(mmolCeil + Constants.DEFAULT_PKU_HIGH_RANGE, PkuLevelUnits.MICRO_MOL, displayUnit))
                        .setNormalFloorValue(Math.max(displayUnit == PkuLevelUnits.MILLI_GRAM ? 0.2f : 2f, convertValue(mmolFloor, PkuLevelUnits.MICRO_MOL, displayUnit)))
                        .setNormalCeilValue(convertValue(mmolCeil, PkuLevelUnits.MICRO_MOL, displayUnit))
                        .setNormalAbsoluteMinValue(convertValue(Constants.DEFAULT_PKU_LOWEST_VALUE, PkuLevelUnits.MICRO_MOL, displayUnit))
                        .setNormalAbsoluteMaxValue(convertValue(Constants.DEFAULT_PKU_HIGHEST_VALUE, PkuLevelUnits.MICRO_MOL, displayUnit))
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

    public void saveValues(final PkuLevelUnits pkuLevelUnits) {
        compositeDisposable.add(pkuRangeInteractor.saveDisplayUnit(pkuLevelUnits)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> ifViewAttached(RangeSettingsView::showSettingsUpdateMessage)));
    }

    public void onBackPressed(final PkuLevelUnits pkuLevelUnits) {
        compositeDisposable.add(pkuRangeInteractor.getInfo()
                .flatMapCompletable(info -> {
                    if (pkuLevelUnits != info.getPkuLevelUnit()) {
                        return Completable.fromAction(() -> ifViewAttached(RangeSettingsView::showSaveChangesDialog));
                    } else {
                        return Completable.fromAction(() -> ifViewAttached(RangeSettingsView::finish));
                    }
                }).subscribe());
    }

    public String formatValue(final PkuLevel pkuLevel) {
        return rangeSettingsValueFormatter.formatRegularValue(pkuLevel);
    }

    private float convertValue(final float value, final PkuLevelUnits currentUnit, final PkuLevelUnits targetUnit) {
        return PkuLevelConverter.convertTo(PkuLevel.create(value, currentUnit), targetUnit).getValue();
    }

}
