package org.jkarsten.bakingapp.bakingapp.data.source.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.source.FoodDataSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by juankarsten on 8/28/17.
 */

public class RemoteFoodDataSource implements FoodDataSource {
    private static final String UNSUCCESFUL = "unsuccessful connection";
    private OkHttpClient mHttpClient;
    private Uri API_URI = Uri.parse("http://go.udacity.com/android-baking-app-json");
    private Call mNewCall;

    private final Context mContext;
    public static final String LOCAL_PREF = "LocalFoodDataSource";
    public static final String LOCAL_PREF_FOOD = "LocalFoodDataSource_FOOD";
    public static final int NO_LOCAL_PREF_FOOD = -1;

    public RemoteFoodDataSource(OkHttpClient okHttpClient, Context context) {
        mHttpClient = okHttpClient;
        mContext = context;
    }

    private List<Food> requestFood() throws IOException {
        Request request = new Request.Builder()
                .url(API_URI.toString())
                .build();

        mNewCall = mHttpClient.newCall(request);
        Response response = mNewCall.execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException(UNSUCCESFUL);
        } else {
            String responseBody = response.body().string();
            Gson gson = new Gson();
            Food[] foods = gson.fromJson(responseBody, Food[].class);
            return Arrays.asList(foods);
        }
    }

    @Override
    public Observable<Food> makeFoodObservable() {
        return Observable.create(new ObservableOnSubscribe<Food>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Food> e) throws Exception {
                List<Food> foods = null;
                try {
                    foods = requestFood();
                } catch (Exception exc) {
                    if (!e.isDisposed())
                        e.onError(exc);
                }
                if (foods != null) {
                    for (Food food:foods) {
                        e.onNext(food);
                    }
                    Log.d(RemoteFoodDataSource.class.getSimpleName(), "onComplete");
                    e.onComplete();
                } else {
                    if (!e.isDisposed())
                        e.onError(new RuntimeException(UNSUCCESFUL));
                }
            }
        }).map(getFood())
          .doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                if (mNewCall != null && mNewCall.isExecuted()) {
                    mNewCall.cancel();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Function<Food, Food> getFood() {
        return new Function<Food, Food>() {
            @Override
            public Food apply(@NonNull Food food) throws Exception {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                int favoriteFoodId = sharedPreferences.getInt(LOCAL_PREF_FOOD, NO_LOCAL_PREF_FOOD);
                food.setFavorite(favoriteFoodId == food.getId());
                Timber.d(food.toString());
                return food;
            }
        };
    }




    @Override
    public Observable<Food> toggleFood(final Food food) {
        Timber.d("toggleFood");
        return Observable
                .just(food)
                .map(getFood())
                .map(new Function<Food, Food>() {
                    @Override
                    public Food apply(@NonNull Food food) throws Exception {
                        food.setFavorite(!food.isFavorite());
                        return food;
                    }
                })
                .map(new Function<Food, Food>() {
                    @Override
                    public Food apply(@NonNull Food food) throws Exception {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (food.isFavorite()) {
                            editor.putInt(LOCAL_PREF_FOOD, food.getId());
                        } else {
                            editor.putInt(LOCAL_PREF_FOOD, NO_LOCAL_PREF_FOOD);
                        }
                        editor.commit();
                        return food;
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
