package com.aptatek.aptatek.presenter.test.tutorial.insertsample;

import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.view.test.base.TestFragmentBaseView;
import com.aptatek.aptatek.view.test.tutorial.insertsample.InsertSamplePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InsertSamplePresenterTest {

    private  static final String TEST_TEXT = "Hello";

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    TestFragmentBaseView view;

    private InsertSamplePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn(TEST_TEXT);

        presenter = new InsertSamplePresenter(resourceInteractor);
        presenter.attachView(view);
    }

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
