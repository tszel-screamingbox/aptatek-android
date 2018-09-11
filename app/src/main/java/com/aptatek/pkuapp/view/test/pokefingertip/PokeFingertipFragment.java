package com.aptatek.pkuapp.view.test.pokefingertip;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.view.test.base.TestBaseFragment;

import javax.inject.Inject;

public class PokeFingertipFragment extends TestBaseFragment<PokeFingertipView, PokeFingertipPresenter> implements PokeFingertipView {

    @Inject
    PokeFingertipPresenter presenter;

    @Override
    protected void injectTestFragment(final @NonNull TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_video;
    }

    @NonNull
    @Override
    public PokeFingertipPresenter createPresenter() {
        return presenter;
    }
}
