package com.aptatek.aptatek.data.datasource;

import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeDataSource;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.util.Constants;

public class PkuRangeDataSourceImpl implements PkuRangeDataSource {

    private final PreferenceManager preferenceManager;

    public PkuRangeDataSourceImpl(final PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
    }

    @Override
    public void setNormalFloorValue(final float value) {
        preferenceManager.setPkuRangeNormalFloor(value);
    }

    @Override
    public void setNormalCeilValue(final float value) {
        preferenceManager.setPkuRangeNormalCeil(value);
    }

    @Override
    public void setDisplayUnit(final PkuLevelUnits unit) {
        preferenceManager.setPkuRangeUnit(unit);
    }

    @Override
    public float getNormalFloorValue() {
        final float pkuRangeNormalFloor = preferenceManager.getPkuRangeNormalFloor();
        return pkuRangeNormalFloor == -1 ? Constants.DEFAULT_PKU_NORMAL_FLOOR : pkuRangeNormalFloor;
    }

    @Override
    public float getNormalCeilValue() {
        final float pkuRangeNormalCeil = preferenceManager.getPkuRangeNormalCeil();
        return pkuRangeNormalCeil == -1 ? Constants.DEFAULT_PKU_NORMAL_CEIL : pkuRangeNormalCeil;
    }

    @Override
    public float getHighCeilValue() {
        return Constants.DEFAULT_PKU_HIGH_CEIL;
    }

    @Override
    public PkuLevelUnits getDisplayUnit() {
        final PkuLevelUnits pkuRangeUnit = preferenceManager.getPkuRangeUnit();
        return pkuRangeUnit == null ? Constants.DEFAULT_PKU_LEVEL : pkuRangeUnit;
    }
}
