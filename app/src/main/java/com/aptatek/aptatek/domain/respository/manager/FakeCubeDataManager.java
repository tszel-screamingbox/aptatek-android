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

    private static final int SIZE = 60;
    private static final int RANGE = 650;
    private static final int OFFSET = 100;
    private List<CubeData> cubeDataList = new ArrayList<>();

    @Inject
    public FakeCubeDataManager() {
        init();
    }

    public FakeCubeDataManager(final List<CubeData> cubeDataList) {
        this.cubeDataList = cubeDataList;
    }

    private void init() {
        final Random random = new Random();

        cubeDataList = Ix.range(0, SIZE)
                .map(x -> CubeData.create(x, date(x), random.nextInt(RANGE) - OFFSET, (double) random.nextInt(RANGE) / OFFSET))
                .reverse()
                .toList();
    }

    @Nullable
    @Override
    public CubeData loadById(final long id) {
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
    public List<CubeData> loadByDate(final Date startDate, final Date endDate) {
        if (!startDate.before(endDate)) {
            throw new IllegalArgumentException("Start date must be earlier than end date!");
        }

        return Ix.from(cubeDataList)
                .filter(cubeData -> startDate.before(cubeData.getDate()) && endDate.after(cubeData.getDate()))
                .toList();
    }

    @Override
    public List<CubeData> listAll() {
        return cubeDataList;
    }

    @Override
    public void removeById(final long id) {
        Ix.from(cubeDataList)
                .remove(cubeData -> cubeData.getId() == id)
                .toList();
    }

    @Override
    public void removeAll() {
        Ix.from(cubeDataList).removeAll();
    }

    private Date date(final int index) {
        final Date dt = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DATE, -index);
        return calendar.getTime();
    }
}
