package com.aptatek.pkuapp.view.test.tutorial.attachcube;

import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.injection.component.test.TestFragmentComponent;
import com.aptatek.pkuapp.view.test.TestScreens;
import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;
import com.aptatek.pkuapp.view.test.tutorial.BaseTutorialFragment;

import javax.inject.Inject;

public class AttachCubeFragment extends BaseTutorialFragment<TestFragmentBaseView, AttachCubePresenter>
    implements TestFragmentBaseView {

    private static final int[] IMAGES = new int[]{R.drawable.ic_attach_cube_1, R.drawable.ic_attach_cube_2};

    @Inject
    AttachCubePresenter attachCubePresenter;

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
    public AttachCubePresenter createPresenter() {
        return attachCubePresenter;
    }

    @Override
    protected int[] getImages() {
        return IMAGES;
    }

    @Override
    public boolean onNavigateForwardPressed() {
        showScreen(TestScreens.INSERT_SAMPLE);

        return true;
    }

}
