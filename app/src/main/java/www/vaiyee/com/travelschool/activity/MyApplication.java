package www.vaiyee.com.travelschool.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2018/10/5.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getQuanjuContext()
    {
        return context;  //获取全局context
    }
}
