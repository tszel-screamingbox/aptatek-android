package com.aptatek.aptatek.domain.interactor.rangeinfo;

import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PkuRangeInteractorTest {

    private PkuRangeInteractor interactor;

    @Mock
    PkuRangeDataSource dataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interactor = new PkuRangeInteractor(dataSource);
    }

    @Test
    public void testGetInfoCallsDataSource() throws Exception {
        when(dataSource.getHighCeilValueMMol()).thenReturn(1f);
        when(dataSource.getNormalCeilValueMMol()).thenReturn(1f);
        when(dataSource.getNormalFloorValueMMol()).thenReturn(1f);
        when(dataSource.getDisplayUnit()).thenReturn(PkuLevelUnits.MICRO_MOL);

        final TestObserver<PkuRangeInfo> test = interactor.getInfo().test();

        verify(dataSource).getHighCeilValueMMol();
        verify(dataSource).getNormalCeilValueMMol();
        verify(dataSource).getNormalFloorValueMMol();
        verify(dataSource).getDisplayUnit();

        test.assertComplete();
        test.assertNoErrors();
        test.assertValueCount(1);
    }

    @Test
    public void testGetInfoSignalsErrorOnDataSourceException() throws Exception {
        doThrow(new RuntimeException()).when(dataSource).getDisplayUnit();

        final TestObserver<PkuRangeInfo> test = interactor.getInfo().test();
        test.assertError(error -> error instanceof RuntimeException);
    }

    @Test
    public void testGetInfoConvertsUnit() throws Exception {
        final float highCeilValue = 510f;
        final float normalCeilValue = 350f;
        final float normalFloorValue = 100f;
        when(dataSource.getDisplayUnit()).thenReturn(PkuLevelUnits.MILLI_GRAM);
        when(dataSource.getHighCeilValueMMol()).thenReturn(highCeilValue);
        when(dataSource.getNormalCeilValueMMol()).thenReturn(normalCeilValue);
        when(dataSource.getNormalFloorValueMMol()).thenReturn(normalFloorValue);

        final TestObserver<PkuRangeInfo> test = interactor.getInfo().test();
        test.await(1, TimeUnit.SECONDS);
        test.assertNoErrors();
        test.assertComplete();

        // TODO get exact conversion and validate it here...
        test.assertValueAt(0, value -> value.getHighCeilValue() != highCeilValue);
    }
}
