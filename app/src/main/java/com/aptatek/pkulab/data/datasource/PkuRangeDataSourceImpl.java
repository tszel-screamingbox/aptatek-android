package com.aptatek.pkulab.data.datasource;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.util.Constants;

public class PkuRangeDataSourceImpl implements PkuRangeDataSource {

    private final PreferenceManager preferenceManager;

    public PkuRangeDataSourceImpl(final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void setNormalFloorValueMMol(final float value) {
        preferenceManager.setPkuRangeNormalFloor(value);
    }

    @Override
    public void setNormalCeilValueMMol(final float value) {
        preferenceManager.setPkuRangeNormalCeil(value);
    }

    @Override
    public void setDisplayUnit(final PkuLevelUnits unit) {
        preferenceManager.setPkuRangeUnit(unit);
    }

    @Override
    public float getNormalFloorValueMMol() {
        final float pkuRangeNormalFloor = preferenceManager.getPkuRangeNormalFloor();
        return pkuRangeNormalFloor == -1 ? Constants.DEFAULT_PKU_NORMAL_FLOOR : pkuRangeNormalFloor;
    }

    @Override
    public float getNormalCeilValueMMol() {
        final float pkuRangeNormalCeil = preferenceManager.getPkuRangeNormalCeil();
        return pkuRangeNormalCeil == -1 ? Constants.DEFAULT_PKU_NORMAL_CEIL : pkuRangeNormalCeil;
    }

    @Override
    public float getHighCeilValueMMol() {
        return getNormalCeilValueMMol() + Constants.DEFAULT_PKU_HIGH_RANGE;
    }

    @Override
    public float getNormalCeilAbsoluteMaxMMol() {
        return Constants.DEFAULT_PKU_HIGHEST_VALUE;
    }

    @Override
    public float getNormalFloorAbsoluteMinMMol() {
        return Constants.DEFAULT_PKU_LOWEST_VALUE;
    }

    @Override
    public PkuLevelUnits getDisplayUnit() {
        final PkuLevelUnits pkuRangeUnit = preferenceManager.getPkuRangeUnit();
        return pkuRangeUnit == null ? Constants.DEFAULT_PKU_LEVEL_UNIT : pkuRangeUnit;
    }
}
