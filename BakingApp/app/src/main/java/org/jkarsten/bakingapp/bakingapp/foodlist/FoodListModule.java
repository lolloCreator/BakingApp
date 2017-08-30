package org.jkarsten.bakingapp.bakingapp.foodlist;

import org.jkarsten.bakingapp.bakingapp.data.source.FoodDataSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by juankarsten on 8/30/17.
 */

@Module
public class FoodListModule {
    private FoodListContract.View view;

    public FoodListModule(FoodListContract.View view) {
        this.view = view;
    }

    @Provides
    public FoodListContract.Presenter provideFoodListPresenter(FoodDataSource dataSource) {
        return new FoodListPresenter(view, dataSource);
    }
}
