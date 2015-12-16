package mrfu.rxface;

import android.app.Application;
import android.content.Context;

/**
 * Created by MrFu on 15/12/15.
 */
public class AppApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
