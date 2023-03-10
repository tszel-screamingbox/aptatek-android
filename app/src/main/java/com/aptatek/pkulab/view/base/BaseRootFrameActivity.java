package com.aptatek.pkulab.view.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;


public abstract class BaseRootFrameActivity<V extends MvpView, P extends MvpPresenter<V>>
        extends BaseActivity<V, P> {


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.root_frame_activity);
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.rootFrame;
    }
}
