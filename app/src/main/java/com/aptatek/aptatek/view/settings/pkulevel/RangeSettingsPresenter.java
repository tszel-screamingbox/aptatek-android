package com.aptatek.aptatek.view.settings.pkulevel;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

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
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::buildSettingsModel)
                .subscribe(model -> ifViewAttached(attachedView ->
                        attachedView.displayRangeSettings(model)
                )));
    }

    private RangeSettingsModel buildSettingsModel(PkuRangeInfo info) {
        return RangeSettingsModel.builder()
                .setHighText(rangeSettingsValueFormatter.getFormattedHigh(info))
                .setLowText(rangeSettingsValueFormatter.getFormattedLow(info))
                .setVeryHighText(rangeSettingsValueFormatter.getFormattedVeryHigh(info))
                .setNormalFloorValue(info.getNormalFloorValue())
                .setNormalCeilValue(info.getNormalCeilValue())
                .setNormalAbsoluteFloorValue(2)
                .setNormalAbsoluteCeilValue(info.getHighCeilValue() - 2)
                .setSelectedUnit(info.getPkuLevelUnit())
                .build();
    }

    public void changeUnit(final @NonNull PkuLevelUnits unit) {
        compositeDisposable.add(pkuRangeInteractor.getInfo(unit)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::buildSettingsModel)
                .subscribe(model -> ifViewAttached(attachedView ->
                        attachedView.displayRangeSettings(model)
                )));
    }

    public void saveNormalRange(final float floor, final float ceil, final PkuLevelUnits units) {
        compositeDisposable.add(pkuRangeInteractor.saveNormalRange(floor, ceil, units)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }
}
