package com.aptatek.pkuapp.view.test.tutorial.insertsample;

import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.view.test.TestScreens;
import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;
import com.aptatek.pkuapp.view.test.tutorial.BaseTutorialFragment;

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
        insertSamplePresenter.startSampleWetting();
        showScreen(TestScreens.SAMPLE_WETTING);

        return true;
    }

    @Override
    protected int[] getImages() {
        return IMAGES;
    }

}