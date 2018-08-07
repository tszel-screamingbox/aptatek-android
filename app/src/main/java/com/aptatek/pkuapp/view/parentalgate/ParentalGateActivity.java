package com.aptatek.pkuapp.view.parentalgate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aptatek.pkuapp.injection.component.ActivityComponent;
import com.aptatek.pkuapp.view.base.BaseFragment;
import com.aptatek.pkuapp.view.base.BaseRootFrameActivity;
import com.aptatek.pkuapp.view.parentalgate.welcome.ParentalGateWelcomeFragment;

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

        showScreen(new ParentalGateWelcomeFragment());
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
    public void showScreen(@NonNull final BaseFragment fragment) {
        switchToFragment(fragment);
    }

    @Override
    public void navigateBack() {
        onBackPressed();
    }
}
