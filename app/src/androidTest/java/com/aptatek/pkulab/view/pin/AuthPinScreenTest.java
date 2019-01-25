package com.aptatek.pkulab.view.pin;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.view.pin.auth.AuthPinHostActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * @test.layer View / Pin
 * @test.feature User authentication by requesting PIN code
 * @test.type Instrumented unit tests
 */
@RunWith(AndroidJUnit4.class)
public class AuthPinScreenTest {

    @Rule
    public ActivityTestRule<AuthPinHostActivity> activityRule = new ActivityTestRule<>(AuthPinHostActivity.class);


    /**
     * Testing the initial view.
     *
     * @test.expected View appears, without any error.
     */
    @Test
    public void testInitialView() {
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(withId(R.id.messageTextView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.pinLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.pinLayout)).check(matches(hasChildCount(6)));
    }

    /**
     * Entering a 6-digit PIN code.
     *
     * @test.expected After typing 6-digit PIN code, the authentication is denied.
     */
    @Test
    public void testEnterPin() {
        onView(withId(R.id.button0)).perform(ViewActions.click());
        onView(withId(R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.button2)).perform(ViewActions.click());
        onView(withId(R.id.button0)).perform(ViewActions.click());
        onView(withId(R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.button2)).perform(ViewActions.click());
        onView(withId(R.id.messageTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.pinLayout)).check(matches(hasChildCount(6)));
        onView(withId(R.id.messageTextView)).check(matches(withText(R.string.auth_pin_message_invalid)));
        onView(withId(R.id.messageTextView)).check(matches(hasTextColor(android.R.color.white)));
    }
}