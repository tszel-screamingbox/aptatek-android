package com.aptatek.aptatek.view.pin.set.confirm;

import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.device.DeviceHelper;
import com.aptatek.aptatek.domain.interactor.auth.AuthInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new ConfirmPinPresenter(authInteractor, deviceHelper);
        presenter.attachView(view);
    }

    @Test
    public void testValidPin() {
        presenter.verifyPin(validPin, validPin);
        verify(view).onValidPinTyped();
    }

    @Test
    public void testInvalidPin() {
        presenter.verifyPin(validPin, invalidPin);
        verify(view).onInvalidPinTyped();
    }

    @Test
    public void testEnableFingerprint() {
        when(deviceHelper.hasEnrolledFingerprints()).thenReturn(true);
        when(deviceHelper.hasFingerprintHadrware()).thenReturn(true);
        presenter.navigateForward();
        verify(view).onFingerprintActivityShouldLoad();
    }

    @Test
    public void testDisableFingerprint() {
        when(deviceHelper.hasEnrolledFingerprints()).thenReturn(false);
        when(deviceHelper.hasFingerprintHadrware()).thenReturn(false);
        presenter.navigateForward();
        verify(view).onMainActivityShouldLoad();
    }

    @Test
    public void testHasScannerButNoFingerprint() {
        when(deviceHelper.hasEnrolledFingerprints()).thenReturn(false);
        when(deviceHelper.hasFingerprintHadrware()).thenReturn(true);
        presenter.navigateForward();
        verify(view).onMainActivityShouldLoad();
    }
}