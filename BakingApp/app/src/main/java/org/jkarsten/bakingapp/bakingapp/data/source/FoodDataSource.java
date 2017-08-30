package org.jkarsten.bakingapp.bakingapp.data.source;

import org.jkarsten.bakingapp.bakingapp.data.Food;

import io.reactivex.Observable;


/**
 * Created by juankarsten on 8/28/17.
 */

public interface FoodDataSource {
    Observable<Food> makeFoodObservable();
}
