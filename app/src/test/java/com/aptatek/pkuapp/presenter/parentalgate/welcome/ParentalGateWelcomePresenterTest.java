package com.aptatek.pkuapp.presenter.parentalgate.welcome;

import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.parentalgate.ParentalGateInteractor;
import com.aptatek.pkuapp.domain.model.AgeCheckModel;
import com.aptatek.pkuapp.domain.model.AgeCheckResult;
import com.aptatek.pkuapp.view.parentalgate.welcome.AgeVerificationResult;
import com.aptatek.pkuapp.view.parentalgate.welcome.ParentalGateWelcomePresenter;
import com.aptatek.pkuapp.view.parentalgate.welcome.ParentalGateWelcomeView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the ParentalGateWelcomePresenter class
 *
 * @test.layer presentation
 * @test.feature ParentalGate
 * @test.type unit
 */
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

    /**
     * Tests the proper behavior: the initUi() method should invoke the ParentalGateWelcomeView's proper methods with the default values to render the inital UI state.
     *
     * @test.input
     * @test.expected
     */
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

    /**
     * Tests the proper behavior: the onButtonPress() method should invoke the ParentalGateVerificationView.showDatePicker() to display the date picker.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testOnButtonPress() throws Exception {
        presenter.initUi();

        presenter.onButtonPress();
        verify(view).showDatePicker();
    }

    /**
     * Tests the proper behavior: the presenter.onButtonPress() should trigger UI changes when the birthDate is already set: Age field and keyboard should become visible.
     *
     * @test.input
     * @test.expected
     */
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

    /**
     * Tests the proper behavior: the verifyAge(String) should trigger the ParentalGateWelcomeView.showResult(AgeVerificationResult) to proceed to the verification result screen.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testOnAgeEntered() throws Exception {
        presenter.initUi();

        when(parentalGateInteractor.verify(ArgumentMatchers.any(AgeCheckModel.class))).thenReturn(Single.just(AgeCheckResult.NOT_OLD_ENOUGH));

        presenter.onBirthDateSet(System.currentTimeMillis());
        presenter.verifyAge("11");
        verify(view).showResult(ArgumentMatchers.any(AgeVerificationResult.class));
    }

}
