package com.aptatek.pkuapp.view.weekly;

import android.support.test.rule.ActivityTestRule;

import com.aptatek.pkuapp.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class WeeklyResultActivityTest {

    @Rule
    public ActivityTestRule<WeeklyResultActivity> activityRule = new ActivityTestRule<>(WeeklyResultActivity.class);


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
}