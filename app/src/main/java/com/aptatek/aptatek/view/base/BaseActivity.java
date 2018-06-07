package com.aptatek.aptatek.view.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.aptatek.aptatek.AptatekApplication;
import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.injection.component.DaggerActivityComponent;
import com.aptatek.aptatek.injection.module.ActivityModule;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;


public abstract class BaseActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpActivity<V, P> implements IActivityComponentProvider {


    private ActivityComponent activityComponent;

    public enum Animation {FADE, SLIDE, RIGHT_TO_LEFT, LEFT_TO_RIGHT}

    /**
     * Handles the component to resolve the injection
     *
     * @param activityComponent The registered component to this activity
     */
    protected abstract void injectActivity(ActivityComponent activityComponent);


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        injectActivity(getActivityComponent());
        super.onCreate(savedInstanceState);

    }

    /**
     * Returns with the associated activity component
     *
     * @return The activity component. If not exists creates one.
     */
    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(AptatekApplication.get(this).getApplicationComponent())
                    .build();
        }
        return activityComponent;
    }

    public void launchActivity(final Intent intent, final boolean clearHistory, final Animation changeAnimation) {
        if (clearHistory) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
        setTransitionAnimation(changeAnimation);
    }

    public void slideToFragment(final BaseFragment fragment) {
        switchToFragmentWithTransition(fragment, false, null, true);
    }

    public void switchToFragment(final BaseFragment fragment) {
        switchToFragmentWithTransition(fragment, false, null, false);
    }

    public void switchToFragment(final BaseFragment fragment, final boolean finishCurrentFragment) {
        switchToFragmentWithTransition(fragment, finishCurrentFragment, null, false);
    }

    public void switchToFragment(final BaseFragment fragment, final String tag) {
        switchToFragmentWithTransition(fragment, false, tag, false);
    }

    public void switchToFragmentWithTransition(final BaseFragment fragment, final boolean finishCurrentFragment, final String name, final boolean smoothly) {
        final FragmentManager fm = getSupportFragmentManager();

        final String tag;
        if (name == null) {
            tag = fragment.getClass().getName();
        } else {
            tag = name;
        }


        if (finishCurrentFragment && !isFinishing()) {
            try {
                fm.popBackStackImmediate();
            } catch (IllegalStateException e) {
                return;
            }
        }

        final boolean fragmentExists = fm.findFragmentByTag(tag) != null;
        if (fragmentExists) {
            fm.popBackStackImmediate(tag, 0);
        } else {
            final FragmentTransaction transaction = fm.beginTransaction();
            if (smoothly) {
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            transaction.replace(getFrameLayoutId(), fragment, tag);
            transaction.addToBackStack(tag);
            transaction.commitAllowingStateLoss();
        }
    }

    public BaseFragment getActiveBaseFragment() {
        final Fragment fragment = getSupportFragmentManager().findFragmentById(getFrameLayoutId());
        if (fragment instanceof BaseFragment) {
            return (BaseFragment) fragment;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        final BaseFragment currentFragment = getActiveBaseFragment();
        if (currentFragment != null) {
            if (!currentFragment.onBackPressed()) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    public abstract int getFrameLayoutId();

    public void setTransitionAnimation(final Animation animation) {
        switch (animation) {
            case FADE:
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case SLIDE:
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case RIGHT_TO_LEFT:
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                break;
            case LEFT_TO_RIGHT:
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            default:
                break;
        }
    }

}
