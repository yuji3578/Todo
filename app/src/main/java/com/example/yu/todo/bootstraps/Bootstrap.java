package com.example.yu.todo.bootstraps;

import android.app.Application;
import com.beardedhen.androidbootstrap.TypefaceProvider;

public class Bootstrap extends Application{
    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
