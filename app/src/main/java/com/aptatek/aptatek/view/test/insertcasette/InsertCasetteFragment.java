package com.aptatek.aptatek.view.test.insertcasette;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.test.TestFragmentComponent;
import com.aptatek.aptatek.view.test.base.TestBaseFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class InsertCasetteFragment extends TestBaseFragment<InsertCasetteView, InsertCasettePresenter>
    implements InsertCasetteView {

    private static final int[] IMAGES = new int[] {R.drawable.ic_casette_1, R.drawable.ic_casette_2};

    @Inject
    InsertCasettePresenter insertCasettePresenter;

    @BindView(R.id.insertcasetteImage)
    ImageView imageView;

    private final Handler animator = new Handler(Looper.getMainLooper());
    private int counter = 0;
    private volatile boolean shouldRunAnimation;

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
        return R.layout.fragment_insert_casette;
    }

    @NonNull
    @Override
    public InsertCasettePresenter createPresenter() {
        return insertCasettePresenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        shouldRunAnimation = true;

        animator.post(new ChangeSrcRunnable());
    }

    @Override
    public void onPause() {
        super.onPause();

        shouldRunAnimation = false;
    }

    private class ChangeSrcRunnable implements Runnable {

        private static final long DELAY_MILLIS = 500L;

        @Override
        public void run() {
            if (imageView != null && shouldRunAnimation) {
                imageView.setImageResource(IMAGES[counter++ / IMAGES.length]);
                animator.postDelayed(new ChangeSrcRunnable(), DELAY_MILLIS);
            }
        }
    }
}
