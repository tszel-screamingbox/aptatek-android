package com.aptatek.pkulab.view.main.continuetest;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.ContinueTestResultType;
import com.aptatek.pkulab.domain.model.TestContinueDialogModel;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnFragment;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisions;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.test.dispose.DisposeActivity;
import com.aptatek.pkulab.view.test.turnreaderon.permission.PermissionRequiredOnTestActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class TurnReaderOnContinueTestFragment extends TurnReaderOnFragment<TurnReaderOnContinueTestView, TurnReaderOnContinueTestPresenter>
        implements TurnReaderOnContinueTestView {

    private static final String TAG_CONTINUE_TEST_UNABLE_SYNC_TAG = "aptatek.test.continue.unable.sync.dialog";

    @BindView(R.id.turnReaderOnSkip)
    View btnSkip;

    @Inject
    TurnReaderOnContinueTestPresenter presenter;

    @NonNull
    @Override
    public TurnReaderOnContinueTestPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @OnClick(R.id.turnReaderOnSkip)
    void onSkipClicked() {
        final AlertDialogFragment dialogFragment = AlertDialogFragment.create(
                TestContinueDialogModel.unableToSyncWithReaderDialogModelCreate(requireContext()),
                decision -> {
                    if (decision == AlertDialogDecisions.POSITIVE) {

                    } else if (decision == AlertDialogDecisions.NEGATIVE) {
                        getBaseActivity().launchActivity(new Intent(requireActivity(), DisposeActivity.class));
                        getBaseActivity().finish();
                    }
                });
        dialogFragment.show(getBaseActivity().getSupportFragmentManager(), TAG_CONTINUE_TEST_UNABLE_SYNC_TAG);
    }

    @Override
    protected void initObjects(final View view) {
        super.initObjects(view);
        headerView.setSubtitle(getString(R.string.test_continue_turnreaderon_message));
        btnSkip.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayMissingPermissions() {
        getBaseActivity().launchActivity(new Intent(requireActivity(), PermissionRequiredOnTestActivity.class));
    }

    @Override
    public void onSelfCheckComplete() {
        presenter.syncData();
    }

    @Override
    public void finishTestContinue(final ContinueTestResultType continueTestResultType) {
        final Intent intent = new Intent();
        intent.putExtra(Constants.CONTINUE_TEST_RESULT_TYPE_KEY, continueTestResultType);
        requireActivity().setResult(Activity.RESULT_OK, intent);
        requireActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResumed();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPaused();
    }
}
