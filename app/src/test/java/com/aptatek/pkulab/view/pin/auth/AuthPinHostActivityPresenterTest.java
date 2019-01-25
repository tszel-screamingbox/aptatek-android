package com.aptatek.pkulab.view.pin.auth;

import com.aptatek.pkulab.device.DeviceHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @test.layer View / Pin
 * @test.feature Authentication request
 * @test.type Unit tests
 */
public class AuthPinHostActivityPresenterTest {

    @Mock
    private DeviceHelper deviceHelper;

    @Mock
    private AuthPinHostActivityView view;

    private AuthPinHostActivityPresenter presenter;

    /**
     * Setting up the required presenter and defining mocked component's behaviour
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new AuthPinHostActivityPresenter(deviceHelper);
        presenter.attachView(view);
    }

    /**
     * Fingerprint authentication is enabled, shows it on the screen.
     *
     * @test.expected {@link  AuthPinHostActivityView#onFingerpintAuthShouldLoad()  onFingerpintAuthShouldLoad()}
     * method is called, without any error.
     */
    @Test
    public void testWhenFingerprintEnable() {
        when(deviceHelper.isFingperprintAuthAvailable()).thenReturn(true);
        presenter.initView();
        verify(view).onFingerpintAuthShouldLoad();
    }

    /**
     * Fingerprint authentication is disable, shows it on the screen.
     *
     * @test.expected {@link  AuthPinHostActivityView#onAuthPinFragmentShouldLoad()  onAuthPinFragmentShouldLoad()}
     * method is called, without any error.
     */
    @Test
    public void testWhenFingerprintDisable() {
        when(deviceHelper.isFingperprintAuthAvailable()).thenReturn(false);
        presenter.initView();
        verify(view).onAuthPinFragmentShouldLoad();
    }
}
