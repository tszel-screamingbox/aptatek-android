package com.aptatek.pkulab.domain.interactor;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.pkulab.device.PreferenceManager;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuRangeInteractor;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.injection.component.DaggerAndroidTestComponent;
import com.aptatek.pkulab.injection.module.ApplicationModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.util.Constants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the PkuRangeInteractor class
 *
 * @test.layer domain
 * @test.feature RangeInfo, RangeSettings, Charts
 * @test.type integration
 */
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

    /**
     * Tests whether the getInfo() method returns default values when the user has not set any custom range settings yet.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testGetInfoReturnsDefaults() throws Exception {
        final TestObserver<PkuRangeInfo> test = interactor.getInfo().test();
        test.assertNoErrors();
        test.assertComplete();
        test.assertValueAt(0, value -> {
            assertTrue(value.getHighCeilValue() == PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_INCREASED_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue());
            assertTrue(value.getNormalCeilValue() == PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_INCREASED_CEIL, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue());
            assertTrue(value.getNormalFloorValue() == PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_INCREASED_FLOOR, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue());
            assertTrue(value.getPkuLevelUnit() == PkuLevelUnits.MILLI_GRAM);

            return true;
        });
    }

    /**
     * Tests whether the getInfo() method returns proper values when the user has changed default values.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testGetInfoReturnsProperCustomRange() throws Exception {
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

    /**
     * Tests whether the getInfo() method returns updated values after changing the previously selected display unit.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testSaveDisplayUnitWorksProperly() throws Exception {
        final TestObserver<PkuRangeInfo> test = interactor.saveDisplayUnit(PkuLevelUnits.MILLI_GRAM)
                .andThen(interactor.getInfo()).test();
        test.assertComplete();
        test.assertNoErrors();
        test.assertValueAt(0, value -> value.getPkuLevelUnit() == PkuLevelUnits.MILLI_GRAM);
    }

    /**
     * Tests whether the getInfo() method returns updated values after a saveNormalRange(PkuLevel, PkuLevel) call.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testSaveNormalRangeWorksProperly() throws Exception {
        final float ceil = 500f;
        final float floor = 50f;
        final TestObserver<PkuRangeInfo> test = interactor.saveNormalRange(PkuLevel.create(floor, PkuLevelUnits.MICRO_MOL), PkuLevel.create(ceil, PkuLevelUnits.MICRO_MOL))
                .andThen(interactor.getInfo())
                .test();
        test.assertNoErrors();
        test.assertComplete();
        test.assertValueAt(0, value ->
                value.getNormalFloorValue() == PkuLevelConverter.convertTo(PkuLevel.create(floor, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue()
                        && value.getNormalCeilValue() == PkuLevelConverter.convertTo(PkuLevel.create(ceil, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue());
    }

    /**
     * Tests whether the saveNormalRange(PkuLevel, PkuLevel) signals an exception when the range values don't meet the requirements
     *
     * @test.input normalFloor = -1 mMol, normalCeil = 400000 mMol
     * @test.expected exception because the normalFloor is invalid
     */
    @Test
    public void testSaveNormalRangeSignalsExceptionOnInvalidRange() throws Exception {
        final float invalidFloor = -1f;
        final float invalidCeil = 40000f;

        final TestObserver<Void> test = interactor.saveNormalRange(PkuLevel.create(invalidFloor, PkuLevelUnits.MICRO_MOL), PkuLevel.create(invalidCeil, PkuLevelUnits.MICRO_MOL))
                .test();

        test.assertError(error -> error instanceof IllegalArgumentException);
    }

    /**
     * Tests whether the saveNormalRange(PkuLevel, PkuLevel) signals an exception when the floor value is higher than the ceil value
     *
     * @test.input normalFloor = 200 mMol, normalCeil = 5 mMol
     * @test.expected exception because the normalFloor is higher than the normalCeil
     */
    @Test
    public void testSaveValuesRange() throws Exception {
        final float floor = 200f;
        final float ceil = 5f;

        final TestObserver<Void> test = interactor.saveNormalRange(PkuLevel.create(floor, PkuLevelUnits.MICRO_MOL), PkuLevel.create(ceil, PkuLevelUnits.MICRO_MOL))
                .test();

        test.assertError(error -> error instanceof IllegalArgumentException);
    }

}
