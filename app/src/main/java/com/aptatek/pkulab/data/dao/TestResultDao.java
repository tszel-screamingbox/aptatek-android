package com.aptatek.pkulab.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.data.model.TestResultDataModel;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface TestResultDao {

    @Insert
    void insert(final TestResultDataModel testResultDataModel);

    @Insert
    void insertAll(final List<TestResultDataModel> testResultDataModels);

    @Query("SELECT COUNT(*) FROM test_results")
    Single<Integer> getNumberOfRecords();

    @Query("SELECT * FROM test_results")
    Single<List<TestResultDataModel>> listAll();

    @Query("SELECT * FROM test_results WHERE timestamp BETWEEN :from AND :to")
    Single<List<TestResultDataModel>> listBetween(final long from, final long to);

    @Query("SELECT * FROM test_results WHERE timestamp BETWEEN :from AND :to AND readerId LIKE :readerId")
    Single<List<TestResultDataModel>> listBetweenOnReader(final long from, final long to, final String readerId);

    @Query("UPDATE test_results SET sick = :sick, fasting = :fasting WHERE id = :id")
    void updateTestResult(final String id, final boolean sick, final boolean fasting);

    @Query("SELECT * FROM test_results ORDER BY timestamp DESC LIMIT 1")
    Single<TestResultDataModel> getLatestResult();

    @Query("SELECT * FROM test_results ORDER BY timestamp ASC LIMIT 1")
    Single<TestResultDataModel> getOldestResult();
}
