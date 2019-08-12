package com.aptatek.pkulab.presenter.parentalgate.welcome;

import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.parentalgate.ParentalGateInteractor;
import com.aptatek.pkulab.domain.manager.analytic.IAnalyticsManager;
import com.aptatek.pkulab.domain.model.AgeCheckModel;
import com.aptatek.pkulab.domain.model.AgeCheckResult;
import com.aptatek.pkulab.view.parentalgate.welcome.AgeVerificationResult;
import com.aptatek.pkulab.view.parentalgate.welcome.ParentalGateWelcomePresenter;
import com.aptatek.pkulab.view.parentalgate.welcome.ParentalGateWelcomeView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @test.layer View / ParentalGate / Welcome
 * @test.feature ParentalGate
 * @test.type Unit tests
 */
public class ParentalGateWelcomePresenterTest {

    private static final String TEST_STRING = "hello";

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    ParentalGateInteractor parentalGateInteractor;

    @Mock
    ParentalGateWelcomeView view;

    @Mock
    IAnalyticsManager analyticsManager;

    private ParentalGateWelcomePresenter presenter;

    /**
     * Setting up the required presenter and defining mocked component's behaviour
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn(TEST_STRING);
        when(parentalGateInteractor.formatBirthDate(ArgumentMatchers.anyLong())).thenReturn(Single.just(TEST_STRING));

        presenter = new ParentalGateWelcomePresenter(resourceInteractor, parentalGateInteractor, analyticsManager);
        presenter.attachView(view);
    }

    /**
     * Initializing the view.
     *
     * @test.expected {@link  ParentalGateWelcomeView#setShowKeypad(boolean)  setShowKeypad(boolean)},
     * {@link  ParentalGateWelcomeView#setShowBirthDateField(boolean)  setShowBirthDateField(boolean)},
     * {@link  ParentalGateWelcomeView#setShowAgeField(boolean)  setShowAgeField(boolean)},
     * {@link  ParentalGateWelcomeView#setShowButton(boolean)  setShowButton(boolean)},
     * {@link  ParentalGateWelcomeView#setKeypadActionText(String)  setKeypadActionText(String)},
     * {@link  ParentalGateWelcomeView#setButtonText(String)  setButtonText(String)},
     * {@link  ParentalGateWelcomeView#setAgeText(String)  setAgeText(String)},
     * {@link  ParentalGateWelcomeView#setBirthDateText(String)  setBirthDateText(String)}
     * methods are called, without any error.
     */
    @Test
    public void testInitUi() {
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

    /**
     * Showing DatePicker flow.
     *
     * @test.expected {@link  ParentalGateWelcomeView#showDatePicker()  showDatePicker()}
     * method is called, without any error.
     */
    @Test
    public void testOnButtonPress() {
        presenter.initUi();

        presenter.onButtonPress();
        verify(view).showDatePicker();
    }

    /**
     * Selecting birthdate and typing age number.
     *
     * @test.expected {@link  ParentalGateWelcomeView#setShowBirthDateField(boolean)  setShowBirthDateField(boolean)},
     * {@link  ParentalGateWelcomeView#setBirthDateText(String)  setBirthDateText(String)},
     * {@link  ParentalGateWelcomeView#setShowButton(boolean)  setShowButton(boolean)},
     * {@link  ParentalGateWelcomeView#setShowKeypad(boolean)  setShowKeypad(boolean)},
     * {@link  ParentalGateWelcomeView#setShowAgeField(boolean) setShowAgeField(boolean)}
     * methods are called, without any error.
     */
    @Test
    public void testOnButtonPressAfterBirthDate() {
        presenter.initUi();

        presenter.onBirthDateSet(System.currentTimeMillis());
        verify(view).setShowBirthDateField(true);
        verify(view).setBirthDateText(TEST_STRING);
        presenter.onButtonPress();
        verify(view).setShowButton(false);
        verify(view).setShowKeypad(true);
        verify(view).setShowAgeField(true);
    }

    /**
     * Testing age verification flow.
     *
     * @test.expected {@link  ParentalGateWelcomeView#showResult(AgeVerificationResult)  showResult(AgeVerificationResult)}
     * method is called, without any error.
     */
    @Test
    public void testOnAgeEntered() {
        presenter.initUi();

        when(parentalGateInteractor.verify(ArgumentMatchers.any(AgeCheckModel.class))).thenReturn(Single.just(AgeCheckResult.NOT_OLD_ENOUGH));

        presenter.onBirthDateSet(System.currentTimeMillis());
        presenter.verifyAge("11");
        verify(view).showResult(ArgumentMatchers.any(AgeVerificationResult.class));
    }

}
