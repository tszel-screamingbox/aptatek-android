package com.aptatek.aptatek.view.rangesettings;

import android.app.Application;
import android.inputmethodservice.Keyboard;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.view.View;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.model.PkuRangeInfo;
import com.aptatek.aptatek.injection.component.DaggerAndroidTestComponent;
import com.aptatek.aptatek.injection.module.ApplicationModule;
import com.aptatek.aptatek.injection.module.rangeinfo.RangeInfoModule;
import com.aptatek.aptatek.util.Constants;
import com.aptatek.aptatek.view.rangeinfo.PkuValueFormatter;
import com.aptatek.aptatek.view.rangeinfo.RangeInfoActivity;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsActivity;
import com.aptatek.aptatek.view.settings.pkulevel.RangeSettingsValueFormatter;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParentIndex;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;

@RunWith(AndroidJUnit4.class)
public class RangeSettingsTest {

    @Rule
    public ActivityTestRule<RangeSettingsActivity> activityRule = new ActivityTestRule<>(RangeSettingsActivity.class);

    @Inject
    RangeSettingsValueFormatter formatter;

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
        onView(withId(R.id.rangeSettingsLowLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsLowDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsLowIndicator)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsNormalLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsNormalDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsNormalFloor)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsNormalCeil)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsRangeBar)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsHighLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsHighDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsHighIndicator)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsVeryHighLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsVeryHighDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsVeryHighIndicator)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsUnitLabel)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsUnitMicroMol)).check(matches(isDisplayed()));
        onView(withId(R.id.rangeSettingsUnitMilliGram)).check(matches(isDisplayed()));
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

        onView(withId(R.id.rangeSettingsLowLabel)).check(matches(withText(R.string.rangeinfo_low)));
        onView(withId(R.id.rangeSettingsLowDescription)).check(matches(withText(formatter.getFormattedLow(pkuInfo))));
        onView(withId(R.id.rangeSettingsNormalLabel)).check(matches(withText(R.string.rangeinfo_normal)));
        onView(withId(R.id.rangeSettingsNormalDescription)).check(matches(withText(R.string.settings_units_normal_description)));
        onView(withId(R.id.rangeSettingsHighLabel)).check(matches(withText(R.string.rangeinfo_high)));
        onView(withId(R.id.rangeSettingsHighDescription)).check(matches(withText(formatter.getFormattedHigh(pkuInfo))));
        onView(withId(R.id.rangeSettingsVeryHighLabel)).check(matches(withText(R.string.rangeinfo_very_high)));
        onView(withId(R.id.rangeSettingsVeryHighDescription)).check(matches(withText(formatter.getFormattedVeryHigh(pkuInfo))));
        onView(withId(R.id.rangeSettingsUnitLabel)).check(matches(withText(R.string.settings_units_label)));
        onView(withId(R.id.rangeSettingsUnitMicroMol)).check(matches(withText(R.string.rangeinfo_pkulevel_mmol)));
        onView(withId(R.id.rangeSettingsUnitMilliGram)).check(matches(withText(R.string.rangeinfo_pkulevel_mg)));
    }

    @Test
    public void testBackDoesntPopDialogFinishesActivity() throws Exception {
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

        Assert.assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void testBackPopsDialogAndFinishesActivity() throws Exception {
        onView(withId(R.id.rangeSettingsNormalCeil)).perform(click());

        Thread.sleep(1000L);

        onView(withId(R.id.rangeSettingsNormalCeil)).perform(ViewActions.replaceText("999"), pressImeActionButton());

        Thread.sleep(1000L);

        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

        onView(withText(R.string.settings_units_confirmation_title)).check(matches(isDisplayed()));
        onView(withText(R.string.settings_units_confirmation_message)).check(matches(isDisplayed()));
        onView(withText(R.string.settings_units_confirmation_button_cancel)).perform(click());

        Assert.assertTrue(activityRule.getActivity().isFinishing());
    }

    @Test
    public void testUnitChangeRefreshesUi() throws Exception {
        onView(withId(R.id.rangeSettingsUnitMilliGram)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isEnabled(); // no constraints, they are checked above
            }

            @Override
            public String getDescription() {
                return "click button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.performClick();
            }
        });

        Thread.sleep(1000L);

        onView(withId(R.id.rangeSettingsLowDescription)).check(matches(withText(containsString(activityRule.getActivity().getString(R.string.rangeinfo_pkulevel_mg)))));
        onView(withId(R.id.rangeSettingsHighDescription)).check(matches(withText(containsString(activityRule.getActivity().getString(R.string.rangeinfo_pkulevel_mg)))));
        onView(withId(R.id.rangeSettingsVeryHighDescription)).check(matches(withText(containsString(activityRule.getActivity().getString(R.string.rangeinfo_pkulevel_mg)))));
    }

}
