package com.aptatek.aptatek.domain.respository;

import android.support.annotation.Nullable;

import com.aptatek.aptatek.data.chart.CubeData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

public class FakeCubeDataManager implements ICubeDataManager {

    private List<CubeData> cubeDataList = new ArrayList<>();

    @Inject
    public FakeCubeDataManager() {
        init();
    }

    private void init() {
        for (int i = 0; i < 100; i++) {
            if (i % 10 != 0) {
                final Random random = new Random();
                final CubeData cubeData = new CubeData(i,
                        generateDescendingDateList(i),
                        random.nextInt(500),
                        random.nextInt(100) / 10);
                cubeDataList.add(cubeData);
            }
        }
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

    private Date generateDescendingDateList(int index) {
        if (index % 4 == 0) {
            index = index - 1; // more measure on the same date
        }

        Date dt = new Date();
        final Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, -index);
        return c.getTime();
    }
}
