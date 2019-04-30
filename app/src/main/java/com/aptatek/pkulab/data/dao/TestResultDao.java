package com.aptatek.pkulab.data.dao;


import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aptatek.pkulab.data.model.TestResultDataModel;
import com.aptatek.pkulab.domain.interactor.testresult.TestResultDataSource;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface TestResultDao extends TestResultDataSource {

    @Query("SELECT * FROM test_results WHERE timestamp BETWEEN :from AND :to")
    @Override
    Flowable<List<TestResultDataModel>> getDataBetween(final long from, final long to);

    @Query("SELECT * FROM test_results ORDER BY timestamp ASC LIMIT 1")
    @Override
    TestResultDataModel getOldestData();

    @Query("SELECT * FROM test_results ORDER BY timestamp DESC LIMIT 1")
    @Override
    TestResultDataModel getLatestData();

    @Query("SELECT * FROM test_results WHERE readerId = :readerId ORDER BY timestamp DESC LIMIT 1")
    @Override
    TestResultDataModel getLatestFromReader(@NonNull final String readerId);

    @Query("SELECT * FROM test_results WHERE id = :id")
    TestResultDataModel getById(final String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Override
    void insertAll(@NonNull final List<TestResultDataModel> testResultDataModels);

    @Query("SELECT COUNT(*) FROM test_results")
    int getNumberOfRecords();

    @Query("SELECT * FROM test_results WHERE timestamp BETWEEN :from AND :to AND readerId LIKE :readerId")
    List<TestResultDataModel> listBetweenOnReader(final long from, final long to, final String readerId);

    @Query("UPDATE test_results SET sick = :sick, fasting = :fasting WHERE id = :id")
    void updateTestResult(final String id, final boolean sick, final boolean fasting);
}
