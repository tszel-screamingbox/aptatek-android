package com.aptatek.pkulab.view.test.testing;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestActivity;
import com.aptatek.pkulab.view.test.TestActivityView;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.result.TestResultActivity;

import javax.inject.Inject;

public class TestingFragment extends TestBaseFragment<TestingView, TestingPresenter> implements TestingView {

    @Inject
    TestingPresenter presenter;

    @Override
    protected void injectTestFragment(final @NonNull TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_video;
    }

    @Override
    public void onTestFinished() {
        getActivity().finish();
        getBaseActivity().launchActivity(TestResultActivity.starter(getActivity()));
    }

    @NonNull
    @Override
    public TestingPresenter createPresenter() {
        return presenter;
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.TESTING;
    }

    @Override
    public void showTurnReaderOn() {
        if (getActivity() instanceof TestActivityView) {
            ((TestActivityView) getActivity()).showScreen(TestScreens.TURN_READER_ON);
        }
    }
}
