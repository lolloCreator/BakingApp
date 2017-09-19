package org.jkarsten.bakingapp.bakingapp.data.source.remote;

import android.net.Uri;
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
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by juankarsten on 8/28/17.
 */

public class RemoteFoodDataSource implements FoodDataSource {
    private static final String UNSUCCESFUL = "unsuccessful connection";
    private OkHttpClient mHttpClient;
    private Uri API_URI = Uri.parse("http://go.udacity.com/android-baking-app-json");
    private Call mNewCall;

    public RemoteFoodDataSource(OkHttpClient okHttpClient) {
        mHttpClient = okHttpClient;
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
        }).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                if (mNewCall != null && mNewCall.isExecuted()) {
                    mNewCall.cancel();
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
