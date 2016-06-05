package com.guo.duoduo.anyshareofandroid;


import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.guo.duoduo.anyshareofandroid.utils.permission.Nammu;


/**
 * Created by 郭攀峰 on 2015/9/11.
 */
public class MyApplication extends Application {

    public static int SCREEN_WIDTH;
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        Nammu.init(getApplicationContext());
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point screen = new Point();
        display.getSize(screen);
        SCREEN_WIDTH = Math.min(screen.x, screen.y);

        initImageLoader();
    }

    private void initImageLoader() {
//        ImageLoaderConfig config = new ImageLoaderConfig()
//                .setLoadingPlaceholder(R.drawable.icon_loading)
//                .setCache(new MemoryCache())
//                .setLoadPolicy(new SerialPolicy());
//        SimpleImageLoader.getInstance().init(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
