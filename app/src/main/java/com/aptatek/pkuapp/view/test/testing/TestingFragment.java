package com.aptatek.pkuapp.view.test.testing;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.view.test.base.TestBaseFragment;
import com.aptatek.pkuapp.view.test.result.TestResultActivity;

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
        startActivity(TestResultActivity.starter(getActivity()));
    }

    @NonNull
    @Override
    public TestingPresenter createPresenter() {
        return presenter;
    }
}
