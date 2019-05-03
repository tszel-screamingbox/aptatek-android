package com.aptatek.pkulab.view.test.result;

import androidx.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface TestResultView extends MvpView {

    void render(@NonNull TestResultState state);

}
