package com.aptatek.pkulab.view.main.continuetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnFragment;
import com.aptatek.pkulab.view.test.turnreaderon.permission.PermissionRequiredOnTestActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class TurnReaderOnContinueTestFragment extends TurnReaderOnFragment<TurnReaderOnContinueTestView, TurnReaderOnContinueTestPresenter>
        implements TurnReaderOnContinueTestView {

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
        presenter.checkLastMeasure();
    }
}
