package org.jkarsten.bakingapp.bakingapp.data.source;

import org.jkarsten.bakingapp.bakingapp.data.Food;

import io.reactivex.Observable;
import io.reactivex.functions.Function;


/**
 * Created by juankarsten on 8/28/17.
 */

public interface FoodDataSource {
    Observable<Food> makeFoodObservable();
    Observable<Food> toggleFood(Food food);
    Function<Food, Food> getFood();
}
