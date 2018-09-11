package com.aptatek.pkuapp.view.test.turnreaderon;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.view.test.TestActivityView;
import com.aptatek.pkuapp.view.test.base.TestBaseFragment;

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
        if (getActivity() instanceof TestActivityView) {
            ((TestActivityView) getActivity()).showNextScreen();
        }
    }

    @NonNull
    @Override
    public TurnReaderOnPresenter createPresenter() {
        return presenter;
    }
}
