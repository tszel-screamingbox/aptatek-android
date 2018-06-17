package com.aptatek.aptatek.presenter.test.incubation;

import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.aptatek.domain.model.Countdown;
import com.aptatek.aptatek.view.test.incubation.IncubationPresenter;
import com.aptatek.aptatek.view.test.incubation.IncubationView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IncubationPresenterTest {

    private static final String TEST_STRING = "test";

    @Mock
    IncubationInteractor incubationInteractor;

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    IncubationView view;

    private IncubationPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn(TEST_STRING);
        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt(), ArgumentMatchers.anyVararg())).thenReturn(TEST_STRING);
        when(incubationInteractor.getIncubationCountdown()).thenReturn(Flowable.just(Countdown.builder().setRemainingFormattedText("").setRemainingMillis(0L).build()));

        presenter = new IncubationPresenter(resourceInteractor, incubationInteractor);
        presenter.attachView(view);
    }

    @Test
    public void testInitUi() throws Exception {
        presenter.initUi();

        verify(view).setTitle(TEST_STRING);
        verify(view).setMessage(TEST_STRING);
        verify(view).setNavigationButtonVisible(true);
        verify(view).setNavigationButtonText(TEST_STRING);
        verify(view).setCircleCancelVisible(true);
        verify(view).setCancelBigVisible(false);
    }

    @Test
    public void testAttachViewCallsInteractor() throws Exception {
        verify(incubationInteractor).getIncubationCountdown();
    }

}
