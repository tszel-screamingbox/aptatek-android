package com.aptatek.pkulab.domain.interactor.pkurange;

import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.util.Constants;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class PkuRangeInteractor {

    private final PkuRangeDataSource dataSource;

    @Inject
    public PkuRangeInteractor(final PkuRangeDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Single<PkuRangeInfo> getInfo() {
        return Single.fromCallable(() -> {
            final PkuLevelUnits displayUnit = dataSource.getDisplayUnit();
            return getInfoInUnit(displayUnit);
        });
    }

    public Completable saveNormalRange(final PkuLevel floor, final PkuLevel ceil) {
        return Completable.fromAction(() -> {
            final PkuLevel floorMMol = PkuLevelConverter.convertTo(floor, PkuLevelUnits.MICRO_MOL);
            final PkuLevel ceilMMol = PkuLevelConverter.convertTo(ceil, PkuLevelUnits.MICRO_MOL);

            if (floorMMol.getValue() < Constants.DEFAULT_PKU_LOWEST_VALUE) {
                throw new IllegalArgumentException("floor value cannot be smaller than " + Constants.DEFAULT_PKU_LOWEST_VALUE);
            }

            if (ceilMMol.getValue() > Constants.DEFAULT_PKU_HIGHEST_VALUE) {
                throw new IllegalArgumentException("ceil value cannot be greater than " + Constants.DEFAULT_PKU_HIGHEST_VALUE);
            }

            if (floorMMol.getValue() > ceilMMol.getValue()) {
                throw new IllegalArgumentException("floor must be larger than ceil");
            }

            dataSource.setNormalFloorValueMMol(floorMMol.getValue());
            dataSource.setNormalCeilValueMMol(ceilMMol.getValue());
        });
    }

    public Completable saveDisplayUnit(final PkuLevelUnits unit) {
        return Completable.fromAction(() -> dataSource.setDisplayUnit(unit));
    }

    public PkuRangeInfo getInfoInUnit(final PkuLevelUnits displayUnit) {
        return PkuRangeInfo.builder()
                .setPkuLevelUnit(displayUnit)
                .setHighCeilValue(getValueInUnit(dataSource.getHighCeilValueMMol(), PkuLevelUnits.MICRO_MOL, displayUnit))
                .setNormalCeilValue(getValueInUnit(dataSource.getNormalCeilValueMMol(), PkuLevelUnits.MICRO_MOL, displayUnit))
                .setNormalFloorValue(getValueInUnit(dataSource.getNormalFloorValueMMol(), PkuLevelUnits.MICRO_MOL, displayUnit))
                .setNormalAbsoluteMinValue(getValueInUnit(dataSource.getNormalFloorAbsoluteMinMMol(), PkuLevelUnits.MICRO_MOL, displayUnit))
                .setNormalAbsoluteMaxValue(getValueInUnit(dataSource.getNormalCeilAbsoluteMaxMMol(), PkuLevelUnits.MICRO_MOL, displayUnit))
                .setDefaultValue(dataSource.isDefaultValue())
                .build();
    }

    private float getValueInUnit(final float value, final PkuLevelUnits originalUnit, final PkuLevelUnits targetUnit) {
        return PkuLevelConverter.convertTo(PkuLevel.create(value, originalUnit), targetUnit).getValue();
    }
}
