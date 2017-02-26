package com.androidtitan.culturedapp.main.newsfeed.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.RecyclerViewMatcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

/**
 * Created by amohnacs on 9/8/16.
 */

@RunWith(AndroidJUnit4.class)
public class NewsFeedActivityTest {
    private final String TAG = getClass().getSimpleName();

    @Rule
    public ActivityTestRule<NewsFeedActivity> mActivityRule =
            new ActivityTestRule<>(NewsFeedActivity.class);


    @Before
    public void setUp() {

    }

    @Test
    public void testLoadingTextIsDisplayed() {

        onView(withId(R.id.culturedTitleTextView)).check(matches(withText("Cultured is loading ...")));
    }

    @Test
    public void testNewsAdapterOnboarding() {

        onView(withRecyclerView(R.id.newsList).atPosition(0))
                .check(matches(hasDescendant(withText(R.string.onboarding_title))))
                .check(matches(hasDescendant(withText(R.string.onboarding_body))))
                .check(matches(hasDescendant(withText(R.string.onboarding_gotit))));

    }

    @Test
    public void testNewsAdapterItem() {

        onView(withId(R.id.newsList)).perform(scrollToPosition(2));

        onView(withRecyclerView(R.id.newsList).atPosition(2))
                .check(matches(hasDescendant(withId(R.id.titleTextView))))
                .check(matches(hasDescendant(withId(R.id.abstractTextView))));

    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

}