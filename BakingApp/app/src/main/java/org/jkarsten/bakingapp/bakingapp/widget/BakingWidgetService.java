package org.jkarsten.bakingapp.bakingapp.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by juankarsten on 9/22/17.
 */

public class BakingWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewFactory(getApplicationContext());
    }
}


