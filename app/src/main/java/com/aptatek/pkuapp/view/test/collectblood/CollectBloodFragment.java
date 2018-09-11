package com.aptatek.pkuapp.view.test.collectblood;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.view.test.base.TestBaseFragment;

import javax.inject.Inject;

public class CollectBloodFragment extends TestBaseFragment<CollectBloodView, CollectBloodPresenter> implements CollectBloodView {

    @Inject
    CollectBloodPresenter presenter;

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
    public CollectBloodPresenter createPresenter() {
        return presenter;
    }
}
