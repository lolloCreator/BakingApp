package org.jkarsten.bakingapp.bakingapp.recipedetail;

import org.jkarsten.bakingapp.bakingapp.data.FoodDataModule;

import dagger.Component;

/**
 * Created by juankarsten on 9/1/17.
 */

@Component(modules = {RecipeDetailModule.class, FoodDataModule.class})
public interface RecipeDetailComponent {
    void inject(RecipeDetailFragment fragment);
}
