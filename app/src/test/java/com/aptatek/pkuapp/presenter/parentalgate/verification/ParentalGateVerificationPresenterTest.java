package com.aptatek.pkuapp.presenter.parentalgate.verification;

import com.aptatek.pkuapp.device.PreferenceManager;
import com.aptatek.pkuapp.view.parentalgate.verification.ParentalGateVerificationPresenter;
import com.aptatek.pkuapp.view.parentalgate.verification.ParentalGateVerificationView;
import com.aptatek.pkuapp.view.parentalgate.welcome.AgeVerificationResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * @test.layer View / ParentalGate
 * @test.feature ParentalGate
 * @test.type Unit tests
 */
public class ParentalGateVerificationPresenterTest {

    private static final String TEST_STRING = "hello";

    @Mock
    PreferenceManager preferenceManager;

    @Mock
    ParentalGateVerificationView view;

    private ParentalGateVerificationPresenter presenter;

    /**
     * Setting up the required presenter
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new ParentalGateVerificationPresenter(preferenceManager);
        presenter.attachView(view);
    }

    /**
     * Successfully passed age verification.
     *
     * @test.expected {@link  ParentalGateVerificationView#showImage(int)  showImage()},
     * {@link  ParentalGateVerificationView#showTitle(String)  showTitle(String)},
     * {@link  ParentalGateVerificationView#showMessage(String)  showMessage(String)},
     * {@link  ParentalGateVerificationView#showButton(boolean)  showButton(boolean)}
     * methods are called, without any error.
     */
    @Test
    public void testInitUiWithSuccess() {
        presenter.initUi(AgeVerificationResult.builder()
                .setShowButton(false)
                .setMessage(TEST_STRING)
                .setTitle(TEST_STRING)
                .setIconRes(ArgumentMatchers.anyInt())
                .build());

        verify(view).showImage(ArgumentMatchers.anyInt());
        verify(view).showTitle(TEST_STRING);
        verify(view).showMessage(TEST_STRING);
        verify(view).showButton(false);
        verify(preferenceManager).setParentalPassed(true);
    }

    /**
     * Failed to pass on age verification.
     *
     * @test.expected {@link  ParentalGateVerificationView#showImage(int)  showImage()},
     * {@link  ParentalGateVerificationView#showTitle(String)  showTitle(String)},
     * {@link  ParentalGateVerificationView#showMessage(String)  showMessage(String)},
     * {@link  ParentalGateVerificationView#showButton(boolean)  showButton(boolean)}
     * methods are called, without any error.
     */
    @Test
    public void testInitUiWithError() {
        presenter.initUi(AgeVerificationResult.builder()
                .setShowButton(true)
                .setMessage(TEST_STRING)
                .setTitle(TEST_STRING)
                .setIconRes(ArgumentMatchers.anyInt())
                .build());

        verify(view).showImage(ArgumentMatchers.anyInt());
        verify(view).showTitle(TEST_STRING);
        verify(view).showMessage(TEST_STRING);
        verify(view).showButton(true);
    }

}
