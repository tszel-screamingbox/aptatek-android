package com.aptatek.pkulab.view.rangeinfo;

import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RangeInfoPresenter extends MvpBasePresenter<RangeInfoView> {

    private final PkuRangeInteractor pkuRangeInteractor;
    private final PkuValueFormatter pkuValueFormatter;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public RangeInfoPresenter(final PkuRangeInteractor pkuRangeInteractor,
                              final PkuValueFormatter pkuValueFormatter) {
        this.pkuRangeInteractor = pkuRangeInteractor;
        this.pkuValueFormatter = pkuValueFormatter;
    }

    public void refresh() {
        disposables.add(pkuRangeInteractor.getInfo()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pkuRangeInfo -> {
                    final RangeInfoUiModel uiModel = RangeInfoUiModel.builder()
                            .setVeryHighValue(pkuValueFormatter.formatVeryHigh(pkuRangeInfo))
                            .setHighValue(pkuValueFormatter.formatHigh(pkuRangeInfo))
                            .setNormalValue(pkuValueFormatter.formatNormal(pkuRangeInfo))
                            .setLowValue(pkuValueFormatter.formatLow(pkuRangeInfo))
                            .setUnitValue(pkuValueFormatter.formatUnits(pkuRangeInfo))
                            .build();

                    ifViewAttached(attachedView -> attachedView.displayRangeInfo(uiModel));
                })
        );
    }

    @Override
    public void detachView() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }

        super.detachView();
    }
}
