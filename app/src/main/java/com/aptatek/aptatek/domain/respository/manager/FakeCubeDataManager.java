package com.aptatek.aptatek.domain.respository.manager;

import android.support.annotation.Nullable;

import com.aptatek.aptatek.domain.respository.chart.CubeData;
import com.aptatek.aptatek.util.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import ix.Ix;

public class FakeCubeDataManager implements ICubeDataRepository {

    private List<CubeData> cubeDataList = new ArrayList<>();

    @Inject
    public FakeCubeDataManager() {
        init();
    }

    public FakeCubeDataManager(final List<CubeData> cubeDataList) {
        this.cubeDataList = cubeDataList;
    }

    private void init() {
        for (int i = 60; i >= 0; i--) {
            final Random random = new Random();
            final CubeData cubeData;
            final int level = random.nextInt(500) - 100;
            final double unit = (double) random.nextInt(500) / 100;
            final Date date = descendingDateList(i);
            cubeData = new CubeData(i, date, level, unit);
            cubeDataList.add(cubeData);
        }
    }

    @Nullable
    @Override
    public CubeData loadById(long id) {
        return Ix.from(cubeDataList)
                .filter(cubeData -> cubeData.getId() == id)
                .first(null);
    }

    @Override
    public List<CubeData> loadByDate(final Date date) {
        return Ix.from(cubeDataList)
                .filter(cubeData -> CalendarUtils.isSameDay(cubeData.getDate(), date))
                .toList();
    }

    @Override
    public List<CubeData> listAll() {
        return cubeDataList;
    }

    @Override
    public void removeById(long id) {
        Ix.from(cubeDataList)
                .remove(cubeData -> cubeData.getId() == id)
                .toList();
    }

    @Override
    public void removeAll() {
        Ix.from(cubeDataList).removeAll();
    }

    private Date descendingDateList(int index) {
        final Date dt = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DATE, -index);
        return calendar.getTime();
    }
}
