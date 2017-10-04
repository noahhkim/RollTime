package com.noahkim.rolltime.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Noah on 9/26/2017.
 */

public class RTWidgetViewService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RTWidgetViewFactory(this.getApplicationContext(), intent);
    }
}
