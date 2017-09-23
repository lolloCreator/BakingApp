package org.jkarsten.bakingapp.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.jkarsten.bakingapp.bakingapp.foodlist.FoodListActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by juankarsten on 9/23/17.
 */
@RunWith(AndroidJUnit4.class)
public class FoodListActivityTest {
    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<FoodListActivity> foodListActivityActivityTestRule =
            new ActivityTestRule<FoodListActivity>(FoodListActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    idlingResource = FoodListActivity.getSimpleIdlingResource();
                    Espresso.registerIdlingResources(idlingResource);
                }
            };

    @Test
    public void startActivity_showRecipe() {
        Espresso.onView(ViewMatchers.withId(R.id.food_list_recyclerview));
        Espresso.onView(ViewMatchers.withId(R.id.food_list_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, ViewActions.click()));
        Espresso.onView(ViewMatchers.withId(R.id.recipe_detail_fragment))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @After
    public void unregisterIdlingProcess() {
        if (idlingResource != null)
            Espresso.unregisterIdlingResources(idlingResource);
    }
}
