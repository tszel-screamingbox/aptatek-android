package com.aptatek.aptatek.domain.respository.manager;

import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.CubeData;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ix.Ix;

import static org.junit.Assert.assertEquals;

public class FakeCubeDataManagerTest {

    private static final int SIZE = 10;

    private FakeCubeDataManager fakeCubeDataManager;
    private List<CubeData> cubeDataList = new ArrayList<>();

    @Before
    public void setUp() {
        cubeDataList = Ix.range(0, SIZE)
                .map(x -> CubeData.create(x, date(x), PkuLevel.create(1.27f, PkuLevelUnits.MICRO_MOL)))
                .toList();
        fakeCubeDataManager = new FakeCubeDataManager(cubeDataList);
    }

    private Date date(final int before) {
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -before);
        return calendar.getTime();
    }

    @Test
    public void testFindById() {
        assertEquals(fakeCubeDataManager.loadById(0), cubeDataList.get(0));
    }

    @Test
    public void testCantFindById() {
        assertEquals(fakeCubeDataManager.loadById(-1), null);
    }

    @Test
    public void testFindByDate() {
        final List<CubeData> filtered = fakeCubeDataManager.loadByDate(date(5));
        assertEquals(filtered.size(), 1);
        assertEquals(filtered.get(0), cubeDataList.get(5));
    }

    @Test
    public void testCantFindByDate() {
        assertEquals(fakeCubeDataManager.loadByDate(date(100)).size(), 0);
    }

    @Test
    public void listAll() {
        assertEquals(fakeCubeDataManager.listAll().size(), cubeDataList.size());
        assertEquals(fakeCubeDataManager.listAll(), cubeDataList);
    }

    @Test
    public void testFilterForToday() {
        final Date date = cubeDataList.get(0).getDate();
        final List<CubeData> cubeDataList = fakeCubeDataManager.loadByDate(date(1), date(-1));
        assertEquals(cubeDataList.size(), 1);
        assertEquals(cubeDataList.get(0).getDate(), date);
    }

    @Test
    public void testFilterWithDates() {
        final List<CubeData> subList = cubeDataList.subList(0, 4);
        final List<CubeData> cubeDataList = fakeCubeDataManager.loadByDate(date(4), date(-1));
        assertEquals(cubeDataList.size(), subList.size());
        assertEquals(subList, cubeDataList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters() {
        fakeCubeDataManager.loadByDate(date(-1), date(1));
    }

    @Test
    public void removeById() {
        fakeCubeDataManager.removeById(0);
        assertEquals(fakeCubeDataManager.listAll().size(), SIZE - 1);
        assertEquals(fakeCubeDataManager.loadById(0), null);
    }

    @Test
    public void removeNonExistingElement() {
        fakeCubeDataManager.removeById(-1);
        assertEquals(fakeCubeDataManager.listAll().size(), SIZE);
    }

    @Test
    public void removeAll() {
        fakeCubeDataManager.removeAll();
        assertEquals(fakeCubeDataManager.listAll().size(), 0);
    }
}