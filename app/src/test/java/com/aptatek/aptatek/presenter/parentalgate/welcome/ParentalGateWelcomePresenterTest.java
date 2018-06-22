package com.aptatek.aptatek.presenter.parentalgate.welcome;

import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.parentalgate.ParentalGateInteractor;
import com.aptatek.aptatek.domain.model.AgeCheckModel;
import com.aptatek.aptatek.view.parentalgate.welcome.AgeVerificationResult;
import com.aptatek.aptatek.view.parentalgate.welcome.ParentalGateWelcomePresenter;
import com.aptatek.aptatek.view.parentalgate.welcome.ParentalGateWelcomeView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ParentalGateWelcomePresenterTest {

    private static final String TEST_STRING = "hello";

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    ParentalGateInteractor parentalGateInteractor;

    @Mock
    ParentalGateWelcomeView view;

    private ParentalGateWelcomePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn(TEST_STRING);
        when(parentalGateInteractor.formatBirthDate(ArgumentMatchers.anyLong())).thenReturn(Single.just(TEST_STRING));

        presenter = new ParentalGateWelcomePresenter(resourceInteractor, parentalGateInteractor);
        presenter.attachView(view);
    }

    @Test
    public void testInitUi() throws Exception {
        presenter.initUi();

        verify(view).setShowKeypad(false);
        verify(view).setShowBirthDateField(false);
        verify(view).setShowAgeField(false);
        verify(view).setShowButton(true);
        verify(view).setKeypadActionText(TEST_STRING);
        verify(view).setButtonText(TEST_STRING);
        verify(view).setAgeText("");
        verify(view).setBirthDateText("");
    }

    @Test
    public void testOnButtonPress() throws Exception {
        presenter.initUi();

        presenter.onButtonPress();
        verify(view).showDatePicker();
    }

    @Test
    public void testOnButtonPressAfterBirthDate() throws Exception {
        presenter.initUi();

        presenter.onBirthDateSet(System.currentTimeMillis());
        verify(view).setShowBirthDateField(true);
        verify(view).setBirthDateText(TEST_STRING);
        presenter.onButtonPress();
        verify(view).setShowButton(false);
        verify(view).setShowKeypad(true);
        verify(view).setShowAgeField(true);
    }

    @Test
    public void testOnAgeEntered() throws Exception {
        presenter.initUi();

        when(parentalGateInteractor.verify(ArgumentMatchers.any(AgeCheckModel.class))).thenReturn(Single.just(false));

        presenter.onBirthDateSet(System.currentTimeMillis());
        presenter.verifyAge("11");
        verify(view).showResult(ArgumentMatchers.any(AgeVerificationResult.class));
    }

}
