package com.team41.boromi;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class ChangeTabUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testOwnerTabsSwitch() {
        onView(withId(R.id.tab_owned_books)).perform(click());

        // Click Owner Available
        onView(withId(R.id.tabs_sub_owned_available)).perform(click());
        onView(withId(R.layout.available)).check(matches(isDisplayed()));


        // Click Owner Requests
        onView(withId(R.id.tabs_sub_owned_requests)).perform(click());
        onView(withId(R.layout.reqom)).check(matches(isDisplayed()));

        // Click Owner Accepted
        onView(withId(R.id.tabs_sub_owned_accepted)).perform(click());
        onView(withId(R.layout.accepted)).check(matches(isDisplayed()));

        // Click Owner Lent
        onView(withId(R.id.tabs_sub_owned_lent)).perform(click());
        onView(withId(R.layout.lent)).check(matches(isDisplayed()));
    }

    @Test
    public void testBorrowedTabsSwitch() {
        onView(withId(R.id.tab_borrowed_books)).perform(click());

        // Click Borrower Borrowed Tab
        onView(withId(R.id.tabs_sub_borrowed_borrowed)).perform(click());
        onView(withId(R.layout.borrowing)).check(matches(isDisplayed()));

        // Click Borrrower Requested Tab
        onView(withId(R.id.tabs_sub_borrowed_requested)).perform(click());
        onView(withId(R.layout.reqbm)).check(matches(isDisplayed()));


        // Click Borrrower Accepted Tab
        onView(withId(R.id.tabs_sub_borrowed_accepted)).perform(click());
        onView(withId(R.layout.accepted_request)).check(matches(isDisplayed()));

    }


    @Test
    public void testBookActivityTabsSwitch() {
        // Check to see if tabs click works
        onView(withId(R.id.tab_owned_books)).perform(click());
        onView(withId(R.layout.fragment_owned)).check(matches(isDisplayed()));

        onView(withId(R.id.tab_borrowed_books)).perform(click());
        onView(withId(R.layout.fragment_borrowed)).check(matches(isDisplayed()));

        onView(withId(R.id.tab_search)).perform(click());
        onView(withId(R.layout.fragment_search)).check(matches(isDisplayed()));

        onView(withId(R.id.tab_location)).perform(click());
        onView(withId(R.layout.fragment_map)).check(matches(isDisplayed()));

        onView(withId(R.id.tab_settings)).perform(click());
        onView(withId(R.layout.fragment_settings)).check(matches(isDisplayed()));
    }


    @Test
    public void testAddFragmentActivity(){
        // Checks to see if the add fragment works
        onView(withId(R.id.tab_owned_books)).perform(click());
        onView(withId(R.layout.fragment_owned)).check(matches(isDisplayed()));

        onView(withId(R.id.toolbar_add)).perform(click());
        onView(withId(R.layout.add_book_fragment)).check(matches(isDisplayed()));

    }

    @Test
    public void testEditFragmentActivity(){
        // Checks to see the edit fragment works
        onView(withId(R.id.tab_owned_books)).perform(click());
        onView(withId(R.layout.fragment_owned)).check(matches(isDisplayed()));

        onView(withId(R.id.toolbar_add)).perform(click());
        onView(withId(R.layout.add_book_fragment)).check(matches(isDisplayed()));

        onView(withId(R.id.add_book_author)).perform(typeText("Book Author"), closeSoftKeyboard());
        onView(withId(R.id.add_book_title)).perform(typeText("Book Title"), closeSoftKeyboard());
        onView(withId(R.id.add_book_isbn)).perform(typeText("1234567891"), closeSoftKeyboard());

        onView(withId(R.id.add_book_add_button)).perform(click());

        onView(withId(R.id.right_button));
        onView(withId(R.id.edit_book)).perform(click());

        onView(withId(R.layout.edit_book_fragment)).check((matches(isDisplayed())));

        onView(withId(R.id.tabs_sub_owned_available)).perform(click());
        onView(withId(R.layout.available)).check(matches(isDisplayed()));

    }
}
