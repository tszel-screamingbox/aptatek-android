package com.aptatek.pkulab.view.main;

import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.view.connect.onboarding.ConnectOnboardingReaderActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * @test.layer View / Main
 * @test.feature ConnectOnboardingReaderActivity, MainHostActivity, BubbleChart, Weekly Chart
 * @test.type Instrumented unit tests
 */
public class MainActivityScreenTest {

    @Rule
    public ActivityTestRule<ConnectOnboardingReaderActivity> activityRule = new ActivityTestRule<>(ConnectOnboardingReaderActivity.class);

    private void navigateToMainScreen() throws InterruptedException {
        onView(withId(R.id.turnReaderOnSkip)).check(matches(isDisplayed()));
        onView(withId(R.id.turnReaderOnSkip)).perform(click());

        Thread.sleep(2000L);
    }

    /**
     * Testing the initial view elements.
     *
     * @test.expected View appears, without any error.
     */
    @Test
    public void testInitialView() throws Exception {
        navigateToMainScreen();

        onView(withId(R.id.newTestButton)).check(matches(isDisplayed()));
        onView(withId(R.id.settingsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.bigSettingsButton)).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));

        onView(withId(R.id.scrollView)).check(matches(isDisplayed()));

        onView(withId(R.id.newTestButton)).check(matches(withText(R.string.main_button_new_test)));
    }

    /**
     * Clicking on New Test button.
     *
     * @test.expected After clicking on the button, the activity is changed to the new.
     */
    @Test
    public void testGoToNewTest() throws Exception {
        navigateToMainScreen();

        onView(withId(R.id.playIcon)).check(matches(not(isDisplayed())));
        onView(withId(R.id.newTestButton)).check(matches(isDisplayed()));
        onView(withId(R.id.newTestButton)).perform(ViewActions.click());
        assert (activityRule.getActivity().isFinishing());
    }

    /**
     * Showing bubble chart.
     *
     * @test.expected After clicking on Play icon, the chart is shown.
     */
    @Test
    public void testShowChart() throws Exception {
        navigateToMainScreen();

        onView(withId(R.id.playIcon)).check(matches(not(isDisplayed())));
        onView(withId(R.id.scrollView)).check(matches(isDisplayed()));
    }


    /**
     * Testing the initial WeeklyResult view.
     *
     * @test.expected View appears, without any error.
     */
    @Test
    public void testInitialWeeklyResult() throws Exception {
        navigateToMainScreen();

        showWeeklyFragment();

        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(withId(R.id.roundedBar)).check(matches(isDisplayed()));
        onView(withId(R.id.leftArrow)).check(matches(isDisplayed()));
        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
        onView(withId(R.id.label)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonPdfExport)).check(matches(isDisplayed()));
    }

    /**
     * Testing WeeklyResult fragment's play icon clicking.
     *
     * @test.expected After clicking on play icon, it disappears.
     */
    @Test
    public void testWeeklyPlayIconClicked() throws Exception {
        navigateToMainScreen();

        showWeeklyFragment();

        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(withId(R.id.roundedBar)).check(matches(isDisplayed()));
        onView(withId(R.id.leftArrow)).check(matches(isDisplayed()));
        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
        onView(withId(R.id.label)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonPdfExport)).check(matches(isDisplayed()));

        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
    }


    /**
     * Testing WeeklyResult fragment's pagination by swiping.
     *
     * @test.expected Swiping right, the right arrow is shown, then swipe left and it disappears.
     */
    @Test
    public void testSwipe() throws Exception {
        navigateToMainScreen();

        showWeeklyFragment();

        onView(withId(R.id.leftArrow)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).perform(swipeRight());
        onView(withId(R.id.roundedBar)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonPdfExport)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(withId(R.id.rightArrow)).check(matches(isDisplayed()));
        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
        onView(withId(R.id.label)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));

        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
    }

    /**
     * Testing pagination by clicking on the arrows icons.
     *
     * @test.expected Click on the left arrow icon, the right arrow is shown
     * and then click on the left arrow icon and the right arrow icon disappears.
     */
    @Test
    public void testPagination() throws Exception {
        navigateToMainScreen();

        showWeeklyFragment();

        onView(withId(R.id.leftArrow)).check(matches(isDisplayed()));
        onView(withId(R.id.leftArrow)).perform(click());
        onView(withId(R.id.roundedBar)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonPdfExport)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(withId(R.id.rightArrow)).check(matches(isDisplayed()));
        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
        onView(withId(R.id.label)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));

        onView(withId(R.id.rightArrow)).perform(click());
        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
    }

    private void showWeeklyFragment() throws Exception {
        Thread.sleep(2000L);
        onView(withId(R.id.weeklyButton)).perform(ViewActions.click());
        Thread.sleep(2000L);
    }
}
