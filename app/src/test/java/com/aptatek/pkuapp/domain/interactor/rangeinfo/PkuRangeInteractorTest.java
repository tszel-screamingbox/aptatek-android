package com.aptatek.pkuapp.domain.interactor.rangeinfo;

import com.aptatek.pkuapp.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.pkuapp.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.domain.model.PkuLevelUnits;
import com.aptatek.pkuapp.domain.model.PkuRangeInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the PkuRangeInteractor class
 *
 * @test.layer domain
 * @test.feature RangeInfo, RangeSettings, Charts
 * @test.type unit
 */
public class PkuRangeInteractorTest {

    private PkuRangeInteractor interactor;

    @Mock
    PkuRangeDataSource dataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interactor = new PkuRangeInteractor(dataSource);
    }

    /**
     * Tests the proper behavior: this class should rely on the PkuRangeDataSource so we expect to call its methods to get the desired values.
     *
     * @test.input
     * @test.expected
     */
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

    /**
     * Tests the proper behavior: the returned stream of getInfo() method should signal error when the DataSource throws exception.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testGetInfoSignalsErrorOnDataSourceException() throws Exception {
        doThrow(new RuntimeException()).when(dataSource).getDisplayUnit();

        final TestObserver<PkuRangeInfo> test = interactor.getInfo().test();
        test.assertError(error -> error instanceof RuntimeException);
    }

    /**
     * Tests the proper behavior: the getInfo() method should complete without error and emit a PkuRangeInfo object with the proper values.
     *
     * @test.input
     * @test.expected
     */
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

        test.assertValueAt(0, value -> value.getNormalCeilValue() == PkuLevelConverter.convertTo(PkuLevel.create(normalCeilValue, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue());
    }

    /**
     * Tests the proper behavior: the saveDisplayUnit(PkuLevelUnits) method should call the the DataSource to store the selected display unit.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testSaveDisplayUnitCallsDataSource() throws Exception {
        final PkuLevelUnits unit = PkuLevelUnits.MICRO_MOL;
        final TestObserver<Void> test = interactor.saveDisplayUnit(unit).test();
        test.assertComplete();
        test.assertNoErrors();

        verify(dataSource).setDisplayUnit(unit);
    }

     /**
     * Tests the proper behavior: the saveNormalRange(float, float) method should call the the DataSource to store the range settings then complete without error.
     *
     * @test.input normalFloor = 3.0 mg, normalCeil = 17.2 mg
     * @test.expected The dataSource.setNormalFloorValueMMol and dataSource.setNormalCeilValueMMol are called with proper values
     */
    @Test
    public void testSaveNormalRangeCallsDataSource() throws Exception {
        final PkuLevel floor = PkuLevel.create(3f, PkuLevelUnits.MILLI_GRAM);
        final PkuLevel ceil = PkuLevel.create(17.2f, PkuLevelUnits.MILLI_GRAM);
        final TestObserver<Void> test = interactor.saveNormalRange(floor, ceil).test();
        test.assertComplete();
        test.assertNoErrors();

        verify(dataSource).setNormalFloorValueMMol(PkuLevelConverter.convertTo(floor, PkuLevelUnits.MICRO_MOL).getValue());
        verify(dataSource).setNormalCeilValueMMol(PkuLevelConverter.convertTo(ceil, PkuLevelUnits.MICRO_MOL).getValue());
    }

    /**
     * Tests the proper behavior: the saveNormalRange(float, float) method should signal an error when invalid values are about to be saved.
     *
     * @test.input normalFloor = -1 mg, normalCeil = 200 mg
     * @test.expected exception is signaled on the stream
     */
    @Test
    public void testSaveNormalRangeError() throws Exception {
        final PkuLevel floor = PkuLevel.create(-1f, PkuLevelUnits.MILLI_GRAM);
        final PkuLevel ceil = PkuLevel.create(200.f, PkuLevelUnits.MILLI_GRAM);
        final TestObserver<Void> test = interactor.saveNormalRange(floor, ceil).test();
        test.assertError(error -> error instanceof IllegalArgumentException);
    }
}
