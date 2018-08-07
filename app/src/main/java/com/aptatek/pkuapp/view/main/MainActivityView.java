package com.aptatek.pkuapp.view.main;

import com.aptatek.pkuapp.view.main.adapter.ChartVM;
import com.aptatek.pkuapp.view.main.adapter.DailyResultAdapterItem;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

interface MainActivityView extends MvpView {

    void displayData(List<ChartVM> data);

    void changeItemZoomState(ChartVM oldItem, ChartVM newItem);

    void updateTitles(String title, String subTitle);

    void setMeasureList(List<DailyResultAdapterItem> data);
}
