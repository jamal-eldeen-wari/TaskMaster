package com.example.taskmaster;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
@RunWith(AndroidJUnit4.class)
public class EspressoTesting {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test public void testingAddTask(){
        onView(withId(R.id.addTask)).perform(click());
        onView((withId(R.id.editTextTextPersonName))).perform(typeText("Buy PS5"),closeSoftKeyboard());
        onView(withId(R.id.editTextTextPersonName2)).perform(typeText("locate a place that sells it in a reasonable price "),closeSoftKeyboard());
        onView(withId(R.id.editTextTextPersonName4)).perform(typeText("In Progress"),closeSoftKeyboard());
        onView(withId(R.id.button3)).perform(click());
        onView(withText("Buy PS5")).check(matches(isDisplayed()));
    }

    @Test public void testingRecyclerView(){
        onView(withId(R.id.taskRecyclerView)).perform(click());
        onView(withId(R.id.titleTask)).check(matches(isDisplayed()));
        onView(withId(R.id.bodyTask)).check(matches(isDisplayed()));
        onView(withId(R.id.stateTask)).check(matches(isDisplayed()));
    }

    @Test public void testingSettingsPage(){
        onView(withId(R.id.settings)).perform(click());
        onView(withId(R.id.editTextTextPersonName3)).perform(typeText("Jamal Wari"),closeSoftKeyboard());
        onView(withId(R.id.save)).perform(click());
        onView(withId(R.id.userNameView)).check(matches(withText("Welcome: Jamal Wari")));
    }
}
