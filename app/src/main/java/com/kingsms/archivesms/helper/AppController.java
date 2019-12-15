package com.kingsms.archivesms.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.kingsms.archivesms.dagger.DaggerApplication;
import com.kingsms.archivesms.view.HomeActivity.HomeSenderNamesActivity;

public class AppController extends Application implements Application.ActivityLifecycleCallbacks {


    private boolean activityInForeground;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);

    }



    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        //Here you can add all Activity class you need to check whether its on screen or not

        activityInForeground = activity instanceof HomeSenderNamesActivity;

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public boolean isActivityInForeground() {
        return activityInForeground;
    }
}
