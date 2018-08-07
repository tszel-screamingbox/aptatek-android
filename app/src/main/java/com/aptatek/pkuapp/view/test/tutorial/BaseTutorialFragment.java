package com.aptatek.pkuapp.view.test.tutorial;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.view.test.TestScreens;
import com.aptatek.pkuapp.view.test.base.TestBaseFragment;
import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

import butterknife.BindView;

public abstract class BaseTutorialFragment<V extends TestFragmentBaseView, P extends MvpPresenter<V>> extends TestBaseFragment<V, P> {

    @BindView(R.id.tutorialImage)
    ImageView imageView;

    private final Handler animator = new Handler(Looper.getMainLooper());
    private int counter = 0;
    private volatile boolean shouldRunAnimation;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_tutorial_animation;
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

    @Override
    public boolean onNavigateBackPressed() {
        showScreen(TestScreens.CANCEL);

        return true;
    }

    protected abstract int[] getImages();

    private class ChangeSrcRunnable implements Runnable {

        private static final long DELAY_MILLIS = 500L;

        @Override
        public void run() {
            if (imageView != null && shouldRunAnimation) {
                final int[] images = getImages();
                imageView.setImageResource(images[counter++ % images.length]);
                animator.postDelayed(new ChangeSrcRunnable(), DELAY_MILLIS);
            }
        }
    }

}
