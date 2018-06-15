package com.aptatek.aptatek.view.test.tutorial.insertsample;

import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.test.TestFragmentComponent;
import com.aptatek.aptatek.view.test.base.TestFragmentBaseView;
import com.aptatek.aptatek.view.test.tutorial.BaseTutorialFragment;

import javax.inject.Inject;

public class InsertSampleFragment extends BaseTutorialFragment<InsertSamplePresenter, TestFragmentBaseView>
        implements TestFragmentBaseView {

    private static final int[] IMAGES = new int[]{R.drawable.ic_casette_1, R.drawable.ic_casette_2};

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
    protected int[] getImages() {
        return IMAGES;
    }

}
