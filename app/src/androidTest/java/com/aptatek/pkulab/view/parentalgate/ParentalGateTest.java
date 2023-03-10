package com.aptatek.pkulab.view.parentalgate;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import com.aptatek.pkulab.R;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * @test.layer View / ParentalGate
 * @test.feature ParentalGate, age verification
 * @test.type Instrumented unit tests
 */
@RunWith(AndroidJUnit4.class)
public class ParentalGateTest {

    @Rule
    public ActivityTestRule<ParentalGateActivity> activityRule = new ActivityTestRule<>(ParentalGateActivity.class);

    /**
     * Testing the initial view elements.
     *
     * @test.expected View appears, without any error.
     */
    @Test
    public void testInitialViewsVisible() {
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(withId(R.id.parentalButton)).check(matches(isDisplayed()));
        onView(withId(R.id.parentalDisclaimer)).check(matches(isDisplayed()));
        onView(withId(R.id.parentalAge)).check(matches(not(isDisplayed())));
        onView(withId(R.id.parentalBirthDate)).check(matches(not(isDisplayed())));
        onView(withId(R.id.keypad)).check(matches(not(isDisplayed())));
    }

    /**
     * Testing the initial view values.
     *
     * @test.expected View appears with the correct values.
     */
    @Test
    public void testInitialUiValues() {
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.parental_welcome_title)));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.parental_welcome_description)));
        onView(withId(R.id.parentalButton)).check(matches(withText(R.string.parental_welcome_enter_birthday)));
        onView(withId(R.id.parentalDisclaimer)).check(matches(withText(R.string.parental_welcome_age_disclaimer)));
    }

    /**
     * Passing age verification successfully.
     *
     * @test.expected The applications accepts the typed age number.
     */
    @Test
    public void
    testHappyCase() throws Exception {
        final Calendar past = eighteenYearsAgo();

        onView(withId(R.id.parentalButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(past.get(YEAR), 1, 1));
        onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click());

        onView(withId(R.id.parentalButton)).check(matches(withText(R.string.parental_welcome_how_old_are_you)));
        onView(withId(R.id.parentalBirthDate)).check(matches(isDisplayed()));
        final String date = new SimpleDateFormat("MM/dd/yyyy").format(past.getTime());
        onView(withId(R.id.parentalBirthDate)).check(matches(withText(Matchers.equalTo(date))));

        onView(withId(R.id.parentalButton)).perform(click());
        onView(withId(R.id.parentalButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.parentalAge)).check(matches(isDisplayed()));
        onView(withId(R.id.keypad)).check(matches(isDisplayed()));
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.parentalAge)).check(matches(withText("1")));
        onView(withId(R.id.button8)).perform(click());
        onView(withId(R.id.parentalAge)).check(matches(withText("18")));
        onView(withId(R.id.buttonAction)).perform(click());

        onView(withId(R.id.parentalVerificationImage)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.parental_verification_success_title)));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.parental_verification_success_message)));

        Thread.sleep(3000L);
    }

    /**
     * Failing on age verification.
     *
     * @test.expected The applications doesn't accepts the typed age number: the first is not old enough, the second doesn't match with selected one.
     */
    @Test
    public void testHappyFailureWithRetry() throws Exception {
        onView(withId(R.id.parentalButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2010, 1, 1));
        onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click());
        onView(withId(R.id.parentalButton)).check(matches(withText(R.string.parental_welcome_how_old_are_you)));
        onView(withId(R.id.parentalBirthDate)).check(matches(isDisplayed()));
        onView(withId(R.id.parentalBirthDate)).check(matches(withText(Matchers.equalTo("01/01/2010"))));

        onView(withId(R.id.parentalButton)).perform(click());
        onView(withId(R.id.parentalButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.parentalAge)).check(matches(isDisplayed()));
        onView(withId(R.id.keypad)).check(matches(isDisplayed()));
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.parentalAge)).check(matches(withText("1")));
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.parentalAge)).check(matches(withText("11")));
        onView(withId(R.id.buttonAction)).perform(click());

        onView(withId(R.id.parentalVerificationImage)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(withId(R.id.parentalVerificationButton)).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.parental_verification_failure_not_old_enough_title)));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.parental_verification_failure_not_old_enough_message)));

        Thread.sleep(1000L);

        onView(withId(R.id.parentalVerificationButton)).perform(click());

        onView(withId(R.id.parentalButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2000, 1, 1));
        onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click());
        onView(withId(R.id.parentalButton)).check(matches(withText(R.string.parental_welcome_how_old_are_you)));
        onView(withId(R.id.parentalBirthDate)).check(matches(isDisplayed()));
        onView(withId(R.id.parentalBirthDate)).check(matches(withText(Matchers.equalTo("01/01/2000"))));

        onView(withId(R.id.parentalButton)).perform(click());
        onView(withId(R.id.parentalButton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.parentalAge)).check(matches(isDisplayed()));
        onView(withId(R.id.keypad)).check(matches(isDisplayed()));
        onView(withId(R.id.button2)).perform(click());
        onView(withId(R.id.parentalAge)).check(matches(withText("2")));
        onView(withId(R.id.button4)).perform(click());
        onView(withId(R.id.parentalAge)).check(matches(withText("24")));
        onView(withId(R.id.buttonAction)).perform(click());

        onView(withId(R.id.parentalVerificationImage)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(isDisplayed()));
        onView(withId(R.id.parentalVerificationButton)).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.title), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.parental_verification_failure_age_not_match_title)));
        onView(allOf(withId(R.id.subtitle), isDescendantOfA(withId(R.id.header)))).check(matches(withText(R.string.parental_verification_failure_age_not_match_message)));
    }

    private Calendar eighteenYearsAgo() {
        final Calendar calendar = getInstance();
        calendar.add(YEAR, -18);
        calendar.set(MONTH, JANUARY);
        calendar.set(DAY_OF_MONTH, 1);
        return calendar;
    }
}
