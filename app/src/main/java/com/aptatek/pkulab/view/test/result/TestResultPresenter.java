package com.aptatek.pkulab.view.test.result;

import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultInteractor;
import com.aptatek.pkulab.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.util.ChartUtils;
import com.aptatek.pkulab.view.settings.pkulevel.RangeSettingsValueFormatter;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class TestResultPresenter extends MvpBasePresenter<TestResultView> {

    private final PkuRangeInteractor rangeInteractor;
    private final TestResultInteractor testResultInteractor;
    private final ResourceInteractor resourceInteractor;
    private final WettingInteractor wettingInteractor;
    private final RangeSettingsValueFormatter rangeSettingsValueFormatter;

    private Disposable disposable;

    @Inject
    public TestResultPresenter(final PkuRangeInteractor rangeInteractor,
                               final TestResultInteractor testResultInteractor,
                               final ResourceInteractor resourceInteractor,
                               final WettingInteractor wettingInteractor,
                               final RangeSettingsValueFormatter rangeSettingsValueFormatter) {
        this.rangeInteractor = rangeInteractor;
        this.testResultInteractor = testResultInteractor;
        this.resourceInteractor = resourceInteractor;
        this.wettingInteractor = wettingInteractor;
        this.rangeSettingsValueFormatter = rangeSettingsValueFormatter;
    }

    public void initUi() {
        disposeSubscriptions();
        disposable =
                clearTestState().andThen(
                        Single.zip(
                                rangeInteractor.getInfo(),
                                testResultInteractor.getLatest().map(TestResult::getPkuLevel),
                                (rangeInfo, pkuLevel) ->
                                        TestResultState.builder()
                                                .setTitle(getTitleForLevel(pkuLevel, rangeInfo))
                                                .setColor(getColorForLevel(pkuLevel, rangeInfo))
                                                .setFormattedPkuValue(getFormattedPkuValue(pkuLevel, rangeInfo))
                                                .setPkuLevelText(getPkuLevelText(pkuLevel, rangeInfo))
                                                .setPkuUnit(rangeSettingsValueFormatter.getProperUnits(pkuLevel.getUnit()))
                                                .build())
                )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(state -> ifViewAttached(attachedView -> attachedView.render(state)));
    }

    @Override
    public void detachView() {
        disposeSubscriptions();

        super.detachView();
    }

    private void disposeSubscriptions() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private String getTitleForLevel(final PkuLevel level, final PkuRangeInfo rangeInfo) {
        final ChartUtils.State state = ChartUtils.getState(level, rangeInfo);
        final @StringRes int resource;

        switch (state) {
            case VERY_HIGH: {
                resource = R.string.test_result_title_veryhigh;
                break;
            }
            case HIGH: {
                resource = R.string.test_result_title_high;
                break;
            }
            case NORMAL: {
                resource = R.string.test_result_title_normal;
                break;
            }
            case LOW: {
                resource = R.string.test_result_title_low;
                break;
            }
            default: {
                Timber.d("Unhandled state: %s", state);
                resource = 0;
                break;
            }
        }

        return resourceInteractor.getStringResource(resource);
    }

    private @ColorInt
    int getColorForLevel(final PkuLevel level, final PkuRangeInfo rangeInfo) {
        return resourceInteractor.getColorResource(ChartUtils.stateColor(ChartUtils.getState(level, rangeInfo)));
    }

    private String getFormattedPkuValue(final PkuLevel level, final PkuRangeInfo rangeInfo) {
        return rangeSettingsValueFormatter.formatRegularValue(ChartUtils.convertToDisplayUnit(level, rangeInfo));
    }

    private String getPkuLevelText(final PkuLevel level, final PkuRangeInfo rangeInfo) {
        final ChartUtils.State state = ChartUtils.getState(level, rangeInfo);
        final @StringRes int resource;

        switch (state) {
            case VERY_HIGH: {
                resource = R.string.test_result_bubble_level_veryhigh;
                break;
            }
            case HIGH: {
                resource = R.string.test_result_bubble_level_high;
                break;
            }
            case NORMAL: {
                resource = R.string.test_result_bubble_level_normal;
                break;
            }
            case LOW: {
                resource = R.string.test_result_bubble_level_low;
                break;
            }
            default: {
                Timber.d("Unhandled state: %s", state);
                resource = 0;
                break;
            }
        }

        return resourceInteractor.getStringResource(resource);
    }

    private Completable clearTestState() {
        return wettingInteractor.resetWetting();
    }
}
