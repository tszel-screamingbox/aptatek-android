package com.aptatek.pkulab.view.main;

import com.aptatek.pkulab.view.main.adapter.chart.ChartVM;
import com.aptatek.pkulab.view.main.adapter.daily.DailyResultAdapterItem;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

interface MainActivityView extends MvpView {

    void displayData(List<ChartVM> data);

    void navigateToTestScreen();

    void changeItemZoomState(ChartVM oldItem, ChartVM newItem);

    void updateTitles(String title, String subTitle);

    void setMeasureList(List<DailyResultAdapterItem> data);
}
