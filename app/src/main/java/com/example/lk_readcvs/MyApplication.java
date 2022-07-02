package com.example.lk_readcvs;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

/**
 * @author: Allen.
 * @date: 2018/7/25
 * @description: application
 */

public class MyApplication extends Application {
    public static MyApplication myApp;
    public static final int TIMEOUT = 60;
    private static Context context;//全局上下文
    public static NotificationManager notificationChannelManager;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationChannelManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myApp = this;
        context = getApplicationContext();
    }
    //获取全局的上下文
    public static Context getContext() {
        return context;
    }

    public static synchronized MyApplication getInstance() {
        if (null == myApp) {
            myApp = new MyApplication();
        }
        return myApp;
    }
}
