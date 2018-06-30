package com.bakingapp.Utilities.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class BakeWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakeWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
