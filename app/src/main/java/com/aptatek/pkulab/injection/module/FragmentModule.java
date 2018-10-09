package com.aptatek.pkulab.injection.module;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.aptatek.pkulab.injection.qualifier.ActivityContext;
import com.aptatek.pkulab.injection.qualifier.ChildFragmentManager;
import com.aptatek.pkulab.view.base.BaseFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {
    private final BaseFragment fragment;

    public FragmentModule(final BaseFragment fragment) {
        this.fragment = fragment;
    }

    @ChildFragmentManager
    @Provides
    public FragmentManager provideFragmentManager() {
        return fragment.getChildFragmentManager();
    }

    @ActivityContext
    @Provides
    public Context provideContext() {
        return fragment.getContext();
    }
}
