package com.huruwo.mapview.mapview;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2018\1\21 0021.
 */

public class AppAplication extends Application{
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
