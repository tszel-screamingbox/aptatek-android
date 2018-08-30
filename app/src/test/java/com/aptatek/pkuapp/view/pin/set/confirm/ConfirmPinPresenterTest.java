package com.aptatek.pkuapp.view.pin.set.confirm;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.data.PinCode;
import com.aptatek.pkuapp.device.DeviceHelper;
import com.aptatek.pkuapp.domain.interactor.auth.AuthInteractor;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
 * @test.layer View / Pin
 * @test.feature PIN validation
 * @test.type Unit tests
 */
public class ConfirmPinPresenterTest {

    @Mock
    private AuthInteractor authInteractor;

    @Mock
    private DeviceHelper deviceHelper;

    @Mock
    private ConfirmPinView view;

    private ConfirmPinPresenter presenter;
    private PinCode validPin = new PinCode("valid".getBytes());
    private PinCode invalidPin = new PinCode("invalid".getBytes());

    /**
     * Setting up the required presenter
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new ConfirmPinPresenter(authInteractor, deviceHelper);
        presenter.attachView(view);
    }

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
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> immediate);
    }

    /**
     * Reset RxJava components after testing.
     */
    @AfterClass
    public static void afterClass() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    /**
     * Comparing two, equal PIN codes.
     *
     * @test.expected The given PINs are the same, {@link  ConfirmPinView#onValidPinTyped()  onValidPinTyped}
     * method is called, without any error.
     */
    @Test
    public void testValidPin() {
        when(authInteractor.setPinCode(validPin)).thenReturn(Completable.complete());
        presenter.verifyPin(validPin, validPin);
        verify(view).onValidPinTyped();
    }

    /**
     * Comparing two, different PIN codes.
     *
     * @test.expected The given PIN codes are different, {@link  ConfirmPinView#onInvalidPinTyped()  onInvalidPinTyped}
     * method is called, without any error.
     */
    @Test
    public void testInvalidPin() {
        presenter.verifyPin(validPin, invalidPin);
        verify(view).onInvalidPinTyped();
    }

    /**
     * Enable finterpint authentication.
     *
     * @test.expected {@link  ConfirmPinView#onFingerprintActivityShouldLoad()  onFingerprintActivityShouldLoad}
     * method is called, without any error.
     */
    @Test
    public void testEnableFingerprint() {
        when(deviceHelper.hasEnrolledFingerprints()).thenReturn(true);
        when(deviceHelper.hasFingerprintHadrware()).thenReturn(true);
        presenter.navigateForward();
        verify(view).onFingerprintActivityShouldLoad();
    }

    /**
     * Disable finterpint authentication.
     *
     * @test.expected {@link  ConfirmPinView#onMainActivityShouldLoad()  onMainActivityShouldLoad}
     * method is called, without any error.
     */
    @Test
    public void testDisableFingerprint() {
        when(deviceHelper.hasEnrolledFingerprints()).thenReturn(false);
        when(deviceHelper.hasFingerprintHadrware()).thenReturn(false);
        presenter.navigateForward();
        verify(view).onMainActivityShouldLoad();
    }

    /**
     * Fingerprint scanner detected, but the device hasn't got enrolled fingerprints.
     *
     * @test.expected {@link  ConfirmPinView#onMainActivityShouldLoad()  onMainActivityShouldLoad}
     * method is called, without any error.
     */
    @Test
    public void testHasScannerButNoFingerprint() {
        when(deviceHelper.hasEnrolledFingerprints()).thenReturn(false);
        when(deviceHelper.hasFingerprintHadrware()).thenReturn(true);
        presenter.navigateForward();
        verify(view).onMainActivityShouldLoad();
    }
}