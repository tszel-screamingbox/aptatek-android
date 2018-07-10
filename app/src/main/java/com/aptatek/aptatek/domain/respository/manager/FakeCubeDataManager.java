package com.aptatek.aptatek.domain.respository.manager;

import android.support.annotation.Nullable;

import com.aptatek.aptatek.domain.respository.chart.CubeData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

public class FakeCubeDataManager implements ICubeDataRepository {

    private List<CubeData> cubeDataList = new ArrayList<>();

    @Inject
    public FakeCubeDataManager() {
        init();
    }

    private void init() {
        for (int i = 0; i < 60; i++) {
            final Random random = new Random();
            final CubeData cubeData;
            final int level = random.nextInt(500) - 100;
            final double unit = (double) random.nextInt(500) / 100;
            cubeData = new CubeData(i, descendingDateList(i), level, unit);
            cubeDataList.add(cubeData);
        }
    }

    public static void main(String[] args) {
        final Random random = new Random();
        System.out.println("asas " + random.nextInt(500) / 100);
    }

    @Nullable
    @Override
    public CubeData loadById(long id) {
        return null;
    }

    @Nullable
    @Override
    public List<CubeData> loadByDate(final Date date) {
        return null;
    }

    @Override
    public List<CubeData> listAllItems() {
        return cubeDataList;
    }

    @Override
    public List<CubeData> filterByDate(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public void removeById(long id) {

    }

    @Override
    public void removeAll() {
        cubeDataList.clear();
    }

    private Date descendingDateList(int index) {
        Date dt = new Date();
        final Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, -index);
        return c.getTime();
    }
}
