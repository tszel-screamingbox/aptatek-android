package com.aptatek.aptatek.domain.interactor;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.aptatek.device.PreferenceManager;
import com.aptatek.aptatek.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.aptatek.domain.model.PkuLevel;
import com.aptatek.aptatek.domain.model.PkuLevelUnits;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.aptatek.aptatek.injection.component.DaggerAndroidTestComponent;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PkuRangeInteractorTest {

    @Inject
    PkuRangeInteractor interactor;

    @Inject
    PreferenceManager preferenceManager;

    @Before
    public void setUp() throws Exception {
        DaggerAndroidTestComponent.builder()
                .applicationModule(new ApplicationModule(((Application) InstrumentationRegistry.getTargetContext().getApplicationContext())))
                .build()
                .plus(new RangeInfoModule())
                .inject(this);
    }

    @Test
    public void testGetInfoReturnsDefaults() throws Exception {
        final TestObserver<PkuRangeInfo> test = interactor.getInfo().test();
        test.assertNoErrors();
        test.assertComplete();
        test.assertValueAt(0, value -> {
            assertTrue(value.getHighCeilValue() == Constants.DEFAULT_PKU_NORMAL_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE);
            assertTrue(value.getNormalCeilValue() == Constants.DEFAULT_PKU_NORMAL_CEIL);
            assertTrue(value.getNormalFloorValue() == Constants.DEFAULT_PKU_NORMAL_FLOOR);
            assertTrue(value.getPkuLevelUnit() == Constants.DEFAULT_PKU_LEVEL_UNIT);

            return true;
        });
    }

    @Test
    public void testGetInfoReturnProper() throws Exception {
        final PkuLevelUnits unit = PkuLevelUnits.MICRO_MOL;
        final float ceil = 400f;
        final float floor = 50f;
        preferenceManager.setPkuRangeUnit(unit);
        preferenceManager.setPkuRangeNormalCeil(ceil);
        preferenceManager.setPkuRangeNormalFloor(floor);

        final TestObserver<PkuRangeInfo> test = interactor.getInfo().test();
        test.assertComplete();
        test.assertNoErrors();
        test.assertValueAt(0, value -> {
            assertTrue(value.getPkuLevelUnit() == unit);
            assertTrue(value.getNormalFloorValue() == floor);
            assertTrue(value.getNormalCeilValue() == ceil);
            assertTrue(value.getHighCeilValue() == ceil + Constants.DEFAULT_PKU_HIGH_RANGE);
            return true;
        });
    }

    @Test
    public void testSaveDisplayUnit() throws Exception {
        final TestObserver<PkuRangeInfo> test = interactor.saveDisplayUnit(PkuLevelUnits.MILLI_GRAM)
                .andThen(interactor.getInfo()).test();
        test.assertComplete();
        test.assertNoErrors();
        test.assertValueAt(0, value -> value.getPkuLevelUnit() == PkuLevelUnits.MILLI_GRAM);
    }

    @Test
    public void testSaveValues() throws Exception {
        final float ceil = 500f;
        final float floor = 50f;
        final TestObserver<PkuRangeInfo> test = interactor.saveNormalRange(PkuLevel.create(floor, PkuLevelUnits.MICRO_MOL), PkuLevel.create(ceil, PkuLevelUnits.MICRO_MOL))
                .andThen(interactor.getInfo())
                .test();
        test.assertNoErrors();
        test.assertComplete();
        test.assertValueAt(0, value -> value.getNormalFloorValue() == floor && value.getNormalCeilValue() == ceil);
    }

    @Test
    public void testSaveValuesInRange() throws Exception {
        final float invalidFloor = -1f;
        final float invalidCeil = 40000f;

        final TestObserver<Void> test = interactor.saveNormalRange(PkuLevel.create(invalidFloor, PkuLevelUnits.MICRO_MOL), PkuLevel.create(invalidCeil, PkuLevelUnits.MICRO_MOL))
                .test();

        test.assertError(error -> error instanceof IllegalArgumentException);
    }

    @Test
    public void testSaveValuesRange() throws Exception {
        final float floor = 200f;
        final float ceil = 5f;

        final TestObserver<Void> test = interactor.saveNormalRange(PkuLevel.create(floor, PkuLevelUnits.MICRO_MOL), PkuLevel.create(ceil, PkuLevelUnits.MICRO_MOL))
                .test();

        test.assertError(error -> error instanceof IllegalArgumentException);
    }

}
