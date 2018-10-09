package com.aptatek.pkulab.view.test.canceltest;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;

import javax.inject.Inject;

import butterknife.OnClick;

public class CancelTestFragment extends TestBaseFragment<CancelTestView, CancelTestPresenter> implements CancelTestView {

    @Inject
    CancelTestPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cancel_test;
    }

    @Override
    protected void injectTestFragment(@NonNull final TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public CancelTestPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @OnClick(R.id.testCancelButton)
    void onCancelClicked() {
        showPreviousScreen();
    }

    @OnClick(R.id.testOkButton)
    void onYesClicked() {
        presenter.stopTest();
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.CANCEL;
    }
}
