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
        BakingWidgetProvider.notifyDataSetChanged(this);
    }
}
