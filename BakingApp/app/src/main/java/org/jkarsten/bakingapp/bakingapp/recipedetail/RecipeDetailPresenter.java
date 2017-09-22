package org.jkarsten.bakingapp.bakingapp.recipedetail;

import android.provider.Settings;
import android.util.Log;

import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.Step;
import org.jkarsten.bakingapp.bakingapp.data.source.FoodDataSource;
import org.jkarsten.bakingapp.bakingapp.widget.ListViewRemoteViewFactory;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by juankarsten on 9/1/17.
 */

public class RecipeDetailPresenter implements RecipeDetailContract.Presenter {
    private static final String FOOD_IS_NULL = "Food can't be null";
    private final FoodDataSource mFooDataSource;
    RecipeDetailContract.View mView;
    private Food mFood;

    public RecipeDetailPresenter(RecipeDetailContract.View mView, FoodDataSource foodDataSource) {
        this.mView = mView;
        mFooDataSource = foodDataSource;
    }

    @Override
    public void start() {
        assert mFood != null;
        mView.setFoodTitle(mFood.getName());
        mView.setHeaderImage(mFood.getImage());
        mView.showIngredients(mFood.getIngredients());
        mView.showSteps(mFood.getSteps());

        Observable.just(mFood).map(mFooDataSource.getFood())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Food>() {
            @Override
            public void accept(Food food) throws Exception {
                        mFood = food;
                        mView.showImage(food.isFavorite());
                        mView.updateWidget(food);
                    }
                });
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

    @Override
    public void onClickFavoriteButton() {
        Timber.d("onClickFavoriteButton");
        mFooDataSource.toggleFood(mFood)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Food>() {
                    @Override
                    public void accept(Food food) throws Exception {
                        Timber.d(food.toString());
                        mView.showImage(food.isFavorite());
                        mView.updateWidget(food);
                    }
                });
    }
}
