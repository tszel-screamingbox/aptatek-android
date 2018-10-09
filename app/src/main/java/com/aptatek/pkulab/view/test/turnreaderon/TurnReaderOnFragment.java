package com.aptatek.pkulab.view.test.turnreaderon;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;

import javax.inject.Inject;

public class TurnReaderOnFragment extends TestBaseFragment<TurnReaderOnView, TurnReaderOnPresenter> implements TurnReaderOnView {

    @Inject
    TurnReaderOnPresenter presenter;

    @Override
    protected void injectTestFragment(final @NonNull TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_video;
    }

    @Override
    public void onReaderConnected() {
        showNextScreen();
    }

    @NonNull
    @Override
    public TurnReaderOnPresenter createPresenter() {
        return presenter;
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.TURN_READER_ON;
    }
}
