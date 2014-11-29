package com.madgeeklabs.mglelmundo.api;

import com.madgeeklabs.mglelmundo.models.News;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Created by goofyahead on 11/28/14.
 */
public interface MundoApi {

    @GET("/tecnologia")
    void getTecnolgiesNews(Callback<List<News>> cb);

    @GET("/deportes")
    void getSportsNews(Callback<List<News>> cb);

    @GET("/actualidad")
    void getBreakingNews(Callback<List<News>> cb);

    @GET("/related/{theme}")
    void getRelatedNews(@Path("theme") String theme, Callback<List<News>> cb);

}
