package com.aptatek.aptatek.interactor;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.aptatek.domain.ReadingInteractor;
import com.aptatek.aptatek.domain.model.Reading;
import com.aptatek.aptatek.injection.component.DaggerTestComponent;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.TestDatabaseModule;

import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ReadingInteractorTest {

    @Inject
    ReadingInteractor readingInteractor;
    @Inject
    DataFactory dataFactory;

    private Reading MOCK_READING;

    @Before
    public void before() {
        DaggerTestComponent.builder()
                .applicationModule(new ApplicationModule(((Application) InstrumentationRegistry.getTargetContext().getApplicationContext())))
                .databaseModule(new TestDatabaseModule())
                .build()
                .inject(this);

        MOCK_READING = Reading.builder()
                .setInMicroMol(dataFactory.getNumberBetween(0, 1000))
                .setInMilligramPerDeciliter(dataFactory.getNumberBetween(0, 150) / 100f)
                .setTimestamp(System.currentTimeMillis())
                .build();
    }

    @Test
    public void testEmptyDatabaseCompletes() throws Exception {
        readingInteractor.listReadings()
                .test()
                .assertNoErrors()
                .assertComplete();
    }

    @Test
    public void testEmptyDatabaseReturnsEmptyListAndCompletes() throws Exception {
        readingInteractor.listReadings()
                .test()
                .assertValue(List::isEmpty)
                .assertComplete();
    }

    @Test
    public void testSaveCompletes() throws Exception {
        readingInteractor.saveReading(MOCK_READING)
                .test()
                .assertNoErrors()
                .assertComplete();
    }

    @Test
    public void testSaveWithNullFails() throws Exception {
        readingInteractor.saveReading(null)
                .test()
                .assertError(Objects::nonNull);
    }

    @Test
    public void testSaveWritesDatabaseCompletes() throws Exception {
        readingInteractor.saveReading(MOCK_READING)
                .andThen(readingInteractor.listReadings())
                .test()
                .assertValue(readings -> readings.size() == 1)
                .assertComplete();
    }

    @Test
    public void testSaveWritesDatabaseAndAssignsIdCompletes() throws Exception {
        readingInteractor.saveReading(MOCK_READING)
                .andThen(readingInteractor.listReadings())
                .test()
                .assertValue(readings -> readings.get(0).getId() > 0)
                .assertComplete();
    }

    @Test
    public void testSaveWritesDatabaseAndCanReadBackCompletes() throws Exception {
        readingInteractor.saveReading(MOCK_READING)
                .andThen(readingInteractor.listReadings())
                .test()
                .assertValue(readings -> {
                            final Reading reading = readings.get(0);
                            return reading.getInMicroMol() == MOCK_READING.getInMicroMol() &&
                                    reading.getInMilligramPerDeciliter() == MOCK_READING.getInMilligramPerDeciliter() &&
                                    reading.getTimestamp() == MOCK_READING.getTimestamp();
                        }
                )
                .assertComplete();
    }


}
