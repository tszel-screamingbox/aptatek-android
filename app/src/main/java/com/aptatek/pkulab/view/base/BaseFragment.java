package com.aptatek.pkulab.view.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.domain.manager.analytic.EventCategory;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.injection.component.DaggerFragmentComponent;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.injection.module.FragmentModule;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.uxcam.UXCam;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ix.Ix;


public abstract class BaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> implements IFragmentFactory {

    private FragmentComponent fragmentComponent;

    @Inject
    IAnalyticsManager analyticManager;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        injectFragment(getFragmentComponent());
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * Returns with the associated activity component
     *
     * @return The activity component. If not exists creates one.
     */
    protected FragmentComponent getFragmentComponent() {
        if (fragmentComponent == null) {
            fragmentComponent = DaggerFragmentComponent.builder()
                    .fragmentModule(new FragmentModule(this))
                    .applicationComponent(AptatekApplication.get(getContext()).getApplicationComponent())
                    .build();
        }
        return fragmentComponent;
    }

    protected abstract List<View> sensitiveViewList();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initObjects(view);
        Ix.from(sensitiveViewList()).foreach(UXCam::occludeSensitiveViewWithoutGesture);
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public void logEvent(final String eventName, final String eventInfo, final EventCategory category) {
        analyticManager.logEvent(eventName, eventInfo, category);
    }

    public void logEllapsedTime(final String eventName, final int seconds, final EventCategory category) {
        analyticManager.logElapsedTime(eventName, seconds, category);
    }

    /**
     * Load child fragment into a container view and save
     *
     * @param fragment        The fragment to be load
     * @param containerViewId The if of the container
     */
    protected void loadChildFragment(final BaseFragment fragment, @IdRes final int containerViewId) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment)
                .addToBackStack(UUID.randomUUID().toString())
                .commitAllowingStateLoss();
    }

    /**
     * Load child fragment into a container view without adding to backstack
     *
     * @param fragment        The fragment to be load
     * @param containerViewId The if of the container
     */
    protected void loadChildFragmentWithoutSave(final BaseFragment fragment, @IdRes final int containerViewId) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment)
                .commitAllowingStateLoss();
    }

    /**
     * Removes a child fragment from the manager
     *
     * @param fragment The fragment to be removed
     */
    protected void removeChildFragment(final BaseFragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
        getChildFragmentManager().popBackStack();
    }

    /**
     * Creates a fragment for the actual model
     *
     * @return A new fragment
     */
    @Override
    public BaseFragment getFragment() {
        return this;
    }


    /**
     * Indicates whether the fragment uses it's own back stack
     *
     * @return True if handled itself, false otherwise
     */
    public boolean onBackPressed() {
        if (getChildFragmentManager().getBackStackEntryCount() >= 1) {
            getChildFragmentManager().popBackStack();
            return true;
        }
        return false;
    }

    /**
     * Returns the title of the fragment
     *
     * @return Title of the fragment
     */
    public abstract String getTitle();

    /**
     * The layout of the fragment
     *
     * @return The associated layout id
     */
    protected abstract int getLayoutId();

    /**
     * Other (non view typed) fields (such as adapters) should be initialized here.
     *
     * @param view Root view (which will be returned in onCreateView)
     */
    protected abstract void initObjects(View view);

    /**
     * Handles the component to resolve the injection
     *
     * @param fragmentComponent The registered component to this fragment
     */
    protected abstract void injectFragment(FragmentComponent fragmentComponent);
}
