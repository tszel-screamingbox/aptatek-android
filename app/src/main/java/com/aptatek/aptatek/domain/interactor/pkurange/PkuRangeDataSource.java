package com.aptatek.aptatek.domain.interactor.pkurange;

import com.aptatek.aptatek.domain.model.PkuLevelUnits;

public interface PkuRangeDataSource {

    void setNormalFloorValue(float value);

    void setNormalCeilValue(float value);

    void setDisplayUnit(PkuLevelUnits unit);

    float getNormalFloorValue();

    float getNormalCeilValue();

    float getHighCeilValue();

    PkuLevelUnits getDisplayUnit();
}
