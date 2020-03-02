package edu.stanford.cs108.bunnyworld;

import android.app.Application;
import android.content.Context;

public class BunnyWorldApplication extends Application {

    private static Context context;

    public static Context getGlobalContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }
}
