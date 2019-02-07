package com.aptatek.pkulab.view.connect.turnreaderon;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public interface TurnReaderOnPresenter<V extends TurnReaderOnView> extends MvpPresenter<V> {

    void onResumed();
    void onPaused();

}
