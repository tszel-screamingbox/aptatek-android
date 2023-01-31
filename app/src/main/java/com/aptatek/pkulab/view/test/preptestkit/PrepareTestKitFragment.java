package com.aptatek.pkulab.view.test.preptestkit;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class PrepareTestKitFragment extends TestBaseFragment<TestFragmentBaseView, PrepareTestKitPresenter> {

    @Inject
    PrepareTestKitPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_prep_test_kit;
    }

    @Override
    protected void injectTestFragment(TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.PREP_TEST_KIT;
    }

    @Override
    public PrepareTestKitPresenter createPresenter() {
        return presenter;
    }
}
