package com.aptatek.aptatek.view.weekly;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.device.time.TimeHelper;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.cube.CubeInteractor;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ix.Ix;

public class WeeklyResultActivityPresenter extends MvpBasePresenter<WeeklyResultActivityView> {

    private static final int EMPTY_LIST = -1;

    private final CubeInteractor cubeInteractor;
    private final ResourceInteractor resourceInteractor;
    private final PkuRangeInteractor rangeInteractor;
    private final WeeklyChartDateFormatter weeklyChartDateFormatter;
    private final List<Integer> weekList = new ArrayList<>();

    private CompositeDisposable disposables;

    @Inject
    public WeeklyResultActivityPresenter(final CubeInteractor cubeInteractor,
                                         final ResourceInteractor resourceInteractor,
                                         final PkuRangeInteractor rangeInteractor,
                                         final WeeklyChartDateFormatter weeklyChartDateFormatter) {
        this.cubeInteractor = cubeInteractor;
        this.resourceInteractor = resourceInteractor;
        this.rangeInteractor = rangeInteractor;
        this.weeklyChartDateFormatter = weeklyChartDateFormatter;
    }

    @Override
    public void attachView(final @NonNull WeeklyResultActivityView view) {
        super.attachView(view);

        disposables = new CompositeDisposable();

        disposables.add(rangeInteractor.getInfo()
                .map(rangeInfo ->
                        resourceInteractor.getStringResource(R.string.weekly_chart_label,
                                resourceInteractor.getStringResource(rangeInfo.getPkuLevelUnit() == PkuLevelUnits.MICRO_MOL
                                        ? R.string.rangeinfo_pkulevel_mmol
                                        : R.string.rangeinfo_pkulevel_mg)
                        )
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(label ->
                        ifViewAttached(attachedView ->
                                attachedView.displayUnitLabel(label)
                        )
                )
        );
    }

    @Override
    public void detachView() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }

        super.detachView();
    }

    void subTitle(final int page) {
        final String weeklyChartTitle = weeklyChartDateFormatter.getWeeklyChartTitle(weekList.get(page));
        ifViewAttached(view -> view.onSubtitleChanged(weeklyChartTitle));
    }

    void showPage(final int pageNum) {
        ifViewAttached(view -> view.onLoadNextPage(pageNum));
        updateArrows(pageNum);
    }

    void updateArrows(final int page) {
        ifViewAttached(view -> view.onUpdateLeftArrow(page != 0));
        ifViewAttached(view -> view.onUpdateRightArrow(page != weekList.size() - 1));
    }

    // TODO should not get ALL data at once...
    public void loadValidWeeks() {
        disposables.add(cubeInteractor.listAll()
                .map(cubeDataList -> {
                    Ix.from(cubeDataList).foreach(cubeData -> {
                        final int week = TimeHelper.getWeeksBetween(cubeData.getTimestamp(), System.currentTimeMillis());
                        if (!weekList.contains(week) && cubeData.getPkuLevel().getValue() >= 0) {
                            weekList.add(week);
                        }
                    });

                    if (weekList.isEmpty()) {
                        weekList.add(EMPTY_LIST);
                    }
                    Collections.reverse(weekList);

                    return weekList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(validWeeks ->
                        ifViewAttached(attachedView ->
                                attachedView.displayValidWeekList(validWeeks)
                        )
                )
        );
    }

    public List<Integer> getValidWeeks() {
        return weekList;
    }
}
