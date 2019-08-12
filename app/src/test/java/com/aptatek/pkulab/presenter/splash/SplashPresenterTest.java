package com.aptatek.pkulab.presenter.splash;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.manager.keystore.KeyStoreManager;
import com.aptatek.pkulab.view.splash.SplashActivityPresenter;
import com.aptatek.pkulab.view.splash.SplashActivityView;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @test.layer View / Splash
 * @test.feature SplashScreen
 * @test.type Unit tests
 */
public class SplashPresenterTest {

    @Mock
    KeyStoreManager keyStoreManager;

    @Mock
    PreferenceManager preferenceManager;

    @Mock
    ReminderInteractor reminderInteractor;

    @Mock
    DeviceHelper deviceHelper;

    @Mock
    SplashActivityView view;

    @Mock
    File dbFile;

    @Mock
    IAnalyticsManager analyticsManager;

    private SplashActivityPresenter presenter;

    /**
     * Initialize RxJava components before testing.
     */
    @BeforeClass
    public static void beforeClass() {
        final Scheduler immediate = new Scheduler() {

            @Override
            public Disposable scheduleDirect(@NonNull final Runnable run, final long delay, @NonNull final TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run, true);
            }
        };

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> immediate);
    }

    @AfterClass
    public static void afterClass() {
        RxJavaPlugins.reset();
    }

    /**
     * Setting up the required presenter and defining mocked component's behaviour
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(reminderInteractor.initializeDays()).thenReturn(Completable.complete());
        when(deviceHelper.isRooted()).thenReturn(false);
        when(dbFile.exists()).thenReturn(true);
        when(preferenceManager.isDbEncryptedWithPin()).thenReturn(true);

        presenter = new SplashActivityPresenter(keyStoreManager, preferenceManager, deviceHelper, dbFile, analyticsManager);
        presenter.attachView(view);
    }

    /**
     * Testing navigation to parental gate screen, when the user hasn't passed any phase of registration
     *
     * @test.expected {@link  SplashActivityView#onParentalGateShouldLoad()  onParentalGateShouldLoad()}
     * method is called and ParentalGateActivity is loaded, without any error.
     */
    @Test
    public void testNavigationToParentalGate() {
        when(preferenceManager.isParentalPassed()).thenReturn(false);
        verify(view).onParentalGateShouldLoad();
    }

    /**
     * Testing detect rooted device
     *
     * @test.expected {@link  SplashActivityView#showAlertDialog(Integer, Integer)}
     * method is called, without any error.
     */
    @Test
    public void testRootedDevice() {
        when(deviceHelper.isRooted()).thenReturn(true);
        presenter.attachView(view);
        verify(view).showAlertDialog(R.string.splash_root_alert_title, R.string.splash_root_alert);
    }

    /**
     * Testing navigation to set PIN screen, when the user passed ParentalGate, but hasn't set PIN code yet
     *
     * @test.expected {@link  SplashActivityView#onSetPinActivityShouldLoad()  onSetPinActivityShouldLoad()}
     * method is called and SetPinHostActivity is loaded, without any error.
     */
    @Test
    public void testNavigationToPinSetup() {
        when(preferenceManager.isParentalPassed()).thenReturn(true);
        when(keyStoreManager.aliasExists()).thenReturn(false);

        presenter.switchToNextActivity();
        verify(view).onSetPinActivityShouldLoad();
    }

    /**
     * Testing navigation to request PIN screen, when the user has a valid registration
     *
     * @test.expected {@link  SplashActivityView#onRequestPinActivityShouldLoad()  onRequestPinActivityShouldLoad()}
     * method is called and AuthPinHostActivity is loaded, without any error.
     */
    @Test
    public void testNavigationToPinCheck() {
        when(preferenceManager.isParentalPassed()).thenReturn(true);
        when(keyStoreManager.aliasExists()).thenReturn(true);

        presenter.switchToNextActivity();
        verify(view).onRequestPinActivityShouldLoad();
    }

}
