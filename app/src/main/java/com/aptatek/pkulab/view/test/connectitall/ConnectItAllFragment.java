package com.aptatek.pkulab.view.test.connectitall;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestActivityView;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;

import javax.inject.Inject;

public class ConnectItAllFragment extends TestBaseFragment<ConnectItAllView, ConnectItAllPresenter> implements ConnectItAllView {

    @Inject
    ConnectItAllPresenter presenter;

    @Override
    protected void injectTestFragment(final @NonNull TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_insert_cassette;
    }

    @NonNull
    @Override
    public ConnectItAllPresenter createPresenter() {
        return presenter;
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.CONNECT_IT_ALL;
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.cancelWettingNotification();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void showTurnReaderOn() {
        if (getActivity() instanceof TestActivityView) {
            ((TestActivityView) getActivity()).showTurnReaderOn();
        }
    }
}
