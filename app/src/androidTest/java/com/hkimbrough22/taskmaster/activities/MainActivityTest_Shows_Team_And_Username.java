package com.hkimbrough22.taskmaster.activities;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.hkimbrough22.taskmaster.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest_Shows_Team_And_Username {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest_Shows_Team_And_Username() {
        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.userSettingsImageView),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.userSettingsTeamSpinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        materialTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.userSettingsUsernameEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.userSettingsUsernameEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.userSettingsUsernameEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.userSettingsUsernameEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("Haustin"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.userSettingsSaveButton), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.homepageTeamNameTextView), withText("Team 1"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("Team 1")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.homepageTitleTextView), withText("Haustin's Tasks"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(withText("Haustin's Tasks")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.taskFragmentTextView), withText("Task {id=f289c2e4-7f98-40fb-a443-c7b3077a8f97, title=testTitle1, body=testBody1, state=testState1, team=Team {id=3f9e64be-6d94-47cb-8f04-dc434470b572, name=Team 1, createdAt=Temporal.DateTime{offsetDateTime='2021-11-03T23:14:59.415Z'}, updatedAt=Temporal.DateTime{offsetDateTime='2021-11-03T23:14:59.415Z'}}, createdAt=Temporal.DateTime{offsetDateTime='2021-11-03T23:15:10.982Z'}, updatedAt=Temporal.DateTime{offsetDateTime='2021-11-03T23:15:10.982Z'}}"),
                        withParent(allOf(withId(R.id.frameLayout),
                                withParent(withId(R.id.taskListRecyclerView)))),
                        isDisplayed()));
        textView3.check(matches(withText("Task {id=f289c2e4-7f98-40fb-a443-c7b3077a8f97, title=testTitle1, body=testBody1, state=testState1, team=Team {id=3f9e64be-6d94-47cb-8f04-dc434470b572, name=Team 1, createdAt=Temporal.DateTime{offsetDateTime='2021-11-03T23:14:59.415Z'}, updatedAt=Temporal.DateTime{offsetDateTime='2021-11-03T23:14:59.415Z'}}, createdAt=Temporal.DateTime{offsetDateTime='2021-11-03T23:15:10.982Z'}, updatedAt=Temporal.DateTime{offsetDateTime='2021-11-03T23:15:10.982Z'}}")));
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
