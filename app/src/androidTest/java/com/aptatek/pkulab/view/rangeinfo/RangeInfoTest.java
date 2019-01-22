package com.aptatek.pkulab.view.rangeinfo;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.interactor.pkurange.PkuLevelConverter;
import com.aptatek.pkulab.domain.model.PkuLevel;
import com.aptatek.pkulab.domain.model.PkuLevelUnits;
import com.aptatek.pkulab.domain.model.PkuRangeInfo;
import com.aptatek.pkulab.injection.component.DaggerAndroidTestComponent;
import com.aptatek.pkulab.injection.module.ApplicationModule;
import com.aptatek.pkulab.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.pkulab.util.Constants;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests for the RangeInfo screen.
 *
 * @test.layer presentation
 * @test.feature RangeInfo
 * @test.type integration
 */
@RunWith(AndroidJUnit4.class)
public class RangeInfoTest {

    @Rule
    public ActivityTestRule<RangeInfoActivity> activityRule = new ActivityTestRule<>(RangeInfoActivity.class);

    @Inject
    PkuValueFormatter formatter;

    @Before
    public void setUp() throws Exception {
        DaggerAndroidTestComponent.builder()
                .applicationModule(new ApplicationModule(((Application) InstrumentationRegistry.getTargetContext().getApplicationContext())))
                .build()
                .plus(new RangeInfoModule())
                .inject(this);
    }

    /**
     * Tests the initial visibility of every view on this screen
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testInitialViewsVisible() {
        onView(withId(R.id.rangeinfo_title)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_message)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_edit)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_units)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_high)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_very_high)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_normal)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_low)).check(matches(isDisplayed()));
    }

    /**
     * Tests the initial values of every view
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testInitialUiValues() {
        final PkuRangeInfo pkuInfo = PkuRangeInfo.builder()
                .setHighCeilValue(PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_NORMAL_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue())
                .setNormalCeilValue(PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_NORMAL_CEIL, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue())
                .setNormalFloorValue(PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_NORMAL_FLOOR, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue())
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .setNormalAbsoluteMinValue(PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_LOWEST_VALUE, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue())
                .setNormalAbsoluteMaxValue(PkuLevelConverter.convertTo(PkuLevel.create(Constants.DEFAULT_PKU_HIGHEST_VALUE, PkuLevelUnits.MICRO_MOL), PkuLevelUnits.MILLI_GRAM).getValue())
                .build();

        onView(withId(R.id.rangeinfo_title)).check(matches(withText(R.string.rangeinfo_title)));
        onView(withId(R.id.rangeinfo_message)).check(matches(withText(R.string.rangeinfo_message)));
        onView(withId(R.id.rangeinfo_edit)).check(matches(withText(R.string.rangeinfo_edit_level_preferences)));
        onView(withId(R.id.rangeinfo_units)).check(matches(withText(formatter.formatUnits(pkuInfo))));
        onView(Matchers.allOf(withId(R.id.title), withParent(withId(R.id.rangeinfo_high)))).check(matches(withText(formatter.formatHigh(pkuInfo))));
        onView(Matchers.allOf(withId(R.id.title), withParent(withId(R.id.rangeinfo_very_high)))).check(matches(withText(formatter.formatVeryHigh(pkuInfo))));
        onView(Matchers.allOf(withId(R.id.title), withParent(withId(R.id.rangeinfo_normal)))).check(matches(withText(formatter.formatNormal(pkuInfo))));
        onView(Matchers.allOf(withId(R.id.title), withParent(withId(R.id.rangeinfo_low)))).check(matches(withText(formatter.formatLow(pkuInfo))));
    }

    /**
     * Tests whether the screen is dismissed when the user presses the back button
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testBackFinishesActivity() {
        onView(withId(R.id.rangeinfo_edit)).perform(click());

        Assert.assertTrue(activityRule.getActivity().isFinishing());
    }

    /**
     * Tests whether the app navigates to the RangeSettings screen when the user taps on the "Edit ranges" view.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testUnitClickTakesToSettings() {
        onView(withId(R.id.rangeinfo_edit)).perform(click());
        onView(withId(R.id.rangeSettingsLowLabel)).check(matches(isDisplayed()));
    }

}
