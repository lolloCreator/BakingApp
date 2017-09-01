package org.jkarsten.bakingapp.bakingapp.recipedetail;

import dagger.Component;

/**
 * Created by juankarsten on 9/1/17.
 */

@Component(modules = {RecipeDetailModule.class})
public interface RecipeDetailComponent {
    void inject(RecipeDetailFragment fragment);
}
