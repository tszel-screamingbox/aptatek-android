package com.aptatek.pkuapp.view.test.samplewetting;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.view.test.TestActivity;
import com.aptatek.pkuapp.view.test.TestScreens;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Tests for the SampleWetting screen.
 *
 * @test.layer presentation
 * @test.feature SampleWetting
 * @test.type integration
 */
@RunWith(AndroidJUnit4.class)
public class SampleWettingScreenTest {

    @Rule
    public ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class);

    @Before
    public void setUp() throws Exception {
        final TestActivity activity = activityRule.getActivity();
        activity.runOnUiThread(() -> activity.showScreen(TestScreens.INSERT_SAMPLE));
        Thread.sleep(2000L);
        onView(withId(R.id.testNavigationButton)).perform(click());
    }

    /**
     * Tests the initial visibility of the ui elements.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testInitialViewsVisible() throws Exception {
        Thread.sleep(2000L);

        onView(withId(R.id.testBaseTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.testBaseMessage)).check(matches(isDisplayed()));
        onView(withId(R.id.testCancelCircleButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.testNavigationButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.testCancelButton)).check(matches(isDisplayed()));
        onView(withId(R.id.wettingCountdown)).check(matches(isDisplayed()));
        onView(withId(R.id.wettingImage)).check(matches(isDisplayed()));
    }

    /**
     * Tests the initial values of the ui elements.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testInitialUiValues() throws Exception {
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_samplewetting_title)));
        onView(withId(R.id.testBaseMessage)).check(matches(withText(R.string.test_samplewetting_description)));
    }

    /**
     * Tests whether pressing the back button displays the Cancel Test screen.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testCancelNavigatesToCancel() throws Exception {
        onView(withId(R.id.testCancelButton)).perform(click());
        onView(withId(R.id.testBaseTitle)).check(matches(withText(R.string.test_cancel_title)));
    }

}
