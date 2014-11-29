package com.madgeeklabs.mglelmundo.base;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.madgeeklabs.mglelmundo.modules.AndroidModule;
import com.madgeeklabs.mglelmundo.modules.CustomModule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by goofyahead on 11/28/14.
 */
public class MundoAplication extends Application {
    private ObjectGraph graph;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        graph = ObjectGraph.create(getModules().toArray());
    }

    public static Context getAppContext() {
        return mContext;
    }

    protected List<Object> getModules() {
        return Arrays.asList(
                new AndroidModule(this),
                new CustomModule()
        );
    }

    public void inject(Object object) {
        graph.inject(object);
    }
}