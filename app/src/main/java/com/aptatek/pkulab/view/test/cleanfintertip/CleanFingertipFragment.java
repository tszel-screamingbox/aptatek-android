package com.aptatek.pkulab.view.test.cleanfintertip;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class CleanFingertipFragment extends TestBaseFragment<TestFragmentBaseView, CleanFingertipPresenter> {

    @Inject
    CleanFingertipPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_clean_fingertip;
    }

    @Override
    protected void injectTestFragment(TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.CLEAN_FINGERTIP;
    }

    @Override
    public CleanFingertipPresenter createPresenter() {
        return presenter;
    }
}
