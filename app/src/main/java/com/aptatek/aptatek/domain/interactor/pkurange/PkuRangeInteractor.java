package com.aptatek.aptatek.domain.interactor.pkurange;

import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;

import javax.inject.Inject;

import io.reactivex.Single;

public class PkuRangeInteractor {

    // TODO get the exact ratio
    private static final int MOL_TO_GRAM_CONVERSION_RATIO = 60;

    private final PkuRangeDataSource dataSource;

    @Inject
    public PkuRangeInteractor(final PkuRangeDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Single<PkuRangeInfo> getInfo() {
        return Single.fromCallable(() -> {
            final PkuLevelUnits displayUnit = dataSource.getDisplayUnit();

            return PkuRangeInfo.builder()
                    .setPkuLevelUnit(displayUnit)
                    .setHighCeilValue(getValueInUnit(dataSource.getHighCeilValue(), displayUnit))
                    .setNormalCeilValue(getValueInUnit(dataSource.getNormalCeilValue(), displayUnit))
                    .setNormalFloorValue(getValueInUnit(dataSource.getNormalFloorValue(), displayUnit))
                    .build();
        });
    }

    private float getValueInUnit(final float value, final PkuLevelUnits unit) {
        if (unit == PkuLevelUnits.MICRO_MOL) {
            return value;
        }

        return value * MOL_TO_GRAM_CONVERSION_RATIO;
    }

}
