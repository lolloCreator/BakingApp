package org.jkarsten.bakingapp.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.Ingredient;
import org.jkarsten.bakingapp.bakingapp.data.Step;
import org.jkarsten.bakingapp.bakingapp.data.source.remote.RemoteFoodDataSource;
import org.jkarsten.bakingapp.bakingapp.foodlist.FoodListActivity;
import org.jkarsten.bakingapp.bakingapp.idlingResource.SimpleIdlingResource;
import org.jkarsten.bakingapp.bakingapp.recipedetail.RecipeDetailActivity;
import org.jkarsten.bakingapp.bakingapp.recipedetail.RecipeDetailFragment;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by juankarsten on 9/23/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {
    Food food = new Food();

    @Rule
    public ActivityTestRule<RecipeDetailActivity> recipeDetailActivityActivityTestRule =
            new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {

                public Step[] getSteps() {
                    Step[] steps = new Step[2];
                    steps[0] = new Step();
                    steps[0].setId(0);
                    steps[0].setDescription("Step 0");
                    steps[0].setShortDescription("short step 0");
                    steps[0].setVideoURL("video0");

                    steps[1] = new Step();
                    steps[1].setId(1);
                    steps[1].setDescription("Step 1");
                    steps[1].setShortDescription("short step 1");
                    steps[1].setVideoURL("video1");

                    return steps;
                }

                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    RecipeDetailFragment.getSimpleIdlingResource();
                }

                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, RecipeDetailActivity.class);

                    food.setId(123);
                    food.setFavorite(false);
                    food.setImage("https://media-cdn.tripadvisor.com/media/photo-s/0a/8c/16/21/best-brownies-from-amsterdam.jpg");
                    food.setIngredients(new ArrayList<Ingredient>());
                    food.setName("Brownies");
                    food.setServings("3 servings");
                    food.setSteps(new ArrayList<Step>(Arrays.asList(getSteps())));

                    result.putExtra(FoodListActivity.FOOD_ARGS, food);
                    return result;
                }
            };

    @Before
    public void registerIdlingRes() {
        SimpleIdlingResource simpleIdlingResource = RecipeDetailFragment.getSimpleIdlingResource();
        Espresso.registerIdlingResources(simpleIdlingResource);

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(recipeDetailActivityActivityTestRule.getActivity());

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(RemoteFoodDataSource.LOCAL_PREF_FOOD,-1);
        editor.commit();
    }

    @After
    public void unregisterIdlingResource() {
        SimpleIdlingResource simpleIdlingResource = RecipeDetailFragment.getSimpleIdlingResource();
        Espresso.unregisterIdlingResources(simpleIdlingResource);
    }

    @Test
    public void clickFavoriteButton_saveInSharedPreferences() {
        Espresso.onView(ViewMatchers.withId(R.id.recipe_detail_fragment));
        Espresso.onView(ViewMatchers.withId(R.id.favorite_toggle_button))
                .perform(ViewActions.click());
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(recipeDetailActivityActivityTestRule.getActivity());
        Assert.assertEquals(food.getId(),
                preferences.getInt(RemoteFoodDataSource.LOCAL_PREF_FOOD,-1));
    }

    @Test
    public void clickStep_ShowStepDetailActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.steps_recyclerview));
        Espresso.onView(ViewMatchers.withId(R.id.steps_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        Espresso.onView(ViewMatchers.withId(R.id.description_textview))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.description_textview))
                .check(ViewAssertions.matches(ViewMatchers.withText("Step 0")));
    }
}


