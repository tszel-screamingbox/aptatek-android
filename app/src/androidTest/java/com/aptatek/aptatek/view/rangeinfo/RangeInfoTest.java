package com.aptatek.aptatek.view.rangeinfo;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.aptatek.aptatek.injection.component.DaggerAndroidTestComponent;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.util.Constants;

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
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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

    @Test
    public void testInitialViewsVisible() {
        onView(withId(R.id.rangeinfo_title)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_message)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_back)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_edit)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_units)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_high_label)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_high)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_veryhigh_label)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_veryhigh)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_normal_label)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_normal)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_low_label)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeinfo_low)).check(matches(isDisplayed()));
    }

    @Test
    public void testInitialUiValues() {
        final PkuRangeInfo pkuInfo = PkuRangeInfo.builder()
                .setHighCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL + Constants.DEFAULT_PKU_HIGH_RANGE)
                .setNormalCeilValue(Constants.DEFAULT_PKU_NORMAL_CEIL)
                .setNormalFloorValue(Constants.DEFAULT_PKU_NORMAL_FLOOR)
                .setPkuLevelUnit(Constants.DEFAULT_PKU_LEVEL_UNIT)
                .setNormalAbsoluteMinValue(Constants.DEFAULT_PKU_LOWEST_VALUE)
                .setNormalAbsoluteMaxValue(Constants.DEFAULT_PKU_HIGHEST_VALUE)
                .build();

        onView(withId(R.id.rangeinfo_title)).check(matches(withText(R.string.rangeinfo_title)));
        onView(withId(R.id.rangeinfo_message)).check(matches(withText(R.string.rangeinfo_message)));
        onView(withId(R.id.rangeinfo_back)).check(matches(withText(R.string.rangeinfo_back)));
        onView(withId(R.id.rangeinfo_edit)).check(matches(withText(R.string.rangeinfo_edit_level_preferences)));
        onView(withId(R.id.rangeinfo_units)).check(matches(withText(formatter.formatUnits(pkuInfo))));
        onView(withId(R.id.rangeinfo_high_label)).check(matches(withText(R.string.rangeinfo_high)));
        onView(withId(R.id.rangeinfo_high)).check(matches(withText(formatter.formatHigh(pkuInfo))));
        onView(withId(R.id.rangeinfo_veryhigh_label)).check(matches(withText(R.string.rangeinfo_very_high)));
        onView(withId(R.id.rangeinfo_veryhigh)).check(matches(withText(formatter.formatVeryHigh(pkuInfo))));
        onView(withId(R.id.rangeinfo_normal_label)).check(matches(withText(R.string.rangeinfo_normal)));
        onView(withId(R.id.rangeinfo_normal)).check(matches(withText(formatter.formatNormal(pkuInfo))));
        onView(withId(R.id.rangeinfo_low_label)).check(matches(withText(R.string.rangeinfo_low)));
        onView(withId(R.id.rangeinfo_low)).check(matches(withText(formatter.formatLow(pkuInfo))));
    }

    @Test
    public void testBackFinishesActivity() throws Exception {
        onView(withId(R.id.rangeinfo_back)).perform(click());

        Assert.assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void testUnitClickTakesToSettings() throws Exception {
        onView(withId(R.id.rangeinfo_edit)).perform(click());
        onView(withId(R.id.rangeSettingsLowLabel)).check(matches(isDisplayed()));
    }

}
