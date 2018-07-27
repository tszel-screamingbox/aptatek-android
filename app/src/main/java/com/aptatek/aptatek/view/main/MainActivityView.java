package com.aptatek.aptatek.view.main;

import com.aptatek.aptatek.view.main.adapter.DailyResultAdapterItem;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

interface MainActivityView extends MvpView {

    void updateTitles(String title, String subTitle);

    void setMeasureList(List<DailyResultAdapterItem> data);
}
