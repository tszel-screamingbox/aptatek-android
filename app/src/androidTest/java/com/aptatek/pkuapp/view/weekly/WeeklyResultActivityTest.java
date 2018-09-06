package com.aptatek.pkuapp.view.weekly;

import android.support.test.rule.ActivityTestRule;

import com.aptatek.pkuapp.R;

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
 * @test.layer View / Weekly
 * @test.feature Weekly Chart
 * @test.type Instrumented unit tests
 */
public class WeeklyResultActivityTest {

    @Rule
    public ActivityTestRule<WeeklyResultActivity> activityRule = new ActivityTestRule<>(WeeklyResultActivity.class);


    /**
     * Testing the initial view.
     *
     * @test.expected View appears, without any error.
     */
    @Test
    public void testInitialView() {
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.leftArrow)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
        onView(withId(R.id.emptyTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.emptySubtitle)).check(matches(isDisplayed()));
        onView(withId(R.id.label)).check(matches(isDisplayed()));
        onView(withId(R.id.playIcon)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));

        onView(withId(R.id.emptyTitle)).check(matches(withText(R.string.weekly_empty_title)));
        onView(withId(R.id.emptySubtitle)).check(matches(withText(R.string.weekly_empty_subtitle)));
    }

    /**
     * Testing play icon clicking.
     *
     * @test.expected After clicking on play icon, it disappears.
     */
    @Test
    public void testPlayIconClicked() {
        onView(withId(R.id.playIcon)).perform(click());

        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.leftArrow)).check(matches(isDisplayed()));
        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
        onView(withId(R.id.label)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));

        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
        onView(withId(R.id.emptyTitle)).check(matches(not(isDisplayed())));
        onView(withId(R.id.emptySubtitle)).check(matches(not(isDisplayed())));
        onView(withId(R.id.playIcon)).check(matches(not(isDisplayed())));
    }

    /**
     * Testing pagination by clicking on the arrows icons.
     *
     * @test.expected Click on the left arrow icon, the right arrow is shown
     * and then click on the left arrow icon and the right arrow icon disappears.
     */
    @Test
    public void testPagination() {
        onView(withId(R.id.playIcon)).perform(click());
        onView(withId(R.id.leftArrow)).check(matches(isDisplayed()));
        onView(withId(R.id.leftArrow)).perform(click());

        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.rightArrow)).check(matches(isDisplayed()));
        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
        onView(withId(R.id.label)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));

        onView(withId(R.id.emptyTitle)).check(matches(not(isDisplayed())));
        onView(withId(R.id.emptySubtitle)).check(matches(not(isDisplayed())));
        onView(withId(R.id.playIcon)).check(matches(not(isDisplayed())));

        onView(withId(R.id.rightArrow)).perform(click());
        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
    }

    /**
     * Testing pagination by swiping.
     *
     * @test.expected Swiping right, the right arrow is shown, then swipe left and it disappears.
     */
    @Test
    public void testSwipe() {
        onView(withId(R.id.playIcon)).perform(click());
        onView(withId(R.id.leftArrow)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).perform(swipeRight());

        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.rightArrow)).check(matches(isDisplayed()));
        onView(withId(R.id.dateText)).check(matches(isDisplayed()));
        onView(withId(R.id.label)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));

        onView(withId(R.id.emptyTitle)).check(matches(not(isDisplayed())));
        onView(withId(R.id.emptySubtitle)).check(matches(not(isDisplayed())));
        onView(withId(R.id.playIcon)).check(matches(not(isDisplayed())));

        onView(withId(R.id.viewpager)).perform(swipeLeft());
        onView(withId(R.id.rightArrow)).check(matches(not(isDisplayed())));
    }
}