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

public class MainActivityScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testInitialView() {
        onView(withId(R.id.newTestButton)).check(matches(isDisplayed()));
        onView(withId(R.id.settingsButton)).check(matches(isDisplayed()));
        onView(withId(R.id.resultButton)).check(matches(isDisplayed()));
        onView(withId(R.id.titleText)).check(matches(isDisplayed()));
        onView(withId(R.id.subTitleText)).check(matches(isDisplayed()));

        onView(withId(R.id.scrollView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.playIcon)).check(matches(isDisplayed()));

        onView(withId(R.id.newTestButton)).check(matches(withText(R.string.main_button_new_test)));
        onView(withId(R.id.resultButton)).check(matches(withText(R.string.main_button_result)));
        onView(withId(R.id.titleText)).check(matches(withText(R.string.main_title_noresult)));
        onView(withId(R.id.subTitleText)).check(matches(withText(R.string.main_title_noresult_hint)));
    }

    @Test
    public void testGoToSettings() {
        onView(withId(R.id.settingsButton)).perform(ViewActions.click());
        assert (activityRule.getActivity().isFinishing());
    }

    @Test
    public void testGoToNewTest() {
        onView(withId(R.id.newTestButton)).perform(ViewActions.click());
        assert (activityRule.getActivity().isFinishing());
    }

    @Test
    public void testGoToResult() {
        onView(withId(R.id.resultButton)).perform(ViewActions.click());
        assert (activityRule.getActivity().isFinishing());
    }

    @Test
    public void testShowChart() {
        onView(withId(R.id.playIcon)).perform(ViewActions.click());
        onView(withId(R.id.playIcon)).check(matches(not(isDisplayed())));
        onView(withId(R.id.scrollView)).check(matches(isDisplayed()));
    }
}