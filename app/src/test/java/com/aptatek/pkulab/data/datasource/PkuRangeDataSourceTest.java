package com.aptatek.pkulab.data.datasource;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Tests for the PkuRangeDataSourceImpl class
 *
 * @test.layer data
 * @test.feature RangeInfo, RangeSettings, Charts
 * @test.type unit
 */
public class PkuRangeDataSourceTest {

    private PkuRangeDataSource dataSource;

    @Mock
    PreferenceManager preferenceManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        dataSource = new PkuRangeDataSourceImpl(preferenceManager);
    }

    /**
     * Tests the proper behavior: the data source should rely on the shared preferences so it needs to call the corresponding methods of our PreferenceManager.
     * @test.input
     * @test.expected
     */
    @Test
    public void testDataSourceCallsPrefManager() throws Exception {
        doReturn(1f).when(preferenceManager).getPkuRangeNormalCeil();
        doReturn(1f).when(preferenceManager).getPkuRangeNormalFloor();
        doReturn(PkuLevelUnits.MICRO_MOL).when(preferenceManager).getPkuRangeUnit();

        dataSource.getDisplayUnit();
        verify(preferenceManager).getPkuRangeUnit();

        dataSource.getNormalCeilValueMMol();
        verify(preferenceManager).getPkuRangeNormalCeil();

        dataSource.getNormalFloorValueMMol();
        verify(preferenceManager).getPkuRangeNormalFloor();

        dataSource.setDisplayUnit(PkuLevelUnits.MICRO_MOL);
        verify(preferenceManager).setPkuRangeUnit(PkuLevelUnits.MICRO_MOL);

        dataSource.setNormalCeilValueMMol(300f);
        verify(preferenceManager).setPkuRangeNormalCeil(300f);

        dataSource.setNormalFloorValueMMol(300f);
        verify(preferenceManager).setPkuRangeNormalFloor(300f);
    }

    /**
     * Test if the user has not set any custom range settings the dataSource should return default values
     * @test.input unit, normalFloor, normalCeil
     * @test.expected unit = uMol, normalFloor = 100, normalCeil = 350
     */
    @Test
    public void testDataSourceReturnsDefaultsWhenPrefNotSet() throws Exception {
        doReturn(-1f).when(preferenceManager).getPkuRangeNormalFloor();
        doReturn(-1f).when(preferenceManager).getPkuRangeNormalCeil();
        doReturn(null).when(preferenceManager).getPkuRangeUnit();

        assertTrue(dataSource.getNormalFloorValueMMol() == Constants.DEFAULT_PKU_NORMAL_FLOOR);
        assertTrue(dataSource.getNormalCeilValueMMol() == Constants.DEFAULT_PKU_NORMAL_CEIL);
        assertTrue(dataSource.getDisplayUnit() == Constants.DEFAULT_PKU_LEVEL_UNIT);
        assertTrue(dataSource.getHighCeilValueMMol() == Constants.DEFAULT_PKU_NORMAL_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE);
        assertTrue(dataSource.getNormalFloorAbsoluteMinMMol() == Constants.DEFAULT_PKU_LOWEST_VALUE);
        assertTrue(dataSource.getNormalCeilAbsoluteMaxMMol() == Constants.DEFAULT_PKU_HIGHEST_VALUE);
    }

}
