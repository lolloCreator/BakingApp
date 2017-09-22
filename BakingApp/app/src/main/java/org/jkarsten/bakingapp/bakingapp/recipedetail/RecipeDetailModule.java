package org.jkarsten.bakingapp.bakingapp.recipedetail;

import org.jkarsten.bakingapp.bakingapp.data.source.FoodDataSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by juankarsten on 9/1/17.
 */
@Module
public class RecipeDetailModule {
    RecipeDetailContract.View mView;

    public RecipeDetailModule(RecipeDetailContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public RecipeDetailContract.Presenter providePresenter(FoodDataSource foodDataSource) {
        return new RecipeDetailPresenter(mView, foodDataSource);
    }
}
