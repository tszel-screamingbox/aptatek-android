package com.aptatek.pkulab.view.fingerprint;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.aptatek.pkulab.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * @test.layer View / Fingerprint
 * @test.feature FingerprintScreen
 * @test.type Instrumented unit tests
 */
public class FingerprintScreenTest {

    @Rule
    public ActivityTestRule<FingerprintActivity> activityRule = new ActivityTestRule<>(FingerprintActivity.class);


    /**
     * Testing the initial view.
     *
     * @test.expected View appears, without any error.
     */
    @Test
    public void testInitialView() {
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.fingerprint_title)));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.fingerprint_message)));
        onView(withId(R.id.enableButton)).check(matches(isDisplayed()));
        onView(withId(R.id.disableButton)).check(matches(isDisplayed()));
    }

    /**
     * Enable fingerprint authentication.
     *
     * @test.expected Finishing current activity.
     */
    @Test
    public void testEnableFingerprint() {
        onView(withId(R.id.enableButton)).perform(ViewActions.click());
        assert (activityRule.getActivity().isFinishing());
    }

    /**
     * Disable fingerprint authentication.
     *
     * @test.expected Finishing current activity.
     */
    @Test
    public void testDisableFingerprint() {
        onView(withId(R.id.enableButton)).perform(ViewActions.click());
        assert (activityRule.getActivity().isFinishing());
    }
}
