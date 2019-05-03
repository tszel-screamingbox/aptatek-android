package com.aptatek.pkulab.view.main;

import androidx.test.rule.ActivityTestRule;

import com.aptatek.pkulab.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * @test.layer View / Main
 * @test.feature MainHostActivity, BubbleChart, Weekly Chart
 * @test.type Instrumented unit tests
 */
public class MainActivityScreenTest {

    @Rule
    public ActivityTestRule<MainHostActivity> activityRule = new ActivityTestRule<>(MainHostActivity.class);


    /**
     * Testing the initial view elements.
     *
     * @test.expected View appears, without any error.
     */
    @Test
    public void testInitialView() throws Exception {
        onView(withId(R.id.playIcon)).check(matches(isDisplayed()));
        onView(withId(R.id.newTestButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.settingsButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bigSettingsButton)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));

        onView(withId(R.id.scrollView)).check(matches(not(isDisplayed())));
    }

    /**
     * Clicking on play icon.
     *
     * @test.expected After clicking on the button, the activity is changed to the new.
     */
    @Test
    public void testGoToNewTest() throws Exception {
        onView(withId(R.id.playIcon)).perform(click());
        assert (activityRule.getActivity().isFinishing());
    }

}
