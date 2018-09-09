package com.aptatek.pkuapp.view.weekly.chart;

import com.github.mikephil.charting.data.BubbleDataSet;
import com.hannesdorfmann.mosby3.mvp.MvpView;


interface WeeklyChartView extends MvpView {

    void displayChartData(BubbleDataSet bubbleDataSet);
}
