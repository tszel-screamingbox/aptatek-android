package com.aptatek.aptatek.view.test.samplewetting;

import android.support.test.espresso.action.ViewActions;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class SampleWettingScreenTest {

    @Rule
    public ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class);

    @Before
    public void setUp() throws Exception {
        activityRule.getActivity().showScreen(TestScreens.SAMPLE_WETTING);
    }

    @Test
    public void testInitialViewsVisible() throws Exception {
        onView(withId(R.id.testBaseTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.testBaseMessage)).check(matches(isDisplayed()));
        onView(withId(R.id.testCancelCircleButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.testNavigationButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.testCancelButton)).check(matches(isDisplayed()));
        onView(withId(R.id.wettingCountdown)).check(matches(isDisplayed()));
        onView(withId(R.id.wettingImage)).check(matches(isDisplayed()));
    }

    @Test
    public void testInitialUiValues() throws Exception {
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_samplewetting_title)));
        onView(withId(R.id.testBaseMessage)).check(matches(withText(R.string.test_samplewetting_description)));
    }

    @Test
    public void testCancelNavigatesToCancel() throws Exception {
        onView(withId(R.id.testCancelButton)).perform(ViewActions.click());
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_cancel_title)));
    }

}
