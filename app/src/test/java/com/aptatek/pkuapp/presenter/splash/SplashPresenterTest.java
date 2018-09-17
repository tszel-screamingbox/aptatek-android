package com.aptatek.pkuapp.presenter.splash;

import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.pkuapp.domain.manager.keystore.KeyStoreManager;
import com.aptatek.pkuapp.view.splash.SplashActivityPresenter;
import com.aptatek.pkuapp.view.splash.SplashActivityView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;

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
    SplashActivityView view;

    private SplashActivityPresenter presenter;

    /**
     * Setting up the required presenter and defining mocked component's behaviour
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(reminderInteractor.initializeDays()).thenReturn(Completable.complete());

        presenter = new SplashActivityPresenter(keyStoreManager, reminderInteractor, preferenceManager);
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
        presenter.switchToNextActivity();
        verify(view).onParentalGateShouldLoad();
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
