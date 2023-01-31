package com.aptatek.pkulab.view.test.prepbuffer;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.base.TestFragmentBaseView;

import javax.inject.Inject;

public class PrepareBufferChamberFragment extends TestBaseFragment<TestFragmentBaseView, PrepareBufferChamberPresenter> {

    @Inject
    PrepareBufferChamberPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_prep_buffer;
    }

    @Override
    protected void injectTestFragment(TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.PREP_BUFFER;
    }

    @Override
    public PrepareBufferChamberPresenter createPresenter() {
        return presenter;
    }
}
