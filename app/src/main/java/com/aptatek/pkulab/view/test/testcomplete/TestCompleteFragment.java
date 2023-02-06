package com.aptatek.pkulab.view.test.testcomplete;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.result.TestResultActivity;

import javax.inject.Inject;

public class TestCompleteFragment extends TestBaseFragment<TestCompleteView, TestCompletePresenter> implements TestCompleteView {

    @Inject
    TestCompletePresenter testCompletePresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_complete;
    }

    @Override
    protected void injectTestFragment(TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.TEST_COMPLETE;
    }

    @Override
    public TestCompletePresenter createPresenter() {
        return testCompletePresenter;
    }

    @Override
    public void showResults(String testId) {
        requireActivity().finish();
        getBaseActivity().launchActivity(TestResultActivity.starter(requireActivity(), testId, false));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setNextButtonVisible(false);
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
    public boolean onNextPressed() {
        presenter.showResult();
        return true;
    }
}
