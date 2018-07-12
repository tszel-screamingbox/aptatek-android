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

        dataSource.getNormalCeilValue();
        verify(preferenceManager).getPkuRangeNormalCeil();

        dataSource.getNormalFloorValue();
        verify(preferenceManager).getPkuRangeNormalFloor();

        dataSource.setDisplayUnit(PkuLevelUnits.MICRO_MOL);
        verify(preferenceManager).setPkuRangeUnit(PkuLevelUnits.MICRO_MOL);

        dataSource.setNormalCeilValue(300f);
        verify(preferenceManager).setPkuRangeNormalCeil(300f);

        dataSource.setNormalFloorValue(300f);
        verify(preferenceManager).setPkuRangeNormalFloor(300f);
    }

    @Test
    public void testDataSourceReturnsDefaultsWhenPrefNotSet() throws Exception {
        doReturn(-1f).when(preferenceManager).getPkuRangeNormalFloor();
        doReturn(-1f).when(preferenceManager).getPkuRangeNormalCeil();
        doReturn(null).when(preferenceManager).getPkuRangeUnit();

        assertTrue(dataSource.getNormalFloorValue() == Constants.DEFAULT_PKU_NORMAL_FLOOR);
        assertTrue(dataSource.getNormalCeilValue() == Constants.DEFAULT_PKU_NORMAL_CEIL);
        assertTrue(dataSource.getDisplayUnit() == Constants.DEFAULT_PKU_LEVEL);
        assertTrue(dataSource.getHighCeilValue() == Constants.DEFAULT_PKU_HIGH_CEIL);
    }

}
