package com.aptatek.pkulab.view.test.turnreaderon.permission;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.connect.permission.PermissionRequiredFragment;
import com.aptatek.pkulab.view.connect.permission.PermissionRequiredView;

import javax.inject.Inject;

public class PermissionRequiredOnTestFragment extends PermissionRequiredFragment<PermissionRequiredView, PermissionRequiredOnTestPresenter> {

    @Inject
    PermissionRequiredOnTestPresenter presenter;

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public void onConditionsMet() {
        if (getActivity() instanceof PermissionRequiredOnTestView) {
            ((PermissionRequiredOnTestView) getActivity()).onConditionsMet();
        }
    }

    @NonNull
    @Override
    public PermissionRequiredOnTestPresenter createPresenter() {
        return presenter;
    }
}
