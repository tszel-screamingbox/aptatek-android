package com.aptatek.pkuapp.view.test.result;

import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.cube.CubeInteractor;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkuapp.domain.interactor.samplewetting.SampleWettingInteractor;
import com.aptatek.pkuapp.domain.model.CubeData;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuRangeInfo;
import com.aptatek.pkuapp.util.ChartUtils;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class TestResultPresenter extends MvpBasePresenter<TestResultView> {

    private final PkuRangeInteractor rangeInteractor;
    private final CubeInteractor cubeInteractor;
    private final ResourceInteractor resourceInteractor;
    private final SampleWettingInteractor sampleWettingInteractor;
    private final IncubationInteractor incubationInteractor;

    private Disposable disposable;

    @Inject
    public TestResultPresenter(final PkuRangeInteractor rangeInteractor,
                               final CubeInteractor cubeInteractor,
                               final ResourceInteractor resourceInteractor,
                               final SampleWettingInteractor sampleWettingInteractor,
                               final IncubationInteractor incubationInteractor) {
        this.rangeInteractor = rangeInteractor;
        this.cubeInteractor = cubeInteractor;
        this.resourceInteractor = resourceInteractor;
        this.sampleWettingInteractor = sampleWettingInteractor;
        this.incubationInteractor = incubationInteractor;
    }

    public void initUi() {
        disposable =
                clearTestState().andThen(
                    Single.zip(
                        rangeInteractor.getInfo(),
                        cubeInteractor.getLatest().map(CubeData::getPkuLevel),
                        (rangeInfo, pkuLevel) ->
                        TestResultState.builder()
                            .setTitle(getTitleForLevel(pkuLevel, rangeInfo))
                            .setMessage(getMessageForLevel(pkuLevel, rangeInfo))
                            .setColor(getColorForLevel(pkuLevel, rangeInfo))
                            .setFormattedPkuValue(getFormattedPkuValue(pkuLevel, rangeInfo))
                            .setPkuLevelText(getPkuLevelText(pkuLevel, rangeInfo))
                            .build())
                )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(state -> ifViewAttached(attachedView -> attachedView.render(state)));
    }

    @Override
    public void detachView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.detachView();
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

        return resource == 0 ? null : resourceInteractor.getStringResource(resource);
    }

    private String getMessageForLevel(final PkuLevel level, final PkuRangeInfo rangeInfo) {
        final ChartUtils.State state = ChartUtils.getState(level, rangeInfo);
        final @StringRes int resource;

        switch (state) {
            case VERY_HIGH: {
                resource = R.string.test_result_message_veryhigh;
                break;
            }
            // TODO wait until the texts are finalized (after release)
//            case HIGH: {
//                resource = R.string.test_result_message_high;
//                break;
//            }
//            case NORMAL: {
//                resource = R.string.test_result_message_normal;
//                break;
//            }
//            case LOW: {
//                resource = R.string.test_result_message_low;
//                break;
//            }
            default: {
                Timber.d("Unhandled state: %s", state);
                resource = 0;
                break;
            }
        }

        return resource == 0 ? null : resourceInteractor.getStringResource(resource);
    }

    private @ColorInt int getColorForLevel(final PkuLevel level, final PkuRangeInfo rangeInfo) {
        return resourceInteractor.getColorResource(ChartUtils.stateColor(ChartUtils.getState(level, rangeInfo)));
    }

    private String getFormattedPkuValue(final PkuLevel level, final PkuRangeInfo rangeInfo) {
        final float valueInDisplayUnit = ChartUtils.convertToDisplayUnit(level, rangeInfo).getValue();
        return String.format(Locale.getDefault(), "%.0f", valueInDisplayUnit);
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

        return resource == 0 ? null : resourceInteractor.getStringResource(resource);
    }

    private Completable clearTestState() {
        return sampleWettingInteractor.resetWetting()
            .andThen(incubationInteractor.resetIncubation());
    }
}
