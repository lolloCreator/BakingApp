package org.jkarsten.bakingapp.bakingapp.foodlist;

import org.jkarsten.bakingapp.bakingapp.BasePresenter;
import org.jkarsten.bakingapp.bakingapp.BaseView;
import org.jkarsten.bakingapp.bakingapp.data.Food;

import java.util.List;

/**
 * Created by juankarsten on 8/28/17.
 */

public interface FoodListContract {
    interface Presenter extends BasePresenter {
        void viewFood(Food food);
    }

    interface View extends BaseView {
        void showNoInternet();
        void showFoods(List<Food> foods);

        void goToFoodActivity(Food food);

        void showLoading();
        void hideLoading();
    }
}
