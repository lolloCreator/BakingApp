package org.jkarsten.bakingapp.bakingapp.data.source.local;

import android.app.LoaderManager;

import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.source.FoodDataSource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by juankarsten on 8/28/17.
 */

public class LocalFoodDataSource implements FoodDataSource {
    public LocalFoodDataSource(LoaderManager loaderManager) {
        // TODO: 8/29/17 write sql
    }

    public List<Food> requestFood() {
        return null;
    }

    @Override
    public Observable<Food> makeFoodObservable() {
        return Observable.create(new ObservableOnSubscribe<Food>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Food> e) throws Exception {
                List<Food> foods = requestFood();
                if (foods != null) {
                    for (Food food:foods) {
                        e.onNext(food);
                    }
                } else {
                    //e.onError(new RuntimeException(UNSUCCESFUL));
                }
            }
        });
    }
}
