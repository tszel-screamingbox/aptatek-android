package com.aptatek.aptatek.view.pin.request.add;

import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.domain.interactor.auth.AuthInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class RequestPinPresenterTest {

    @Mock
    private
    AuthInteractor authInteractor;

    @Mock
    private
    RequestPinView view;

    private RequestPinPresenter presenter;
    private PinCode validPin = new PinCode("valid".getBytes());
    private PinCode invalidPin = new PinCode("invalid".getBytes());

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(authInteractor.isValidPinCode(validPin)).thenReturn(true);
        Mockito.when(authInteractor.isValidPinCode(invalidPin)).thenReturn(false);

        presenter = new RequestPinPresenter(authInteractor);
        presenter.attachView(view);
    }

    @Test
    public void testValidPin() {
        presenter.verifyPinCode(validPin);
        Mockito.verify(view).onMainActivityShouldLoad();
    }

    @Test
    public void testInvalidPin() {
        presenter.verifyPinCode(invalidPin);
        Mockito.verify(view).onInvalidPinTyped();
    }
}