package com.aptatek.aptatek.domain.respository.manager;

import android.support.annotation.Nullable;

import com.aptatek.aptatek.domain.respository.chart.CubeData;

import java.util.Date;
import java.util.List;

public interface ICubeDataRepository {

    @Nullable
    CubeData loadById(long id);

    List<CubeData> loadByDate(Date date);

    List<CubeData> listAll();

    void removeById(long id);

    void removeAll();
}
