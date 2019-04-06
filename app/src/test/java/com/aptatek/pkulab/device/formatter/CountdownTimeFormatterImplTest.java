package com.aptatek.pkulab.device.formatter;

import com.aptatek.pkulab.domain.interactor.ResourceInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CountdownTimeFormatterImplTest {

    private CountdownTimeFormatterImpl formatter;

    @Mock
    ResourceInteractor resourceInteractor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn("mm:ss");
        formatter = new CountdownTimeFormatterImpl(resourceInteractor);
    }

    @Test
    public void testFormat() throws Exception {
        final String formatted = formatter.getFormattedRemaining(((11 * 60) + 32) * 1000L);
        assert formatted.equals("11:32");
    }
}
