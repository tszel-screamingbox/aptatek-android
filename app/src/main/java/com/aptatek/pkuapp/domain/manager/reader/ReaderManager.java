package com.aptatek.pkuapp.domain.manager.reader;

import io.reactivex.Flowable;

public interface ReaderManager {

    Flowable<Integer> batteryLevel();

    // TODO gather other characteristics

}
