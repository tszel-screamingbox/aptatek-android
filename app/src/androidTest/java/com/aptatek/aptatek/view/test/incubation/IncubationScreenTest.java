package com.aptatek.aptatek.view.test.incubation;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.view.test.TestActivity;

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
public class IncubationScreenTest {

    @Rule
    public ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class);

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.testNavigationButton)).perform(ViewActions.click());
    }

    @Test
    public void testInitialViewsVisible() throws Exception {
        onView(withId(R.id.testBaseTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.testBaseMessage)).check(matches(isDisplayed()));
        onView(withId(R.id.testCancelCircleButton)).check(matches(isDisplayed()));
        onView(withId(R.id.testNavigationButton)).check(matches(isDisplayed()));
        onView(withId(R.id.testCancelButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.incubationCounter)).check(matches(isDisplayed()));
    }

    @Test
    public void testInitialUiValues() throws Exception {
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_incubation_title)));
        onView(withId(R.id.testBaseMessage)).check(matches(withText(R.string.test_incubation_description)));
        onView(withId(R.id.testNavigationButton)).check(matches(withText(R.string.test_button_next)));

        Thread.sleep(2000L);

        onView(withId(R.id.incubationCounter)).check(matches(withText(StringContains.containsString("29:"))));
    }

    @Test
    public void testBackNavigation() throws Exception {
        onView(withId(R.id.testCancelCircleButton)).perform(ViewActions.click());
        onView(withId(R.id.testNavigationButton)).check(matches(withText(R.string.test_cancel_navigation_yes)));
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_cancel_title)));
        onView(withId(R.id.testBaseMessage)).check(matches(withText(R.string.test_cancel_description)));
    }

    @Test
    public void testForwardNavigation() throws Exception {
        onView(withId(R.id.testNavigationButton)).perform(ViewActions.click());
        // TODO implement next screen
    }

}