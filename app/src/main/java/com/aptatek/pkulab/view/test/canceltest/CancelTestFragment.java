package com.aptatek.pkulab.view.test.canceltest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.analytic.events.riskmitigation.UnfinishedTest;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestActivityView;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;

import javax.inject.Inject;

import butterknife.OnClick;

import static com.aptatek.pkulab.view.test.TestScreens.CANCEL;

public class CancelTestFragment extends TestBaseFragment<CancelTestView, CancelTestPresenter> implements CancelTestView {

    private static final String IS_FROM_TESTING = "from.wetting.key";

    @Inject
    CancelTestPresenter presenter;
    @Inject
    IAnalyticsManager analyticsManager;

    private boolean fromTesting = false;

    public static CancelTestFragment createCancelFragment(final boolean fromTesting) {
        final CancelTestFragment fragment = new CancelTestFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_FROM_TESTING, fromTesting);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cancel_test;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            fromTesting = bundle.getBoolean(IS_FROM_TESTING, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
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
        analyticsManager.logEvent(new UnfinishedTest("risk_unfinished_test", fromTesting));
        presenter.stopTest();
    }

    @Override
    public TestScreens getScreen() {
        return CANCEL;
    }

    @Override
    public TestScreens getPreviousScreen() {
        FragmentActivity fragmentActivity = requireActivity();
        return (fragmentActivity instanceof TestActivityView) ? ((TestActivityView) fragmentActivity).getPreviousScreen() : null;
    }
}
