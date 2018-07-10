package com.aptatek.aptatek.view.rangeinfo;

import android.support.annotation.NonNull;

import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface RangeInfoView extends MvpView {

    void displayRangeInfo(@NonNull RangeInfoUiModel uiModel);

}
