package com.aptatek.pkulab.view.test.preparecassette;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class PrepareCassetteFragment extends TestBaseFragment<TestFragmentBaseView, PrepareCassettePresenter> {

    @Inject
    PrepareCassettePresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attach_chamber;
    }

    @Override
    protected void injectTestFragment(TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.ATTACH_CHAMBER;
    }

    @Override
    public PrepareCassettePresenter createPresenter() {
        return presenter;
    }
}