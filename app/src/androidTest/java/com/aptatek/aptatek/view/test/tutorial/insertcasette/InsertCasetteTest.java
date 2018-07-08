package com.aptatek.aptatek.view.test.tutorial.insertcasette;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.view.test.TestActivity;
import com.aptatek.aptatek.view.test.TestScreens;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class InsertCasetteTest {

    @Rule
    public ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class);

    @Before
    public void setUp() throws Exception {
        activityRule.getActivity().showScreen(TestScreens.INSERT_CASSETTE);
    }

    @Test
    public void testInitialUi() throws Exception {
        onView(withId(R.id.testBaseTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.testBaseMessage)).check(matches(isDisplayed()));
        onView(withId(R.id.tutorialImage)).check(matches(isDisplayed()));
        onView(withId(R.id.testNavigationButton)).check(matches(isDisplayed()));
        onView(withId(R.id.testCancelCircleButton)).check(matches(isDisplayed()));
        onView(withId(R.id.testCancelButton)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testInitialUiValues() throws Exception {
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_insertcasette_title)));
        onView(withId(R.id.testBaseMessage)).check(matches(withText(R.string.test_insertcasette_description)));
        onView(withId(R.id.testNavigationButton)).check(matches(withText(R.string.test_button_next)));
    }

    @Test
    public void testForwardNavigation() throws Exception {
        onView(withId(R.id.testNavigationButton)).perform(click());
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_attachcube_title)));
    }

    @Test
    public void testCancel() throws Exception {
        onView(withId(R.id.testCancelCircleButton)).perform(click());
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_cancel_title)));
    }

    @Test
    public void testBackNavigation() {
        onView(withId(R.id.testNavigationButton)).perform(pressBack());
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_takesample_title)));
    }

}
