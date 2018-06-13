package com.aptatek.aptatek.view.pin.set;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.aptatek.aptatek.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.not;

public class SetPinTest {

    @Rule
    public ActivityTestRule<SetPinHostActivity> activityRule = new ActivityTestRule<>(SetPinHostActivity.class);


    @Test
    public void testInitialView() {
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.title)).check(matches(withText(R.string.set_pin_title)));
        onView(withId(R.id.hintTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.hintTextView)).check(matches(withText(R.string.set_pin_hint)));
        onView(withId(R.id.messageTextView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.pinLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.pinLayout)).check(matches(hasChildCount(6)));
    }

    @Test
    public void testEnterValidPin() {
        // SetPin
        enterPin();
        // ConfirmPin
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.title)).check(matches(withText(R.string.confirm_pin_title)));
        onView(withId(R.id.hintTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.hintTextView)).check(matches(withText(R.string.confirm_pin_hint)));
        onView(withId(R.id.messageTextView)).check(matches(not(isDisplayed())));
        // Enter the same pin
        enterPin();
        onView(withId(R.id.messageTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.messageTextView)).check(matches(hasTextColor(R.color.applicationGreen)));

        //TODO check MainActivity
    }

    @Test
    public void testEnterInvalidPinAndCorrectIt() throws Exception {
        // SetPin
        enterPin();
        // ConfirmPin
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.title)).check(matches(withText(R.string.confirm_pin_title)));
        onView(withId(R.id.hintTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.hintTextView)).check(matches(withText(R.string.confirm_pin_hint)));
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
        onView(withId(R.id.messageTextView)).check(matches(hasTextColor(R.color.applicationRed)));
        sleep(2500);
        // SetPin Again
        enterPin();
        // Confirm
        enterPin();
        onView(withId(R.id.messageTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.messageTextView)).check(matches(withText(R.string.confirm_pin_successful)));
        onView(withId(R.id.messageTextView)).check(matches(hasTextColor(R.color.applicationGreen)));

        //TODO check MainActivity
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