package com.aptatek.aptatek.view.test.tutorial;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.view.test.base.TestBaseFragment;
import com.aptatek.aptatek.view.test.base.TestFragmentBaseView;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

import butterknife.BindView;

public abstract class BaseTutorialFragment<P extends MvpPresenter<V>, V extends TestFragmentBaseView> extends TestBaseFragment<V, P> {

    @BindView(R.id.insertcasetteImage)
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
