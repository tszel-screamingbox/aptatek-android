package com.aptatek.aptatek.view.rangeinfo;

import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeInteractor;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RangeInfoPresenter extends MvpBasePresenter<RangeInfoView> {

    private final PkuRangeInteractor pkuRangeInteractor;
    private final PkuValueFormatter pkuValueFormatter;

    private Disposable disposable;

    @Inject
    public RangeInfoPresenter(final PkuRangeInteractor pkuRangeInteractor,
                              final PkuValueFormatter pkuValueFormatter) {
        this.pkuRangeInteractor = pkuRangeInteractor;
        this.pkuValueFormatter = pkuValueFormatter;
    }

    public void refresh() {
        disposable = pkuRangeInteractor.getInfo()
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
                });
    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
    }
}
