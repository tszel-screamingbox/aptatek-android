package com.aptatek.pkulab.view.test.breakfoil;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;

import javax.inject.Inject;

public class BreakFoilFragment extends TestBaseFragment<BreakFoilView, BreakFoilPresenter> implements BreakFoilView {

    @Inject
    BreakFoilPresenter presenter;

    @Override
    protected void injectTestFragment(final TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_video;
    }

    @NonNull
    @Override
    public BreakFoilPresenter createPresenter() {
        return presenter;
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.BREAK_FOIL;
    }
}
