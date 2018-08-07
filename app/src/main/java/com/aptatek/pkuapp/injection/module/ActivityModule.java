package com.aptatek.pkuapp.injection.module;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.aptatek.pkuapp.injection.qualifier.ActivityContext;
import com.aptatek.pkuapp.injection.qualifier.SupportFragmentManager;

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
