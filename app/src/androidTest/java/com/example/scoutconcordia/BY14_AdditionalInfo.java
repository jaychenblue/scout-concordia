package com.example.scoutconcordia;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.scoutconcordia.Activities.SplashScreenActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
public class BY14_AdditionalInfo {
    
    @Rule
    public ActivityTestRule<SplashScreenActivity> mActivityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);
    
    @Rule
    public GrantPermissionRule mGrantPermissionRule =
    GrantPermissionRule.grant(
    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void bY14_AdditionalInfo() throws InterruptedException
    {
        Thread.sleep(2500);
        try
        {
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject marker = device.findObject(new UiSelector().descriptionContains("ER Building"));
            marker.click();
        }
        catch (Throwable t)
        {
            Log.println(Log.WARN, "MessedUpTest", "The Marker could not be found");
        }
        Thread.sleep(500);
        ViewInteraction button = onView(
        allOf(withId(R.id.directionsButton), withText("Get Directions"),
              childAtPosition(
              childAtPosition(
              withClassName(is("android.widget.RelativeLayout")),
              6),
              0),
              isDisplayed()));
        button.perform(click());
        Thread.sleep(2500);
        
        ViewInteraction button5 = onView(
        allOf(withId(R.id.exploreInsideButton), withText("Explore Inside"),
              childAtPosition(
              childAtPosition(
              withClassName(is("android.widget.RelativeLayout")),
              6),
              1),
              isDisplayed()));
        button5.perform(click());
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
