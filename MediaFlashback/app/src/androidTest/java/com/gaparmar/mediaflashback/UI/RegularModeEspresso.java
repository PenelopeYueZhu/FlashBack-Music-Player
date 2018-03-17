package com.gaparmar.mediaflashback.UI;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.gaparmar.mediaflashback.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegularModeEspresso {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void regularModeEspresso() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(35989);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.regular_mode_title), withText("Regular Mode"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout4),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Regular Mode")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.regular_mode_title), withText("Regular Mode"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout4),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Regular Mode")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.browse_button),
                        childAtPosition(
                                allOf(withId(R.id.root_navigation),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.root_navigation),
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1)),
                        1),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.root_navigation),
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1)),
                        2),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction imageButton4 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.root_navigation),
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1)),
                        3),
                        isDisplayed()));
        imageButton4.check(matches(isDisplayed()));

        ViewInteraction imageButton5 = onView(
                allOf(withId(R.id.toggleBtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout4),
                                        7),
                                3),
                        isDisplayed()));
        imageButton5.check(matches(isDisplayed()));

        ViewInteraction imageButton6 = onView(
                allOf(withId(R.id.previous_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout4),
                                        7),
                                0),
                        isDisplayed()));
        imageButton6.check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
