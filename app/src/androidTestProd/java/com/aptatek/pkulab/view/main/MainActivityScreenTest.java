package com.aptatek.pkulab.view.main;

import android.support.test.rule.ActivityTestRule;

import com.aptatek.pkulab.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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
        onView(withText(R.string.home_range_dialog_message)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).perform(click());

        Thread.sleep(2000L);

        onView(withId(R.id.playIcon)).check(matches(isDisplayed()));
        onView(withId(R.id.newTestButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.settingsButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bigSettingsButton)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));

        onView(withId(R.id.scrollView)).check(matches(not(isDisplayed())));
    }

    /**
     * Clicking on Set Phe Levels button in the shown dialog.
     *
     * @test.expected After clicking on the button, the activity is changed to the new.
     */
    @Test
    public void testGoToRangeSettings() {
        onView(withText(R.string.home_range_dialog_message)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        assert (activityRule.getActivity().isFinishing());
    }

    /**
     * Clicking on Settings button.
     *
     * @test.expected After clicking on the button, the activity is changed to the new.
     */
    @Test
    public void testGoToSettings() throws Exception {
        onView(withText(R.string.home_range_dialog_message)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).perform(click());

        Thread.sleep(2000L);

        onView(withId(R.id.bigSettingsButton)).perform(click());
        assert (activityRule.getActivity().isFinishing());
    }

    /**
     * Clicking on play icon.
     *
     * @test.expected After clicking on the button, the activity is changed to the new.
     */
    @Test
    public void testGoToNewTest() throws Exception {
        onView(withText(R.string.home_range_dialog_message)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).perform(click());

        Thread.sleep(2000L);

        onView(withId(R.id.playIcon)).perform(click());
        assert (activityRule.getActivity().isFinishing());
    }

}
