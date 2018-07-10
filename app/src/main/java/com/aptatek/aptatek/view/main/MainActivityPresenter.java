package com.aptatek.aptatek.view.main;

import com.aptatek.aptatek.domain.respository.manager.FakeCubeDataManager;
import com.aptatek.aptatek.util.ChartUtils;
import com.aptatek.aptatek.view.main.adapter.ChartVM;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

class MainActivityPresenter extends MvpBasePresenter<MainActivityView> {

    private final FakeCubeDataManager fakeCubeDataManager;
    private final ChartUtils chartUtils;

    @Inject
    MainActivityPresenter(FakeCubeDataManager fakeCubeDataManager,
                          ChartUtils chartUtils) {
        this.fakeCubeDataManager = fakeCubeDataManager;
        this.chartUtils = chartUtils;
    }

    List<ChartVM> fakeData() {
        return chartUtils.asChartVMList(fakeCubeDataManager.listAllItems());
    }
}
