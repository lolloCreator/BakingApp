package org.jkarsten.bakingapp.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.picasso.Picasso;

import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.Ingredient;
import org.jkarsten.bakingapp.bakingapp.data.source.FoodDataSource;
import org.jkarsten.bakingapp.bakingapp.data.source.remote.RemoteFoodDataSource;
import org.jkarsten.bakingapp.bakingapp.foodlist.FoodListActivity;
import org.jkarsten.bakingapp.bakingapp.foodlist.ui.FoodImageUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import okhttp3.OkHttpClient;

/**
 * Created by juankarsten on 9/22/17.
 */

public class ListViewRemoteViewFactory
        implements RemoteViewsService.RemoteViewsFactory {

    public static final String
            UPDATE_WIDGET_ACTION = "org.jkarsten.bakingapp.bakingapp.widget.ListViewRemoteViewFactory";

    private Context mContext;
    private Food mFood;
    private FoodDataSource mFoodDataSource;
    CompositeDisposable mDisposable;

    public ListViewRemoteViewFactory(Context context) {
        mContext = context;
        mFoodDataSource = new RemoteFoodDataSource(new OkHttpClient(),context);
    }

    @Override
    public void onCreate() {
        Log.d(ListViewRemoteViewFactory.class.getSimpleName(), "onCreate");

        mDisposable = new CompositeDisposable();
    }

    @Override
    public void onDataSetChanged() {
        Log.d(ListViewRemoteViewFactory.class.getSimpleName(), "onDataSetChanged");
        Disposable disposable = mFoodDataSource
                .makeFoodObservable()
                .filter(new Predicate<Food>() {
                    @Override
                    public boolean test(@NonNull Food food) throws Exception {
                            return food.isFavorite();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Food>() {
                    @Override
                    public void accept(Food food) throws Exception {
                            if (food.isFavorite()) {
                                mFood = food;
                            }
                    }
                });
        mDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        mDisposable.dispose();
    }

    @Override
    public int getCount() {
        if (mFood == null || mFood.getIngredients() == null)
            return 0;
        return mFood.getIngredients().size() + 1;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = null;
        views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);
        if (i == 0) {
            views.setTextViewText(R.id.ingredient_item_name_textview, mFood.getName());
            views.setTextViewTextSize(R.id.ingredient_item_name_textview,
                    TypedValue.COMPLEX_UNIT_SP, 24);
        } else {
            if (mFood != null && mFood.getIngredients() != null && i-1 < mFood.getIngredients().size()) {
                Ingredient ingredient = mFood.getIngredients().get(i-1);
                views.setTextViewText(R.id.ingredient_item_name_textview, ingredient.getIngredient());
            }
        }
        views.setViewVisibility(R.id.bottom_border, View.GONE);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}

