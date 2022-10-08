package com.aptatek.pkulab.view.test.mixsample;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;

import javax.inject.Inject;

public class MixSampleFragment extends TestBaseFragment<MixSampleView, MixSamplePresenter> implements MixSampleView {

    @Inject
    MixSamplePresenter presenter;

    @Override
    protected void injectTestFragment(final @NonNull TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mix_sample;
    }

    @NonNull
    @Override
    public MixSamplePresenter createPresenter() {
        return presenter;
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.MIX_SAMPLE;
    }
}
