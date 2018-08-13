package com.aptatek.pkuapp.view.pin.auth.add;

import android.support.annotation.NonNull;

import com.aptatek.pkuapp.data.PinCode;
import com.aptatek.pkuapp.device.DeviceHelper;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.auth.AuthInteractor;
import com.aptatek.pkuapp.domain.interactor.auth.Callback;
import com.aptatek.pkuapp.domain.manager.keystore.KeyStoreError;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(authInteractor.checkPinCode(validPin)).thenReturn(Completable.complete());
        when(authInteractor.checkPinCode(invalidPin)).thenReturn(Completable.error(new KeyStoreError(new Throwable())));

        presenter = new AuthPinPresenter(authInteractor, deviceHelper, resourceInteractor);
        presenter.attachView(view);
    }

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

    @Test
    public void testWhenFingerprintEnable() {
        when(deviceHelper.isFingperprintAuthAvailable()).thenReturn(true);
        presenter.initView();
        verify(view).onFingerprintAvailable();
    }

    @Test
    public void testWhenFingerprintDisable() {
        when(deviceHelper.isFingperprintAuthAvailable()).thenReturn(false);
        presenter.initView();
        verify(view).onFingerprintDisabled();
    }

    @Test
    public void testValidPin() {
        presenter.verifyPinCode(validPin);
        verify(view).onValidPinTyped();
    }

    @Test
    public void testInvalidPin() {
        presenter.verifyPinCode(invalidPin);
        verify(view).onInvalidPinTyped();
    }

    @Test
    public void testValidFingerprintDetected() {
        doAnswer(answerVoid(Callback::onSucceeded))
                .when(authInteractor).listenFingerPrintScanner(any(Callback.class));

        when(deviceHelper.isFingperprintAuthAvailable()).thenReturn(true);
        presenter.startListening();
        verify(view).onValidFingerprintDetected();
    }

    @Test
    public void testInvalidFingerprintDetected() {
        doAnswer(answerVoid(Callback::onInvalidFingerprintDetected))
                .when(authInteractor).listenFingerPrintScanner(any(Callback.class));

        when(deviceHelper.isFingperprintAuthAvailable()).thenReturn(true);
        presenter.startListening();
        verify(view).onInvalidFingerprintDetected(ArgumentMatchers.any());
    }

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
