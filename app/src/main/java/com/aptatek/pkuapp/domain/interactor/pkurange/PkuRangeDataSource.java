package com.aptatek.pkuapp.domain.interactor.pkurange;

import com.aptatek.pkuapp.domain.model.PkuLevelUnits;

public interface PkuRangeDataSource {

    void setNormalFloorValueMMol(float value);

    void setNormalCeilValueMMol(float value);

    void setDisplayUnit(PkuLevelUnits unit);

    float getNormalFloorValueMMol();

    float getNormalCeilValueMMol();

    float getHighCeilValueMMol();

    float getNormalCeilAbsoluteMaxMMol();

    float getNormalFloorAbsoluteMinMMol();

    PkuLevelUnits getDisplayUnit();
}
