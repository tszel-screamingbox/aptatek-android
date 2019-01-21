package com.aptatek.pkulab.view.pin.auth.add;

import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.PinCode;
import com.aptatek.pkulab.device.DeviceHelper;
import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.auth.AuthInteractor;
import com.aptatek.pkulab.domain.interactor.auth.Callback;
import com.aptatek.pkulab.domain.manager.keystore.KeyStoreError;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @test.layer View / Pin
 * @test.feature Setting PIN code
 * @test.type Unit tests
 */
public class AuthPinPresenterTest {

    @Mock
    private AuthInteractor authInteractor;

    @Mock
    private DeviceHelper deviceHelper;

    @Mock
    private ResourceInteractor resourceInteractor;

    @Mock
    private AuthPinView view;

    private AuthPinPresenter presenter;
    private PinCode validPin = new PinCode("valid".getBytes());
    private PinCode invalidPin = new PinCode("invalid".getBytes());

    /**
     * Setting up the required presenter and defining mocked component's behaviour
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(authInteractor.checkPinCode(validPin)).thenReturn(Completable.complete());
        when(authInteractor.checkPinCode(invalidPin)).thenReturn(Completable.error(new KeyStoreError(new Throwable())));

        presenter = new AuthPinPresenter(authInteractor, deviceHelper, resourceInteractor);
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
     * Fingerprint authentication is enabled, shows it on the screen.
     *
     * @test.expected {@link  AuthPinView#onFingerprintAvailable()  onFingerprintAvailable()}
     * method is called, without any error.
     */
    @Test
    public void testWhenFingerprintEnable() {
        when(deviceHelper.isFingperprintAuthAvailable()).thenReturn(true);
        presenter.initView();
        verify(view).onFingerprintAvailable();
    }

    /**
     * Fingerprint authentication is disable, shows it on the screen.
     *
     * @test.expected {@link  AuthPinView#onFingerprintDisabled()  onFingerprintDisabled()}
     * method is called, without any error.
     */
    @Test
    public void testWhenFingerprintDisable() {
        when(deviceHelper.isFingperprintAuthAvailable()).thenReturn(false);
        presenter.initView();
        verify(view).onFingerprintDisabled();
    }

    /**
     * Enter a valid PIN code.
     *
     * @test.expected The PIN is valid,{@link  AuthPinView#onValidPinTyped()  onValidPinTyped()}
     * method is called, without any error.
     */
    @Test
    public void testValidPin() {
        presenter.verifyPinCode(validPin);
        verify(view).onValidPinTyped();
    }

    /**
     * Enter an invalid PIN code.
     *
     * @test.expected The PIN is invalid,{@link  AuthPinView#onInvalidPinTyped()  onInvalidPinTyped()}
     * method is called, without any error.
     */
    @Test
    public void testInvalidPin() {
        presenter.verifyPinCode(invalidPin);
        verify(view).onInvalidPinTyped();
    }

    /**
     * Enter an invalid PIN code 5 times.
     *
     * @test.expected The PIN is invalid,{@link  AuthPinView#onInvalidPinTyped()  onInvalidPinTyped()}
     * method is called four times, and then {@link  AuthPinView#showAlertDialog()  showAlertDialog()} method is called, without any error.
     */
    @Test
    public void testInvalidPin5thTimes() {
        presenter.verifyPinCode(invalidPin);
        presenter.verifyPinCode(invalidPin);
        presenter.verifyPinCode(invalidPin);
        presenter.verifyPinCode(invalidPin);
        presenter.verifyPinCode(invalidPin);

        verify(view, times(4)).onInvalidPinTyped();
        verify(view).showAlertDialog();
    }

    /**
     * Valid fingerprint detection.
     *
     * @test.expected The detected fingerprint is valid,{@link  AuthPinView#onValidFingerprintDetected()  onValidFingerprintDetected()}
     * method is called, without any error.
     */
    @Test
    public void testValidFingerprintDetected() {
        doAnswer(answerVoid(Callback::onSucceeded))
                .when(authInteractor).listenFingerPrintScanner(any(Callback.class));

        when(deviceHelper.isFingperprintAuthAvailable()).thenReturn(true);
        presenter.startListening();
        verify(view).onValidFingerprintDetected();
    }

    /**
     * Invalid fingerprint detection.
     *
     * @test.expected The detected fingerprint is invalid,{@link  AuthPinView#onInvalidFingerprintDetected(String)  onInvalidFingerprintDetected(String)}
     * method is called, without any error.
     */
    @Test
    public void testInvalidFingerprintDetected() {
        doAnswer(answerVoid(Callback::onInvalidFingerprintDetected))
                .when(authInteractor).listenFingerPrintScanner(any(Callback.class));

        when(deviceHelper.isFingperprintAuthAvailable()).thenReturn(true);
        presenter.startListening();
        verify(view).onInvalidFingerprintDetected(ArgumentMatchers.any());
    }

    /**
     * Can not read the fingerprint.
     *
     * @test.expected The detected fingerprint is not valid,{@link  AuthPinView#onInvalidFingerprintDetected(String)  onInvalidFingerprintDetected(String)}
     * method is called, without any error.
     */
    @Test
    public void testCantDetectFingerprint() {
        final String error = "Can not read fingerprint";
        doAnswer(answerVoid((Callback callback) -> callback.onErrorOccurred(error)))
                .when(authInteractor).listenFingerPrintScanner(any(Callback.class));

        when(deviceHelper.isFingperprintAuthAvailable()).thenReturn(true);
        presenter.startListening();
        verify(view).onInvalidFingerprintDetected(error);
    }
}
