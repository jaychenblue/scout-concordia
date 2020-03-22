package com.example.scoutconcordia;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.scoutconcordia.Activities.SplashScreenActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BY87_NavigationPane {

    @Rule
    public ActivityTestRule<SplashScreenActivity> mActivityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void bY87_NavigationPane() throws InterruptedException {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.nav_shuttle), withContentDescription("Shuttle"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_bar_activity_maps),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());
        Thread.sleep(2500);

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.nav_map), withContentDescription("Map"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_bar_activity_shuttle_schedule),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());
        Thread.sleep(2500);

        ViewInteraction bottomNavigationItemView3 = onView(
                allOf(withId(R.id.nav_schedule), withContentDescription("Schedule"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_bar_activity_maps),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView3.perform(click());
        Thread.sleep(2500);

        ViewInteraction bottomNavigationItemView4 = onView(
                allOf(withId(R.id.nav_map), withContentDescription("Map"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_bar_activity_calendar),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView4.perform(click());
        Thread.sleep(2500);
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
