package com.aptatek.pkulab.view.rangeinfo;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface RangeInfoView extends MvpView {

    void displayRangeInfo(@NonNull RangeInfoUiModel uiModel);

}
