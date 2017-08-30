package org.jkarsten.bakingapp.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.source.remote.RemoteFoodDataSource;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RemoteFoodDataSource remoteFoodDataSource = new RemoteFoodDataSource(new OkHttpClient());
        Observable<Food> observable = remoteFoodDataSource.makeFoodObservable();

        Disposable disposable = observable.subscribe(new Consumer<Food>() {
            @Override
            public void accept(Food food) throws Exception {
                Log.d(MainActivity.class.getSimpleName(), food.toString());
            }
        });
    }
}
