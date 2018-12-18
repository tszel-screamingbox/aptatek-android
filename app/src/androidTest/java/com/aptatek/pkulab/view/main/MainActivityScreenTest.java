package com.aptatek.pkulab.view.main;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.aptatek.pkulab.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * @test.layer View / Main
 * @test.feature MainHostActivity, BubbleChart, Weekly Chart
 * @test.type Instrumented unit tests
 */
public class MainActivityScreenTest {

    @Rule
    public ActivityTestRule<MainHostActivity> activityRule = new ActivityTestRule<>(MainHostActivity.class);

    // TODO need to mock at least presentation layer as soon as we get official decision

    /**
     * Testing the initial view elements.
     *
     * @test.expected View appears, without any error.
     */
    @Test
    public void testInitialView() {
//        onView(withId(R.id.newTestButton)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.settingsButton)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.bigSettingsButton)).check(matches(isDisplayed()));
//        onView(withId(R.id.titleText)).check(matches(isDisplayed()));
//        onView(withId(R.id.subTitleText)).check(matches(isDisplayed()));
//
//        onView(withId(R.id.scrollView)).check(matches(not(isDisplayed())));
//
//        onView(withId(R.id.newTestButton)).check(matches(withText(R.string.main_button_new_test)));
//        onView(withId(R.id.titleText)).check(matches(withText(R.string.main_title_noresult)));
//        onView(withId(R.id.subTitleText)).check(matches(withText(R.string.main_title_noresult_hint)));
    }

    /**
     * Clicking on Settings button.
     *
     * @test.expected After clicking on the button, the activity is changed to the new.
     */
    @Test
    public void testGoToSettings() {
//        onView(withId(R.id.settingsButton)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.playIcon)).perform(ViewActions.click());
//        onView(withId(R.id.settingsButton)).perform(ViewActions.click());
//        assert (activityRule.getActivity().isFinishing());
    }

    /**
     * Clicking on New Test button.
     *
     * @test.expected After clicking on the button, the activity is changed to the new.
     */
    @Test
    public void testGoToNewTest() {
//        onView(withId(R.id.newTestButton)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.playIcon)).perform(ViewActions.click());
//        onView(withId(R.id.newTestButton)).perform(ViewActions.click());
//        assert (activityRule.getActivity().isFinishing());
    }

    /**
     * Showing bubble chart.
     *
     * @test.expected After clicking on Play icon, the chart is shown.
     */
    @Test
    public void testShowChart() throws Exception {
//        onView(withId(R.id.playIcon)).perform(ViewActions.click());
//        Thread.sleep(1000L);
//        onView(withId(R.id.playIcon)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.scrollView)).check(matches(isDisplayed()));
    }


    /**
     * Testing the initial WeeklyResult view.
     *
     * @test.expected View appears, without any error.
     */
    @Test
    public void testInitialWeeklyResult() throws Exception {
//        showWeeklyFragment();
//
//        onView(withId(R.id.title)).check(matches(isDisplayed()));
//        onView(withId(R.id.roundedBar)).check(matches(isDisplayed()));
//        onView(withId(R.id.leftArrow)).check(matches(isDisplayed()));
//        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
//        onView(withId(R.id.label)).check(matches(isDisplayed()));
//        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));
//        onView(withId(R.id.buttonPdfExport)).check(matches(isDisplayed()));
    }

    /**
     * Testing WeeklyResult fragment's play icon clicking.
     *
     * @test.expected After clicking on play icon, it disappears.
     */
    @Test
    public void testWeeklyPlayIconClicked() throws Exception {
//        showWeeklyFragment();
//
//        onView(withId(R.id.title)).check(matches(isDisplayed()));
//        onView(withId(R.id.roundedBar)).check(matches(isDisplayed()));
//        onView(withId(R.id.leftArrow)).check(matches(isDisplayed()));
//        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
//        onView(withId(R.id.label)).check(matches(isDisplayed()));
//        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));
//        onView(withId(R.id.buttonPdfExport)).check(matches(isDisplayed()));
//
//        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
    }


    /**
     * Testing WeeklyResult fragment's pagination by swiping.
     *
     * @test.expected Swiping right, the right arrow is shown, then swipe left and it disappears.
     */
    @Test
    public void testSwipe() throws Exception {
//        showWeeklyFragment();
//
//        onView(withId(R.id.leftArrow)).check(matches(isDisplayed()));
//        onView(withId(R.id.viewpager)).perform(swipeRight());
//        onView(withId(R.id.roundedBar)).check(matches(isDisplayed()));
//        onView(withId(R.id.buttonPdfExport)).check(matches(isDisplayed()));
//        onView(withId(R.id.title)).check(matches(isDisplayed()));
//        onView(withId(R.id.rightArrow)).check(matches(isDisplayed()));
//        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
//        onView(withId(R.id.label)).check(matches(isDisplayed()));
//        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));
//
//        onView(withId(R.id.viewpager)).perform(swipeLeft());
//        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
    }

    /**
     * Testing pagination by clicking on the arrows icons.
     *
     * @test.expected Click on the left arrow icon, the right arrow is shown
     * and then click on the left arrow icon and the right arrow icon disappears.
     */
    @Test
    public void testPagination() throws Exception {
//        showWeeklyFragment();
//
//        onView(withId(R.id.leftArrow)).check(matches(isDisplayed()));
//        onView(withId(R.id.leftArrow)).perform(click());
//        onView(withId(R.id.roundedBar)).check(matches(isDisplayed()));
//        onView(withId(R.id.buttonPdfExport)).check(matches(isDisplayed()));
//        onView(withId(R.id.title)).check(matches(isDisplayed()));
//        onView(withId(R.id.rightArrow)).check(matches(isDisplayed()));
//        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
//        onView(withId(R.id.label)).check(matches(isDisplayed()));
//        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));
//
//        onView(withId(R.id.rightArrow)).perform(click());
//        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
    }

    private void showWeeklyFragment() throws Exception {
//        onView(withId(R.id.playIcon)).perform(ViewActions.click());
//        Thread.sleep(1000L);
//        onView(withId(R.id.weeklyButton)).perform(ViewActions.click());
//        Thread.sleep(500L);
    }
}
