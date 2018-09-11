package com.aptatek.pkuapp.view.test.breakfoil;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.model.AlertDialogModel;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.view.dialog.AlertDialogFragment;
import com.aptatek.pkuapp.view.test.base.TestBaseFragment;

import javax.inject.Inject;

public class BreakFoilFragment extends TestBaseFragment<BreakFoilView, BreakFoilPresenter> implements BreakFoilView {

    private  static final String TAG_ALERT = "alert";

    @Inject
    BreakFoilPresenter presenter;

    @Override
    protected void injectTestFragment(final TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_video;
    }

    @Override
    public void showAlert(@NonNull final AlertDialogModel alertDialogModel) {
        final AlertDialogFragment alertDialogFragment = AlertDialogFragment.create(alertDialogModel, null);
        alertDialogFragment.show(getChildFragmentManager(), TAG_ALERT);
    }

    @NonNull
    @Override
    public BreakFoilPresenter createPresenter() {
        return presenter;
    }
}
