package com.aptatek.aptatek.view.test.incubation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

import com.aptatek.aptatek.AptatekApplication;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationNotRunningError;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationNotificationFactory;
import com.aptatek.aptatek.domain.model.Countdown;
import com.aptatek.aptatek.injection.component.test.DaggerTestServiceComponent;
import com.aptatek.aptatek.injection.component.test.TestServiceComponent;
import com.aptatek.aptatek.injection.module.ServiceModule;
import com.aptatek.aptatek.injection.module.test.TestModule;
import com.aptatek.aptatek.util.Constants;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class IncubationReminderService extends Service {

    private static final int COUNTDOWN_NOTIFICATION_ID = 462;
    private static final int FINISHED_NOTIFICATION_ID = 346;
    private static final int ERROR_NOTIFICATION_ID = 377;

    @Inject
    IncubationInteractor incubationInteractor;

    @Inject
    IncubationNotificationFactory incubationNotificationFactory;

    @Inject
    NotificationManagerCompat notificationManager;

    private Disposable disposable;

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final TestServiceComponent serviceComponent = DaggerTestServiceComponent.builder()
                .applicationComponent(((AptatekApplication) getApplication()).getApplicationComponent())
                .testModule(new TestModule())
                .serviceModule(new ServiceModule(this))
                .build();
        serviceComponent.inject(this);

        disposable = incubationInteractor.getIncubationCountdown()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    countdown -> {
                        Timber.d("Countdown: %s", countdown);
                        notificationManager.notify(COUNTDOWN_NOTIFICATION_ID, incubationNotificationFactory.createCountdownNotification(countdown));
                    },
                    error -> {
                        Timber.d("Countdown error: %s", error.toString());
                        stopForeground(false);
                        if (!(error instanceof IncubationNotRunningError)) {
                            notificationManager.notify(ERROR_NOTIFICATION_ID, incubationNotificationFactory.createErrorNotification(error));
                        }
                        stopSelf();
                    },
                    () -> {
                        Timber.d("Countdown complete");
                        stopForeground(false);
                        notificationManager.notify(FINISHED_NOTIFICATION_ID, incubationNotificationFactory.createFinishedNotification());
                        stopSelf();
                    });


        startForeground(COUNTDOWN_NOTIFICATION_ID, incubationNotificationFactory.createCountdownNotification(
                Countdown.builder()
                        .setRemainingFormattedText("30:00")
                        .setRemainingMillis(Constants.DEFAULT_INCUBATION_PERIOD)
                        .build()));
    }

    @Override
    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        super.onDestroy();
    }
}
