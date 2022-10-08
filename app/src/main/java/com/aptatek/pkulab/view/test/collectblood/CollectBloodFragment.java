package com.aptatek.pkulab.view.test.collectblood;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;

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
        return R.layout.fragment_collect_blood;
    }

    @NonNull
    @Override
    public CollectBloodPresenter createPresenter() {
        return presenter;
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.COLLECT_BLOOD;
    }
}
