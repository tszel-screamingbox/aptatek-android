package com.aptatek.pkulab.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.pkulab.injection.component.DaggerBroadcastReceiverComponent;
import com.aptatek.pkulab.injection.module.ReminderModule;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Inject
    ReminderInteractor reminderInteractor;

    @Override
    public void onReceive(Context context, Intent intent) {
        DaggerBroadcastReceiverComponent.builder()
                .applicationComponent(((AptatekApplication) context.getApplicationContext()).getApplicationComponent())
                .reminderModule(new ReminderModule())
                .build()
                .inject(this);

        reminderInteractor.rescheduleReminderAfterReboot()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }
}
