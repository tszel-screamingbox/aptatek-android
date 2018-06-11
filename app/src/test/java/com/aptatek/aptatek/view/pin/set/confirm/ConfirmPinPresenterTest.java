package com.aptatek.aptatek.view.pin.set.confirm;

import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.domain.interactor.auth.AuthInteractor;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


public class ConfirmPinPresenterTest {

    @Mock
    private
    AuthInteractor authInteractor;

    @Mock
    private
    ConfirmPinView view;

    private ConfirmPinPresenter presenter;
    private PinCode validPin = new PinCode("valid".getBytes());
    private PinCode invalidPin = new PinCode("invalid".getBytes());


    @BeforeClass
    public static void before() {
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new ConfirmPinPresenter(authInteractor);
        presenter.attachView(view);
    }

    @Test
    public void testValidPin() {
        presenter.verifyPin(validPin, validPin);
        verify(view).onValidPinTyped();
        verify(view, timeout(5000)).onMainActivityShouldLoad();
    }

    @Test
    public void testInvalidPin() {
        presenter.verifyPin(validPin, invalidPin);
        verify(view).onInvalidPinTyped();
        verify(view, timeout(5000)).onInvalidPinTyped();
    }
}