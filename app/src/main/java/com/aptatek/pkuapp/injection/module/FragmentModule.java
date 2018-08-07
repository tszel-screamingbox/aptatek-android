package com.aptatek.pkuapp.injection.module;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.aptatek.pkuapp.injection.qualifier.ActivityContext;
import com.aptatek.pkuapp.injection.qualifier.ChildFragmentManager;
import com.aptatek.pkuapp.view.base.BaseFragment;

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
