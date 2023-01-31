package com.aptatek.pkulab.view.test.addsample;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class AddSampleFragment extends TestBaseFragment<TestFragmentBaseView, AddSamplePresenter> {

    @Inject
    AddSamplePresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_sample;
    }

    @Override
    protected void injectTestFragment(TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.ADD_SAMPLE;
    }

    @Override
    public AddSamplePresenter createPresenter() {
        return presenter;
    }
}