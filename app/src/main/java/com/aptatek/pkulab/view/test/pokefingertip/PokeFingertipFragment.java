package com.aptatek.pkulab.view.test.pokefingertip;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;

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
        return R.layout.fragment_poke_fingertip;
    }

    @NonNull
    @Override
    public PokeFingertipPresenter createPresenter() {
        return presenter;
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.POKE_FINGERTIP;
    }
}
