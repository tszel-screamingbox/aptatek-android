package com.aptatek.pkulab.view.test.selftest;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;

import javax.inject.Inject;

public class SelfTestFragment extends TestBaseFragment<SelfTestView, SelfTestPresenter> implements SelfTestView {

    @Inject
    SelfTestPresenter presenter;

    @Override
    protected void injectTestFragment(final TestFragmentComponent fragmentComponent) {
            fragmentComponent.inject(this);
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.SELF_TEST;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_video;
    }

    @NonNull
    @Override
    public SelfTestPresenter createPresenter() {
        return presenter;
    }
}
