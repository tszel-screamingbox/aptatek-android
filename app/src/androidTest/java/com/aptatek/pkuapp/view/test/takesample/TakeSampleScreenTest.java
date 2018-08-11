package com.aptatek.pkuapp.view.test.takesample;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.view.test.TestActivity;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class TakeSampleScreenTest {

    @Rule
    public ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testInitialViewsVisible() throws Exception {
        onView(withId(R.id.testBaseTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.testBaseMessage)).check(matches(isDisplayed()));
        onView(withId(R.id.testCancelCircleButton)).check(matches(isDisplayed()));
        onView(withId(R.id.testNavigationButton)).check(matches(isDisplayed()));
        onView(withId(R.id.testCancelButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.takeSampleAgeToggle)).check(matches(isDisplayed()));
        onView(withId(R.id.takeSampleVideo)).check(matches(isDisplayed()));
    }

    @Test
    public void testInitialUiValues() throws Exception {
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_takesample_title)));
        onView(withId(R.id.testBaseMessage)).check(matches(withText(R.string.test_takesample_description)));
        onView(withId(R.id.testNavigationButton)).check(matches(withText(R.string.test_takesample_button_start)));
        onView(withId(R.id.takeSampleAgeToggle)).check(matches(withText(StringContains.containsString(activityRule.getActivity().getString(R.string.test_takesample_age_child)))));
    }

    @Test
    public void testCancelNavigatesBack() throws Exception {
        onView(withId(R.id.testCancelCircleButton)).perform(ViewActions.click());
        assert(activityRule.getActivity().isFinishing());
    }

    @Test
    public void testAgeSwitch() throws Exception {
        onView(withId(R.id.takeSampleAgeToggle)).perform(ViewActions.click());
        onView(withId(R.id.takeSampleAgeToggle)).check(matches(withText(StringContains.containsString(activityRule.getActivity().getString(R.string.test_takesample_age_adult)))));
    }

    @Test
    public void testNextNavigatesForward() throws Exception {
        onView(withId(R.id.testNavigationButton)).perform(ViewActions.click());
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_incubation_title)));
    }

}
