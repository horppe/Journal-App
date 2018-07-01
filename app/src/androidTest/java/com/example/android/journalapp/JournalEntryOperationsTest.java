package com.example.android.journalapp;


import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class JournalEntryOperationsTest {
    private String textToType;
    private String titleTextToType;
    private String changedTitleText;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initText(){
        titleTextToType = "This is the title";
        textToType = "Hello World of Espresso :)";
        changedTitleText = "This is the Changed title Text :(";
    }

    @Test
    public void addEntryAndDisplay(){
        onView(withId(R.id.addNewEntry)).perform(click());
        onView(withId(R.id.title_text)).perform(typeText(titleTextToType)).perform(closeSoftKeyboard());
        onView(withId(R.id.editNoteView)).perform(typeText(textToType)).perform(closeSoftKeyboard());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Save")).perform(click());
    }

    @Test
    public void editEntryAndSave(){
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.edit_btn)).perform(click());
        onView(withId(R.id.title_text)).perform(typeText(changedTitleText))
                .perform(closeSoftKeyboard());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Save")).perform(click());
    }

    @Test public void showCreatedDate(){
        onView(withId(R.id.addNewEntry)).perform(click());
        closeSoftKeyboard();
        onView(withId(R.id.created_toogleButton)).perform(click()).check(matches(isDisplayed()));
        pressBack();
    }

    @Test public void showUpdatedDate(){
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        closeSoftKeyboard();
        onView(withId(R.id.updated_toogleButton)).perform(click()).check(matches(isDisplayed()));
        pressBack();
    }

}
