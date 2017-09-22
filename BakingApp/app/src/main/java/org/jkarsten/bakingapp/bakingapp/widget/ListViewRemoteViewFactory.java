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
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.Ingredient;
import org.jkarsten.bakingapp.bakingapp.foodlist.FoodListActivity;

/**
 * Created by juankarsten on 9/22/17.
 */

public class ListViewRemoteViewFactory
        extends BroadcastReceiver
        implements RemoteViewsService.RemoteViewsFactory {

    public static final String
            UPDATE_WIDGET_ACTION = "org.jkarsten.bakingapp.bakingapp.widget.ListViewRemoteViewFactory";

    private Context mContext;
    private Food mFood;

    public ListViewRemoteViewFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        Log.d(ListViewRemoteViewFactory.class.getSimpleName(), "onCreate");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_WIDGET_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(this, intentFilter);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(this);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);
        if (mFood != null && mFood.getIngredients() != null) {
            Ingredient ingredient = mFood.getIngredients().get(i);
            views.setTextViewText(R.id.ingredient_item_name_textview, ingredient.getIngredient());
        }
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        if (mFood == null || mFood.getIngredients() == null)
            return 0;
        return mFood.getIngredients().size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mFood = intent.getParcelableExtra(FoodListActivity.FOOD_ARGS);
        Log.d(ListViewRemoteViewFactory.class.getSimpleName(), "onReceive" + mFood.toString());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);
    }

    public static void sendBroadcast(Context context, Food food) {
        Intent intent = new Intent(ListViewRemoteViewFactory.UPDATE_WIDGET_ACTION);
        intent.putExtra(FoodListActivity.FOOD_ARGS, food);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}

