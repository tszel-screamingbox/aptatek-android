package com.aptatek.aptatek.view.parentalgate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseFragment;
import com.aptatek.aptatek.view.base.BaseRootFrameActivity;
import com.aptatek.aptatek.view.parentalgate.welcome.ParentalGateWelcomeFragment;

import javax.inject.Inject;

public class ParentalGateActivity extends BaseRootFrameActivity<ParentalGateView, ParentalGatePresenter>
    implements ParentalGateView {

    public static Intent starter(@NonNull final Context context) {
        return new Intent(context, ParentalGateActivity.class);
    }

    @Inject
    ParentalGatePresenter presenter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showScreen(ParentalGateScreens.AGE_CHECK);
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @NonNull
    @Override
    public ParentalGatePresenter createPresenter() {
        return presenter;
    }

    @Override
    public void showScreen(@NonNull final ParentalGateScreens screen) {
        final BaseFragment fragment;

        switch (screen) {
            case RESULT: {
                fragment = new ParentalGateWelcomeFragment();
                break;
            }
            case AGE_CHECK:
            default: {
                fragment = null;
                break;
            }
        }

        switchToFragment(fragment);
    }
}
