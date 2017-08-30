package org.jkarsten.bakingapp.bakingapp.foodlist;

import org.jkarsten.bakingapp.bakingapp.data.FoodDataModule;

import dagger.Component;

/**
 * Created by juankarsten on 8/30/17.
 */
@Component(modules = {FoodDataModule.class, FoodListModule.class})
public interface FoodListComponent {
    void inject(FoodListActivity activity);
}
