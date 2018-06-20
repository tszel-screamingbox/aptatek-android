package com.aptatek.aptatek.domain;

import com.aptatek.aptatek.data.AptatekDatabase;
import com.aptatek.aptatek.data.dao.ReadingDao;
import com.aptatek.aptatek.data.mapper.ReadingMapper;
import com.aptatek.aptatek.domain.model.Reading;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ReadingInteractor {

    private final ReadingDao readingDao;
    private final ReadingMapper readingMapper;

    @Inject
    ReadingInteractor(AptatekDatabase aptatekDatabase, ReadingMapper readingMapper) {
        readingDao = aptatekDatabase.getReadingDao();
        this.readingMapper = readingMapper;
    }

    public Completable saveReading(Reading reading) {
        return Completable.fromAction(() -> readingDao.addReading(readingMapper.toData(reading)));
    }

    public Single<List<Reading>> listReadings() {
        return readingDao.getReadings()
                .map(readingMapper::toDomainList);
    }

}
