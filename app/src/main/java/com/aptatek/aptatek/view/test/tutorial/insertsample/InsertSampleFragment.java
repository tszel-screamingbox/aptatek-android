package com.aptatek.aptatek.view.test.tutorial.insertsample;

import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.test.TestFragmentComponent;
import com.aptatek.aptatek.view.rangeinfo.RangeInfoActivity;
import com.aptatek.aptatek.view.test.TestScreens;
import com.aptatek.aptatek.view.test.base.TestFragmentBaseView;
import com.aptatek.aptatek.view.test.tutorial.BaseTutorialFragment;

import javax.inject.Inject;

public class InsertSampleFragment extends BaseTutorialFragment<TestFragmentBaseView, InsertSamplePresenter>
        implements TestFragmentBaseView {

    private static final int[] IMAGES = new int[]{R.drawable.ic_insert_sample_1, R.drawable.ic_insert_sample_2};

    @Inject
    InsertSamplePresenter insertSamplePresenter;

    @Override
    protected void initObjects(final View view) {
        super.initObjects(view);
        presenter.initUi();
    }

    @Override
    protected void injectTestFragment(@NonNull final TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public InsertSamplePresenter createPresenter() {
        return insertSamplePresenter;
    }

    @Override
    public boolean onNavigateForwardPressed() {
        getActivity().startActivity(RangeInfoActivity.starter(getActivity()));

        return true;
    }

    @Override
    protected int[] getImages() {
        return IMAGES;
    }

}
