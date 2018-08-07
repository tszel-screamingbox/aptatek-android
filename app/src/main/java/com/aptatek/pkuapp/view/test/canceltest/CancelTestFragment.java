package com.aptatek.pkuapp.view.test.canceltest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.view.test.base.TestBaseFragment;
import com.aptatek.pkuapp.view.test.incubation.IncubationReminderService;

import javax.inject.Inject;

public class CancelTestFragment extends TestBaseFragment<CancelTestView, CancelTestPresenter>
    implements CancelTestView {

    @Inject
    CancelTestPresenter presenter;

    @Override
    protected void initObjects(final View view) {
        super.initObjects(view);

        presenter.initUi();
    }

    @Override
    protected void injectTestFragment(@NonNull final TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @NonNull
    @Override
    public CancelTestPresenter createPresenter() {
        return presenter;
    }

    @Override
    public boolean onNavigateBackPressed() {
        return false; // just let the activity pop the stack
    }

    @Override
    public boolean onNavigateForwardPressed() {
        presenter.stopTest();
        final FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.stopService(new Intent(activity, IncubationReminderService.class));
        }

        return true;
    }

}
