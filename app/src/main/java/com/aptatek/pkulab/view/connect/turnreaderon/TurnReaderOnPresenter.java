package com.aptatek.pkulab.view.connect.turnreaderon;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.domain.model.reader.ReaderDevice;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public interface TurnReaderOnPresenter<V extends TurnReaderOnView> extends MvpPresenter<V> {

    void onResumed();
    void onPaused();
    void connectTo(@NonNull ReaderDevice readerDevice);

}
