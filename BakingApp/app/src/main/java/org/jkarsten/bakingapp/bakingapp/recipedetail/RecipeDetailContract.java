package org.jkarsten.bakingapp.bakingapp.recipedetail;

import org.jkarsten.bakingapp.bakingapp.BasePresenter;
import org.jkarsten.bakingapp.bakingapp.BaseView;
import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.Ingredient;
import org.jkarsten.bakingapp.bakingapp.data.Step;

import java.util.List;

/**
 * Created by juankarsten on 9/1/17.
 */

public interface RecipeDetailContract {
    interface View extends BaseView {
        boolean isDualPane();
        void goToStepActivity(List<Step> steps, int position);
        void showStepDetail(Step step);
        void showIngredients(List<Ingredient> ingredients);
        void showSteps(List<Step> steps);
        void setHeaderImage(String foodImage);
        void setFoodTitle(String title);

        void updateWidget(Food mFood);

        void showImage(boolean favorite);
    }

    interface Presenter extends BasePresenter {
        void onStepSelected(int position);
        void onFoodReady(Food food);
        void onClickFavoriteButton();
    }
}
