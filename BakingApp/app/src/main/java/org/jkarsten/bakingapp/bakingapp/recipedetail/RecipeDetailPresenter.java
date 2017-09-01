package org.jkarsten.bakingapp.bakingapp.recipedetail;

import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.Step;

import javax.inject.Inject;

/**
 * Created by juankarsten on 9/1/17.
 */

public class RecipeDetailPresenter implements RecipeDetailContract.Presenter {
    private static final String FOOD_IS_NULL = "Food can't be null";
    RecipeDetailContract.View mView;
    private Food mFood;

    public RecipeDetailPresenter(RecipeDetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void start() {
        assert mFood != null;
        mView.setFoodTitle(mFood.getName());
        mView.setHeaderImage(mFood.getImage());
        mView.showIngredients(mFood.getIngredients());
        mView.showSteps(mFood.getSteps());
    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onStepSelected(int position) {
        if (mView.isDualPane()) {
            Step step = mFood.getSteps().get(position);
            mView.showStepDetail(step);
        } else {
            mView.goToStepActivity(mFood.getSteps(), position);
        }
    }

    @Override
    public void onFoodReady(Food food) {
        assert food != null;
        assert food.getSteps() != null;
        assert food.getIngredients() != null;

        mFood = food;
    }
}
