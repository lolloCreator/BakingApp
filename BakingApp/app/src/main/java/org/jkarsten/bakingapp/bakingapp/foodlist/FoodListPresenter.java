package org.jkarsten.bakingapp.bakingapp.foodlist;

import android.content.Intent;
import android.util.Log;

import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.source.FoodDataSource;
import org.jkarsten.bakingapp.bakingapp.recipedetail.RecipeDetailActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by juankarsten on 8/29/17.
 */

public class FoodListPresenter implements FoodListContract.Presenter {
    private FoodListContract.View mView;
    private FoodDataSource mDataSource;
    private List<Food> foods;
    private CompositeDisposable mCompositeDisposable;

    public FoodListPresenter(FoodListContract.View mView, FoodDataSource dataSource) {
        this.mView = mView;
        this.mDataSource = dataSource;
    }

    @Override
    public void start() {
        foods = new ArrayList<>();

        mCompositeDisposable = new CompositeDisposable();
        Observable<Food> observable = mDataSource.makeFoodObservable();

        mView.showLoading();
        observable.observeOn(Schedulers.computation())
                .filter(new Predicate<Food>() {
                    @Override
                    public boolean test(@NonNull Food food) throws Exception {
                        return !foods.contains(food);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Food>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Food food) {
                        Log.d(FoodListPresenter.class.getSimpleName(), "onNext " + food);
                        foods.add(food);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(FoodListPresenter.class.getSimpleName(), e.toString());
                        mView.showNoInternet();
                    }

                    @Override
                    public void onComplete() {
                        mView.showFoods(foods);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void stop() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void viewFood(Food food) {
        mView.goToFoodActivity(food);
    }

}
