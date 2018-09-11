package com.aptatek.pkuapp.view.main;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.aptatek.pkuapp.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * @test.layer View / Main
 * @test.feature MainActivity, BubbleChart
 * @test.type Instrumented unit tests
 */
public class MainActivityScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    /**
     * Testing the initial view elements.
     *
     * @test.expected View appears, without any error.
     */
    @Test
    public void testInitialView() {
        onView(withId(R.id.newTestButton)).check(matches(isDisplayed()));
        onView(withId(R.id.settingsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.weeklyButton)).check(matches(isDisplayed()));
        onView(withId(R.id.titleText)).check(matches(isDisplayed()));
        onView(withId(R.id.subTitleText)).check(matches(isDisplayed()));

        onView(withId(R.id.scrollView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.playIcon)).check(matches(isDisplayed()));

        onView(withId(R.id.newTestButton)).check(matches(withText(R.string.main_button_new_test)));
        onView(withId(R.id.weeklyButton)).check(matches(withText(R.string.main_button_weekly)));
        onView(withId(R.id.titleText)).check(matches(withText(R.string.main_title_noresult)));
        onView(withId(R.id.subTitleText)).check(matches(withText(R.string.main_title_noresult_hint)));
    }

    /**
     * Clicking on Settings button.
     *
     * @test.expected After clicking on the button, the activity is changed to the new.
     */
    @Test
    public void testGoToSettings() {
        onView(withId(R.id.settingsButton)).perform(ViewActions.click());
        assert (activityRule.getActivity().isFinishing());
    }

    /**
     * Clicking on New Test button.
     *
     * @test.expected After clicking on the button, the activity is changed to the new.
     */
    @Test
    public void testGoToNewTest() {
        onView(withId(R.id.newTestButton)).perform(ViewActions.click());
        assert (activityRule.getActivity().isFinishing());
    }

    /**
     * Clicking on Result summary button.
     *
     * @test.expected After clicking on the button, the activity is changed to the new.
     */
    @Test
    public void testGoToResult() {
        onView(withId(R.id.weeklyButton)).perform(ViewActions.click());
        assert (activityRule.getActivity().isFinishing());
    }

    /**
     * Showing bubble chart.
     *
     * @test.expected After clicking on Play icon, the chart is shown.
     */
    @Test
    public void testShowChart() throws Exception {
        onView(withId(R.id.playIcon)).perform(ViewActions.click());
        Thread.sleep(1000L);
        onView(withId(R.id.playIcon)).check(matches(not(isDisplayed())));
        onView(withId(R.id.scrollView)).check(matches(isDisplayed()));
    }
}