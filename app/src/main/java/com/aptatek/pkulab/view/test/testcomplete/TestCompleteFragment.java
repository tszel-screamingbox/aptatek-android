package com.aptatek.pkulab.view.test.testcomplete;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.connect.turnreaderon.SyncProgress;
import com.aptatek.pkulab.view.dialog.SyncProgressDialogFragment;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.result.TestResultActivity;

import javax.inject.Inject;

public class TestCompleteFragment extends TestBaseFragment<TestCompleteView, TestCompletePresenter> implements TestCompleteView {
    private static final String TAG_SYNC_DIALOG = "syncDialog";

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

    @Override
    public void showSyncProgressDialog(SyncProgress syncProgress) {
        final SyncProgressDialogFragment dialogFragment = (SyncProgressDialogFragment) getChildFragmentManager().findFragmentByTag(TAG_SYNC_DIALOG);
        if (dialogFragment == null) {
            SyncProgressDialogFragment.create(syncProgress).show(getChildFragmentManager(), TAG_SYNC_DIALOG);
        } else {
            dialogFragment.updateSyncProgress(syncProgress);
        }
    }

    @Override
    public void dismissProgressDialog() {
        final SyncProgressDialogFragment dialogFragment = (SyncProgressDialogFragment) getChildFragmentManager().findFragmentByTag(TAG_SYNC_DIALOG);
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }
}
