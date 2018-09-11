package com.aptatek.pkuapp.view.test.connectitall;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.view.test.base.TestBaseFragment;

import javax.inject.Inject;

public class ConnectItAllFragment extends TestBaseFragment<ConnectItAllView, ConnectItAllPresenter> implements ConnectItAllView {

    @Inject
    ConnectItAllPresenter presenter;

    @Override
    protected void injectTestFragment(final @NonNull TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_video;
    }

    @NonNull
    @Override
    public ConnectItAllPresenter createPresenter() {
        return presenter;
    }
}
