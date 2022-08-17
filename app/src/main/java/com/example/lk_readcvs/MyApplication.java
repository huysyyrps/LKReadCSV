package com.example.lk_readcvs;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

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
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);


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
