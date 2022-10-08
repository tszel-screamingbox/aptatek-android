package com.aptatek.pkulab.view.test.prepbluecap;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class PrepareBlueCapFragment extends TestBaseFragment<TestFragmentBaseView, PrepareBlueCapPresenter> {

    @Inject
    PrepareBlueCapPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_prep_blue_cap;
    }

    @Override
    protected void injectTestFragment(TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.PREP_BLUE_CAP;
    }

    @Override
    public PrepareBlueCapPresenter createPresenter() {
        return presenter;
    }
}

