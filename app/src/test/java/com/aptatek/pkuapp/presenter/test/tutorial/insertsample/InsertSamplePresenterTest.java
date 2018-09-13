package com.aptatek.pkuapp.presenter.test.tutorial.insertsample;

import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.wetting.WettingInteractor;
import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;
import com.aptatek.pkuapp.view.test.tutorial.insertsample.InsertSamplePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the InsertSamplePresenter class
 *
 * @test.layer presentation
 * @test.feature InsertSample
 * @test.type unit
 */
public class InsertSamplePresenterTest {

    private  static final String TEST_TEXT = "Hello";

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    WettingInteractor wettingInteractor;

    @Mock
    IncubationInteractor incubationInteractor;

    @Mock
    TestFragmentBaseView view;

    private InsertSamplePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn(TEST_TEXT);
        when(incubationInteractor.getIncubationStatus()).thenReturn(Single.just(IncubationStatus.NOT_STARTED));

        presenter = new InsertSamplePresenter(resourceInteractor, wettingInteractor, incubationInteractor);
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
