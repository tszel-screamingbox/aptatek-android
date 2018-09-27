package com.aptatek.pkuapp.view.connect.scan;

import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.FragmentComponent;
import com.aptatek.pkuapp.view.connect.common.BaseConnectScreenFragment;

import javax.inject.Inject;

public class ScanFragment extends BaseConnectScreenFragment<ScanView, ScanPresenter> implements ScanView {

    @Inject
    ScanPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_scan;
    }

    @Override
    protected void initObjects(final View view) {

    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public ScanPresenter createPresenter() {
        return presenter;
    }
}
