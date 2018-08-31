package com.aptatek.pkuapp.presenter.test.tutorial.insertcasette;

import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;
import com.aptatek.pkuapp.view.test.tutorial.insertcasette.InsertCasettePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the InsertCasettePresenter class.
 *
 * @test.layer presentation
 * @test.feature InsertCasette
 * @test.type unit
 */
public class InsertCasettePresenterTest {

    private  static final String TEST_TEXT = "Hello";

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    TestFragmentBaseView view;

    private InsertCasettePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn(TEST_TEXT);

        presenter = new InsertCasettePresenter(resourceInteractor);
        presenter.attachView(view);
    }

    /**
     * Tests the proper behavior: the initUi() method should trigger changes on UI: the initial state should be rendered
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testInitialUi() throws Exception {
        presenter.initUi();

        verify(view).setCancelBigVisible(false);
        verify(view).setCircleCancelVisible(true);
        verify(view).setNavigationButtonVisible(true);
        verify(view).setNavigationButtonText(TEST_TEXT);
        verify(view).setTitle(TEST_TEXT);
        verify(view).setMessage(TEST_TEXT);
    }

}
