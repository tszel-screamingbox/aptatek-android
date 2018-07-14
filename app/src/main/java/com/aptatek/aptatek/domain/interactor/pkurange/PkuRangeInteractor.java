package com.aptatek.aptatek.domain.interactor.pkurange;

import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class PkuRangeInteractor {

    // TODO get the exact ratio
    private static final float MOL_TO_GRAM_CONVERSION_RATIO = 60f;

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

    public Single<PkuRangeInfo> getInfo(final PkuLevelUnits units) {
        return Single.fromCallable(() -> getInfoInUnit(units));
    }

    public Completable saveNormalRangeMMol(final float floorValue, final float ceilValue) {
        return Completable.fromAction(() -> {
            dataSource.setNormalFloorValue(floorValue);
            dataSource.setNormalCeilValue(ceilValue);
        });
    }

    private PkuRangeInfo getInfoInUnit(final PkuLevelUnits unit) {
        return PkuRangeInfo.builder()
                .setPkuLevelUnit(unit)
                .setHighCeilValue(getValueInUnit(dataSource.getHighCeilValue(), PkuLevelUnits.MICRO_MOL, unit))
                .setNormalCeilValue(getValueInUnit(dataSource.getNormalCeilValue(), PkuLevelUnits.MICRO_MOL, unit))
                .setNormalFloorValue(getValueInUnit(dataSource.getNormalFloorValue(),  PkuLevelUnits.MICRO_MOL,unit))
                .build();
    }

    public float getValueInUnit(final float value,
                                 final PkuLevelUnits originalUnit,
                                 final PkuLevelUnits targetUnit) {
        if (originalUnit == targetUnit) {
            return value;
        }

        final float ratio;

        if (originalUnit == PkuLevelUnits.MICRO_MOL) {
            ratio = 1 / MOL_TO_GRAM_CONVERSION_RATIO;
        } else {
            ratio = MOL_TO_GRAM_CONVERSION_RATIO;
        }


        return value * ratio;
    }



}
