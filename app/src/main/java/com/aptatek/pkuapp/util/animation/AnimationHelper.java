package com.aptatek.pkuapp.util.animation;

import android.animation.Animator;
import android.view.View;

import javax.inject.Inject;

public final class AnimationHelper {

    private static final int DURATION_MILLISEC = 200;
    private static final float SCALE_MIN = 1f;


    @Inject
    public AnimationHelper() {
    }

    public void zoomIn(final View view, final AnimationCallback callback) {
        zoom(view, callback, SCALE_MIN * 2f);
    }

    public void zoomOut(final View view, final AnimationCallback callback) {
        zoom(view, callback, SCALE_MIN);
    }

    private void zoom(final View view, final AnimationCallback callback, final float scale) {
        view.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(DURATION_MILLISEC)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(final Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(final Animator animation) {
                        callback.animationEnd();
                    }

                    @Override
                    public void onAnimationCancel(final Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(final Animator animation) {

                    }
                })
                .start();
    }
}