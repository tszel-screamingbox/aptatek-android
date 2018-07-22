package com.aptatek.aptatek.data.datasource;

import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class PkuRangeDataSourceTest {

    private PkuRangeDataSource dataSource;

    @Mock
    PreferenceManager preferenceManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        dataSource = new PkuRangeDataSourceImpl(preferenceManager);
    }

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
