package com.madgeeklabs.mglelmundo.modules;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.madgeeklabs.mglelmundo.Activities.MainActivity;
import com.madgeeklabs.mglelmundo.Utils.Credentials;
import com.madgeeklabs.mglelmundo.adapters.CategoriesAdapter;
import com.madgeeklabs.mglelmundo.adapters.NewsAdapter;
import com.madgeeklabs.mglelmundo.api.MundoApi;
import com.madgeeklabs.mglelmundo.volley.BitmapLruCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by goofyahead on 11/28/14.
 */
@Module(injects = {
        MainActivity.class,
        NewsAdapter.class,
        CategoriesAdapter.class
},
        library = true)

public class AndroidModule {

    private final Context mContext;

        public AndroidModule(Context context) {
            this.mContext = context;
        }

        /**
         * Allow the application context to be injected but require that it be annotated with
         * {@link @Annotation} to explicitly differentiate it from an activity context.
         */
        @Provides
        @Singleton
        Context provideApplicationContext() {
            return mContext;
        }

        @Provides
        @Singleton
        RequestQueue provideQueue() {
            return Volley.newRequestQueue(mContext);
        }

        @Provides
        @Singleton
        ImageLoader provideImageLoader(RequestQueue mRequestQueue) {
            return new ImageLoader(mRequestQueue, new BitmapLruCache(50));
        }

        @Provides
        @Singleton
        RestAdapter provideRestAdapter() {
            return new RestAdapter.Builder()
                    .setEndpoint(Credentials.SERVER_URL)
                    .build();
        }

        @Provides
        @Singleton
        MundoApi provideApi() {
            return provideRestAdapter().create(MundoApi.class);
        }
}
