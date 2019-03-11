package com.aptatek.pkulab.view.connect.onboarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.connect.onboarding.permission.PermissionRequiredOnboardingFragment;
import com.aptatek.pkulab.view.connect.onboarding.turnon.TurnReaderOnConnectFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static android.support.design.widget.BottomSheetBehavior.from;

public class ConnectOnboardingReaderActivity extends BaseActivity<ConnectReaderView, ConnectReaderPresenter> implements ConnectReaderView {

    public static Intent starter(final Context context) {
        return new Intent(context, ConnectOnboardingReaderActivity.class);
    }

    @Inject
    ConnectReaderPresenter presenter;

    @BindView(R.id.bottom_sheet)
    ConstraintLayout bottomConstraintLayout;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_connect_reader_activity);
        ButterKnife.bind(this);

        showScreen(ConnectReaderScreen.TURN_ON);
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.container;
    }

    @Override
    public void showScreen(@NonNull final ConnectReaderScreen screen) {
        final BaseFragment fragment;
        boolean addToBackstack = false;

        switch (screen) {
            case TURN_ON: {
                fragment = new TurnReaderOnConnectFragment();
                break;
            }
            case PERMISSION_REQUIRED: {
                addToBackstack = true;
                fragment = new PermissionRequiredOnboardingFragment();
                break;
            }
            default: {
                Timber.d("Unhandled screen: %s", screen);
                throw new IllegalArgumentException("Unhandled screen: " + screen);
            }
        }

        showFragment(fragment, addToBackstack);
    }

    @Override
    public void navigateBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }

        super.onBackPressed();
    }

    @NonNull
    @Override
    public ConnectReaderPresenter createPresenter() {
        return presenter;
    }

    protected void showFragment(final @NonNull BaseFragment fragment, final boolean addToBackstack) {
        if (getActiveBaseFragment() != null && getActiveBaseFragment().getClass().equals(fragment.getClass())) {
            return;
        }

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(getFrameLayoutId(), fragment, fragment.getClass().getSimpleName());

        if (addToBackstack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showHelpScreen() {
        final BottomSheetBehavior behavior = from(bottomConstraintLayout);
        behavior.setState(STATE_EXPANDED);
    }

    public void closeHelpScreen() {
        final BottomSheetBehavior behavior = from(bottomConstraintLayout);
        behavior.setState(STATE_COLLAPSED);
    }
}
