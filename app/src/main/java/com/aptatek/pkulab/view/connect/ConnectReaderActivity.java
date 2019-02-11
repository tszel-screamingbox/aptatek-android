package com.aptatek.pkulab.view.connect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.injection.module.scan.ScanModule;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.connect.permission.PermissionRequiredFragment;
import com.aptatek.pkulab.view.connect.turnon.TurnReaderOnConnectFragment;

import javax.inject.Inject;

import timber.log.Timber;

public class ConnectReaderActivity extends BaseActivity<ConnectReaderView, ConnectReaderPresenter> implements ConnectReaderView {

    public static Intent starter(Context context) {
        return new Intent(context, ConnectReaderActivity.class);
    }

    @Inject
    ConnectReaderPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connect);

        showScreen(ConnectReaderScreen.TURN_ON);
    }

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        activityComponent.plus(new ScanModule()).inject(this);
    }

    @Override
    public int getFrameLayoutId() {
        return R.id.connectFrame;
    }

    @Override
    public void showScreen(@NonNull ConnectReaderScreen screen) {
        final BaseFragment fragment;
        boolean addToBackstack = false;

        switch (screen) {
            case TURN_ON: {
                fragment = new TurnReaderOnConnectFragment();
                break;
            }
            case PERMISSION_REQUIRED: {
                addToBackstack = true;
                fragment = new PermissionRequiredFragment();
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

}
