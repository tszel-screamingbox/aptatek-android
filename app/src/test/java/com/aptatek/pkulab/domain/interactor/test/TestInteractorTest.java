package com.aptatek.pkulab.domain.interactor.test;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.view.test.TestScreens;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doReturn;

public class TestInteractorTest {

    @Mock
    PreferenceManager preferenceManager;

    private TestInteractor interactor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        interactor = new TestInteractor(preferenceManager);
    }

    @Test
    public void testDefaults() {
        doReturn(TestScreens.TURN_READER_ON).when(preferenceManager).getTestStatus();
        interactor.getLastScreen().test()
                .assertValue(TestScreens.TURN_READER_ON);
    }
}
