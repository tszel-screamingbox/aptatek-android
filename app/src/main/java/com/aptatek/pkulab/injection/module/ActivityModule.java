package com.aptatek.pkulab.injection.module;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.aptatek.pkulab.injection.qualifier.ActivityContext;
import com.aptatek.pkulab.injection.qualifier.SupportFragmentManager;

import dagger.Module;
import dagger.Provides;


@Module
public class ActivityModule {
    private final AppCompatActivity activity;

    public ActivityModule(final AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    public Activity provideActivity() {
        return activity;
    }

    @ActivityContext
    @Provides
    public Context provideContext() {
        return activity;
    }

    @SupportFragmentManager
    @Provides
    public FragmentManager provideFragmentManager() {
        return activity.getSupportFragmentManager();
    }
}
