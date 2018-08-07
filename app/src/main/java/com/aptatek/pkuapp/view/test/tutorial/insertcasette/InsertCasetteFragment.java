package com.aptatek.pkuapp.view.test.tutorial.insertcasette;

import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.view.test.TestScreens;
import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;
import com.aptatek.pkuapp.view.test.tutorial.BaseTutorialFragment;

import javax.inject.Inject;

public class InsertCasetteFragment extends BaseTutorialFragment<TestFragmentBaseView, InsertCasettePresenter>
        implements TestFragmentBaseView {

    private static final int[] IMAGES = new int[]{R.drawable.ic_casette_1, R.drawable.ic_casette_2};

    @Inject
    InsertCasettePresenter insertCasettePresenter;

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
    public InsertCasettePresenter createPresenter() {
        return insertCasettePresenter;
    }

    @Override
    protected int[] getImages() {
        return IMAGES;
    }

    @Override
    public boolean onNavigateForwardPressed() {
        showScreen(TestScreens.ATTACH_CUBE);

        return true;
    }
}
