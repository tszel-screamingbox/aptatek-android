package com.aptatek.aptatek.view.splash;


import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class SplashActivityPresenter extends MvpBasePresenter<SplashActivityView> {

    private static final int DELAY_IN_MILLISEC = 1000;

    private Disposable tickHandler;

    @Inject
    SplashActivityPresenter() {

    }


    void initView() {
        tickHandler = Observable.interval(DELAY_IN_MILLISEC, 1, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    Timber.d("Load MainActivity");
                    ifViewAttached(SplashActivityView::mainActivityShouldLoad);
                    tickHandler.dispose();
                });
    }
}
