package com.aptatek.pkulab.view.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.injection.component.DaggerActivityComponent;
import com.aptatek.pkulab.injection.module.ActivityModule;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisionListener;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisions;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.pin.auth.AuthPinHostActivityStarter;
import com.aptatek.pkulab.view.test.TestActivity;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import javax.inject.Inject;

public abstract class BaseActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpActivity<V, P> implements IActivityComponentProvider, AlertDialogDecisionListener {

    private static final String ALERT_DIALOG_FRAGMENT_TAG = "alertDialogFragmentTag";
    private ActivityComponent activityComponent;

    @Inject
    IAnalyticsManager analyticManager;

    private BroadcastReceiver reminderDialogBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            AlertDialogFragment.create(AlertDialogModel.builder()
                    .setPositiveButtonText(getString(R.string.reminder_notification_half_hour))
                    .setNegativeButtonText(getString(R.string.reminder_notification_quarter_hour))
                    .setNeutralButtonText(getString(R.string.reminder_notification_now))
                    .setMessage(getString(R.string.reminder_notification_message))
                    .setTitle(getString(R.string.reminder_notification_title))
                    .setTheme(R.style.ReminderSnoozeDialogTheme)
                    .setPositiveButtonTextColor(R.color.applicationPink)
                    .setNegativeButtonTextColor(R.color.applicationPink)
                    .setNeutralButtonTextColor(R.color.applicationPink)
                    .setCancelable(true)
                    .build(), BaseActivity.this).show(getSupportFragmentManager(), ALERT_DIALOG_FRAGMENT_TAG);
        }
    };

    private final BroadcastReceiver requestPinReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (shouldShowPinAuthWhenInactive()) {
                AuthPinHostActivityStarter.start(BaseActivity.this, true);
            }
        }
    };

    @Override
    public void onDecision(@NonNull final AlertDialogDecisions decision) {
        if (decision == AlertDialogDecisions.NEUTRAL) {
            startActivity(TestActivity.createStarter(this));
        } else if (decision == AlertDialogDecisions.POSITIVE) {
            AptatekApplication.get(this).getAlarmManager().scheduleSnooze(30);
        } else {
            AptatekApplication.get(this).getAlarmManager().scheduleSnooze(15);
        }
    }


    public enum Animation {FADE, SLIDE, RIGHT_TO_LEFT, LEFT_TO_RIGHT, BOTTOM_TO_TOP}

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

    @Override
    protected void onStart() {
        super.onStart();

        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager
                .registerReceiver(reminderDialogBroadcast, new IntentFilter(Constants.REMINDER_DIALOG_BROADCAST_NAME));
        broadcastManager.registerReceiver(requestPinReceiver, new IntentFilter(Constants.PIN_IDLE_ACTION));
    }

    @Override
    protected void onStop() {
        final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager
                .unregisterReceiver(reminderDialogBroadcast);
        broadcastManager.unregisterReceiver(requestPinReceiver);

        super.onStop();
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

    public void logEvent(final String eventName, final String eventInfo, final EventCategory category) {
        analyticManager.logEvent(eventName, eventInfo, category);
    }

    public void logEllapsedTime(final String eventName, final int seconds, final EventCategory category){
        analyticManager.logElapsedTime(eventName, seconds, category);
    }

    public void launchActivity(final Intent intent) {
        launchActivity(intent, false, Animation.RIGHT_TO_LEFT);
    }

    public void launchActivity(final Intent intent, final boolean clearHistory, final Animation changeAnimation) {
        if (clearHistory) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
        if (changeAnimation != null) {
            setTransitionAnimation(changeAnimation);
        }
    }

    public void launchActivityForResult(final Intent intent, final Animation changeAnimation, final int requestCode) {
        startActivityForResult(intent, requestCode);
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
            } catch (final IllegalStateException e) {
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

    protected void clearFragmentStack() {
        final FragmentManager fm = getSupportFragmentManager();
        while (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
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
        getSupportFragmentManager().executePendingTransactions();
        final BaseFragment currentFragment = getActiveBaseFragment();
        if (currentFragment != null) {
            if (!currentFragment.onBackPressed()) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStackImmediate();
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
            case BOTTOM_TO_TOP:
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            default:
                break;
        }
    }

    protected boolean shouldShowPinAuthWhenInactive() {
        return true;
    }

}
