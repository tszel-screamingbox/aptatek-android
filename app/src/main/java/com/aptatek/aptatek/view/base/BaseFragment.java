package com.aptatek.aptatek.view.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aptatek.aptatek.AptatekApplication;
import com.aptatek.aptatek.injection.component.DaggerFragmentComponent;
import com.aptatek.aptatek.injection.component.FragmentComponent;
import com.aptatek.aptatek.injection.module.FragmentModule;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.UUID;

import butterknife.ButterKnife;


public abstract class BaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> implements IFragmentFactory {


    private FragmentComponent fragmentComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        injectFragment(fragmentComponent());
    }


    /**
     * Returns with the associated activity component
     *
     * @return The activity component. If not exists creates one.
     */
    protected FragmentComponent fragmentComponent() {
        if (fragmentComponent == null) {
            fragmentComponent = DaggerFragmentComponent.builder()
                    .fragmentModule(new FragmentModule(this))
                    .applicationComponent(AptatekApplication.get(getContext()).getApplicationComponent())
                    .build();
        }
        return fragmentComponent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initObjects(view);
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    /**
     * Load child fragment into a container view and save
     *
     * @param fragment        The fragment to be load
     * @param containerViewId The if of the container
     */
    protected void loadChildFragment(BaseFragment fragment, @IdRes int containerViewId) {
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
    protected void loadChildFragmentWithoutSave(BaseFragment fragment, @IdRes int containerViewId) {
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
    protected void removeChildFragment(BaseFragment fragment) {
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
    protected abstract void initObjects(final View view);

    /**
     * Handles the component to resolve the injection
     *
     * @param fragmentComponent The registered component to this fragment
     */
    protected abstract void injectFragment(FragmentComponent fragmentComponent);
}
