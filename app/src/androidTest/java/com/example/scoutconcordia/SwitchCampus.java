package com.example.scoutconcordia;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.scoutconcordia.Activities.MapsActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
public class SwitchCampus {
    
    @Rule
    public ActivityTestRule<MapsActivity> mActivityTestRule = new ActivityTestRule<>(MapsActivity.class);
    
    @Rule
    public GrantPermissionRule mGrantPermissionRule =
    GrantPermissionRule.grant(
    "android.permission.ACCESS_FINE_LOCATION");
    
    @Test
    public void switchCampus() throws InterruptedException
    {
        Thread.sleep(2500);
        ViewInteraction toggleButton = onView(
        allOf(withId(R.id.toggleButton), withText("SGW"),
              childAtPosition(
              withClassName(is("android.widget.RelativeLayout")),3),
              isDisplayed()));
        toggleButton.perform(click());
        Thread.sleep(500);
        ViewInteraction toggleButton2 = onView(
        allOf(withId(R.id.toggleButton), withText("LOYOLA"),
              childAtPosition(
              withClassName(is("android.widget.RelativeLayout")),3),
              isDisplayed()));
        toggleButton2.perform(click());
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
