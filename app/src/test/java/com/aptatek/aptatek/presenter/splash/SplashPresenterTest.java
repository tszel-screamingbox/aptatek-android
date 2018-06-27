package com.aptatek.aptatek.presenter.splash;

import com.aptatek.aptatek.data.AptatekDatabase;
import com.aptatek.aptatek.data.dao.ReminderDayDao;
import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.manager.keystore.KeyStoreManager;
import com.aptatek.aptatek.view.splash.SplashActivityPresenter;
import com.aptatek.aptatek.view.splash.SplashActivityView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import io.reactivex.Single;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SplashPresenterTest {

    @Mock
    KeyStoreManager keyStoreManager;

    @Mock
    PreferenceManager preferenceManager;

    @Mock
    AptatekDatabase aptatekDatabase;

    @Mock
    ReminderDayDao reminderDao;

    @Mock
    SplashActivityView view;

    private SplashActivityPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(reminderDao.getReminderDays()).thenReturn(Single.just(Collections.emptyList()));
        when(aptatekDatabase.getReminderDayDao()).thenReturn(reminderDao);

        presenter = new SplashActivityPresenter(keyStoreManager, aptatekDatabase, preferenceManager);
        presenter.attachView(view);
    }

    @Test
    public void testNavigationToParentalGate() throws Exception {
        when(preferenceManager.isParentalPassed()).thenReturn(false);

        presenter.switchToNextActivity();
        verify(view).onParentalGateShouldLoad();
    }

    @Test
    public void testNavigationToPinSetup() throws Exception {
        when(preferenceManager.isParentalPassed()).thenReturn(true);
        when(keyStoreManager.aliasExists()).thenReturn(false);

        presenter.switchToNextActivity();
        verify(view).onSetPinActivityShouldLoad();
    }

    @Test
    public void testNavigationToPinCheck() throws Exception {
        when(preferenceManager.isParentalPassed()).thenReturn(true);
        when(keyStoreManager.aliasExists()).thenReturn(true);

        presenter.switchToNextActivity();
        verify(view).onRequestPinActivityShouldLoad();
    }
}
