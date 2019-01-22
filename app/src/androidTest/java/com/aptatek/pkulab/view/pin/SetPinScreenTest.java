package com.aptatek.pkulab.view.pin;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.view.pin.set.SetPinHostActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

/**
 * @test.layer View / Pin
 * @test.feature Setting user's PIN code
 * @test.type Instrumented unit tests
 */
public class SetPinScreenTest {

    @Rule
    public ActivityTestRule<SetPinHostActivity> activityRule = new ActivityTestRule<>(SetPinHostActivity.class);


    /**
     * Testing the initial view.
     *
     * @test.expected View appears, without any error.
     */
    @Test
    public void testInitialView() {
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.title)).check(matches(withText(R.string.set_pin_title)));
        onView(withId(R.id.header)).check(matches(isDisplayed()));
        onView(withId(R.id.subtitle)).check(matches(withText(R.string.set_pin_hint)));
        onView(withId(R.id.messageTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.pinLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.pinLayout)).check(matches(hasChildCount(6)));
    }

    /**
     * Valid PIN code validation.
     *
     * @test.expected The typed PIN code is valid, finishing current activity.
     */
    @Test
    public void testEnterValidPin() throws Exception {
        // SetPin
        enterPin();
        sleep(1000);
        // ConfirmPin
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.confirm_pin_title)));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.confirm_pin_hint)));
        onView(withId(R.id.messageTextView)).check(matches(not(isDisplayed())));
        // Enter the same pin
        enterPin();
        onView(withId(R.id.messageTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.messageTextView)).check(matches(hasTextColor(android.R.color.white)));
        sleep(10000);
        assertTrue(activityRule.getActivity().isFinishing());
    }

    /**
     * Invalid PIN code validation.
     *
     * @test.expected The first PIN is invalid, but second is valid. Then finishing current activity.
     */
    @Test
    public void testEnterInvalidPinAndCorrectIt() throws Exception {
        // SetPin
        enterPin();
        sleep(1000);
        // ConfirmPin View
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.confirm_pin_title)));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.confirm_pin_hint)));
        onView(withId(R.id.messageTextView)).check(matches(not(isDisplayed())));

        // Enter wrong pin
        onView(withId(R.id.button0)).perform(ViewActions.click());
        onView(withId(R.id.button0)).perform(ViewActions.click());
        onView(withId(R.id.button0)).perform(ViewActions.click());
        onView(withId(R.id.button0)).perform(ViewActions.click());
        onView(withId(R.id.button0)).perform(ViewActions.click());
        onView(withId(R.id.button0)).perform(ViewActions.click());

        onView(withId(R.id.messageTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.messageTextView)).check(matches(withText(R.string.confirm_pin_error)));
        onView(withId(R.id.messageTextView)).check(matches(hasTextColor(android.R.color.white)));
        sleep(1000);
        // SetPin Again
        enterPin();
        sleep(1000);
        // Confirm
        enterPin();

        onView(withId(R.id.messageTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.messageTextView)).check(matches(withText(R.string.confirm_pin_successful)));
        onView(withId(R.id.messageTextView)).check(matches(hasTextColor(android.R.color.white)));
        sleep(10000);
        assertTrue(activityRule.getActivity().isFinishing());
    }


    private void enterPin() {
        onView(withId(R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.button1)).perform(ViewActions.click());
    }
}