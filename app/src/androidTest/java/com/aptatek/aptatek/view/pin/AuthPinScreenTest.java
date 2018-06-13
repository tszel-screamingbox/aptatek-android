package com.aptatek.aptatek.view.pin;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.view.pin.auth.AuthPinHostActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AuthPinScreenTest {

    @Rule
    public ActivityTestRule<AuthPinHostActivity> activityRule = new ActivityTestRule<>(AuthPinHostActivity.class);


    @Test
    public void testInitialView() {
        onView(withId(R.id.hintTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.messageTextView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.fingerpintImage)).check(matches(not(isDisplayed())));
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.pinLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.pinLayout)).check(matches(hasChildCount(6)));
    }

    @Test
    public void testEnterPin() {
        onView(withId(R.id.fingerpintImage)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button0)).perform(ViewActions.click());
        onView(withId(R.id.button1)).perform(ViewActions.click());
        onView(withId(R.id.button2)).perform(ViewActions.click());
        onView(withId(R.id.button3)).perform(ViewActions.click());
        onView(withId(R.id.button4)).perform(ViewActions.click());
        onView(withId(R.id.button5)).perform(ViewActions.click());
        onView(withId(R.id.messageTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.pinLayout)).check(matches(hasChildCount(6)));
        onView(withId(R.id.messageTextView)).check(matches(withText(R.string.require_pin_message_invalid)));
        onView(withId(R.id.messageTextView)).check(matches(hasTextColor(R.color.applicationRed)));
    }
}