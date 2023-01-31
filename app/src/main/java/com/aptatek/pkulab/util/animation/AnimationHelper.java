package com.aptatek.pkulab.util.animation;

import static com.aptatek.pkulab.util.animation.AnimationHelper.Fade.IN;
import static com.aptatek.pkulab.util.animation.AnimationHelper.Fade.OUT;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import javax.inject.Inject;

public final class AnimationHelper {

    private static final float SCALE_MAX = 2f;
    private static final float SCALE_MIN = 0.8f;
    private static final long DURATION_MILLISEC = 200L;

    @Inject
    public AnimationHelper() {
    }

    public void animateConstraintWidthAndHeigth(@NonNull final View view, @NonNull final AnimationCallback callback, final float toPercent) {
        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        final ValueAnimator animator = ValueAnimator.ofFloat(layoutParams.matchConstraintPercentWidth, toPercent);
        animator.addUpdateListener(animation -> {
            layoutParams.matchConstraintPercentWidth = (float) animation.getAnimatedValue();
            layoutParams.matchConstraintPercentHeight = (float) animation.getAnimatedValue();
            view.requestLayout();
        });
        animator.setDuration(DURATION_MILLISEC);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                callback.animationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public enum Fade {
        IN,
        OUT
    }

    public void zoomIn(final View view, final AnimationCallback callback) {
        zoom(view, callback, SCALE_MAX);
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

    public void fadeIn(final View view, final int duration, final AnimationCallback callback) {
        fade(view, IN, duration, callback);
    }

    public void fadeOut(final View view, final int duration, final AnimationCallback callback) {
        fade(view, OUT, duration, callback);
    }

    private void fade(final View view, final Fade type, final int duration, final AnimationCallback callback) {
        if (type == IN) {
            view.setVisibility(View.VISIBLE);
        }

        final Animation fade = getAlphaAnim(type);
        fade.setDuration(duration);
        fade.setFillAfter(true);
        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                if (type == OUT) {
                    view.setVisibility(View.INVISIBLE);
                }
                callback.animationEnd();
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });

        final AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(fade);
        view.startAnimation(animSet);
    }

    private AlphaAnimation getAlphaAnim(final Fade direction) {
        switch (direction) {
            case IN:
                return new AlphaAnimation(0f, 1f);
            case OUT:
                return new AlphaAnimation(1f, 0f);
            default:
                return new AlphaAnimation(0f, 0f);
        }
    }
}
