package com.aptatek.pkulab.view.test.unscrewcap;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class UnscrewCapFragment extends TestBaseFragment<TestFragmentBaseView, UnscrewCapPresenter> {

    @Inject
    UnscrewCapPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_unscrew_cap;
    }

    @Override
    protected void injectTestFragment(TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.UNSCREW_CAP;
    }

    @Override
    public UnscrewCapPresenter createPresenter() {
        return presenter;
    }
}
