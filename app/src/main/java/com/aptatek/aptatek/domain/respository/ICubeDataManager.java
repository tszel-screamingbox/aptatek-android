package com.aptatek.aptatek.domain.respository;

import android.support.annotation.Nullable;

import com.aptatek.aptatek.data.chart.CubeData;

import java.util.Date;
import java.util.List;

public interface ICubeDataManager {

    @Nullable
    CubeData loadById(final long id);

    @Nullable
    List<CubeData> loadByDate(final Date date);

    List<CubeData> listAllItems();

    List<CubeData> filterByDate(final Date startDate, final Date endDate);

    void removeById(final long id);

    void removeAll();
}
