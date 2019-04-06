package com.aptatek.pkulab.device.formatter;

import com.aptatek.pkulab.domain.interactor.ResourceInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BirthdateFormatterImplTest {

    private BirthDateFormatterImpl formatter;

    @Mock
    ResourceInteractor resourceInteractor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn("yyyy/MM/dd");
        formatter = new BirthDateFormatterImpl(resourceInteractor);
    }

    @Test
    public void testFormat() throws Exception {
        final String s = formatter.formatBirthDate(1553028965000L);
        assert s.equals("2019/03/19");
    }

}
