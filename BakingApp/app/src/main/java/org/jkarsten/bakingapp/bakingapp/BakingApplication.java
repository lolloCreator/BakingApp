package org.jkarsten.bakingapp.bakingapp;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import org.jkarsten.bakingapp.bakingapp.widget.BakingWidgetProvider;

/**
 * Created by juankarsten on 9/23/17.
 */

public class BakingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);
        BakingWidgetProvider.updateAppWidgets(this, appWidgetManager, appWidgetIds);
    }
}
