package com.aptatek.pkulab.view.fingerprint;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.aptatek.pkulab.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        onView(withId(R.id.fingerpintImage)).check(matches(isDisplayed()));
        onView(withId(R.id.fingerprintTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.fingerprintMessage)).check(matches(isDisplayed()));
        onView(withId(R.id.enableButton)).check(matches(isDisplayed()));
        onView(withId(R.id.disableButton)).check(matches(isDisplayed()));

        onView(withId(R.id.fingerprintTitle)).check(matches(withText(R.string.fingerprint_title)));
        onView(withId(R.id.fingerprintMessage)).check(matches(withText(R.string.fingerprint_message)));
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
