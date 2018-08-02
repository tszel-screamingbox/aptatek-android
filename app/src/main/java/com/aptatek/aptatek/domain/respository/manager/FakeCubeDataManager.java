package com.aptatek.aptatek.domain.respository.manager;

import android.support.annotation.Nullable;

import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.respository.chart.CubeData;
import com.aptatek.aptatek.util.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import ix.Ix;

@Singleton
public class FakeCubeDataManager implements ICubeDataRepository {

    private static final int SIZE = 30;
    private static final int HOURS_IN_A_DAY = 24;
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
                .map(x -> CubeData.create(x, date(x), PkuLevel.create((float) random.nextInt(RANGE) / OFFSET, PkuLevelUnits.MILLI_GRAM)))
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
        final Random random = new Random();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DATE, -index);
        calendar.add(Calendar.HOUR, random.nextInt(HOURS_IN_A_DAY));
        return calendar.getTime();
    }
}
