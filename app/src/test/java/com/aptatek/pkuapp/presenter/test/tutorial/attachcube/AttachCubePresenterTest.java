package com.aptatek.pkuapp.presenter.test.tutorial.attachcube;

import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.test.base.TestFragmentBaseView;
import com.aptatek.pkuapp.view.test.tutorial.attachcube.AttachCubePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AttachCubePresenterTest {

    private  static final String TEST_TEXT = "Hello";

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    TestFragmentBaseView view;

    private AttachCubePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn(TEST_TEXT);

        presenter = new AttachCubePresenter(resourceInteractor);
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
