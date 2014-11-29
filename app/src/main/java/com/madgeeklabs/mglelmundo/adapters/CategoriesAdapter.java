package com.madgeeklabs.mglelmundo.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.madgeeklabs.mglelmundo.R;
import com.madgeeklabs.mglelmundo.api.MundoApi;
import com.madgeeklabs.mglelmundo.base.MundoAplication;
import com.madgeeklabs.mglelmundo.models.News;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by goofyahead on 11/28/14.
 */

public class CategoriesAdapter extends PagerAdapter {
    private static final String TAG = CategoriesAdapter.class.getName();
    private final TextToSpeech mTts;
    private ArrayList<String> contents;
    private LayoutInflater mInflater;
    @Inject
    RestAdapter restAdapter;
    private MundoAplication app;
    private Context mContext;
    private int currentPage;
    private ArrayList<NewsAdapter> listOfNewsAdapter = new ArrayList<>();

    public CategoriesAdapter (final ArrayList<String> categories, final Context mContext, final MundoAplication app) {
        this.contents = categories;
        mInflater = LayoutInflater.from(mContext);
        this.app = app;
        this.mContext = mContext;
        app.inject(this);

        MundoApi api = restAdapter.create(MundoApi.class);

        api.getTecnolgiesNews(new Callback<List<News>>() {
            @Override
            public void success(List<News> newses, Response response) {
                listOfNewsAdapter.add(new NewsAdapter((ArrayList<News>) newses, mContext, app));
//                listOfNewsFeed.add((ArrayList<News>)newses);
                notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "something failed" + error.getMessage());
            }
        });

        api.getBreakingNews(new Callback<List<News>>() {
            @Override
            public void success(List<News> newses, Response response) {
                listOfNewsAdapter.add(new NewsAdapter((ArrayList<News>) newses, mContext, app));
//                listOfNewsFeed.add((ArrayList<News>) newses);
                notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "something failed" + error.getMessage());
            }
        });

        api.getSportsNews(new Callback<List<News>>() {
            @Override
            public void success(List<News> newses, Response response) {
                listOfNewsAdapter.add(new NewsAdapter((ArrayList<News>) newses, mContext, app));
//                listOfNewsFeed.add((ArrayList<News>) newses);
                notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "something failed" + error.getMessage());
            }
        });

        mTts=new TextToSpeech( mContext, new TextToSpeech.OnInitListener() {
            @Override
            final public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            Log.d(TAG, "voice started");
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            Log.d(TAG, "voice done " + currentPage);

                            if (listOfNewsAdapter.get(currentPage).getCount() > 1) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "Reading page: " + currentPage + " current news " + ((News) listOfNewsAdapter.get(currentPage).getItem(0)).getTittle());
                                        listOfNewsAdapter.get(currentPage).removeItem(0);
                                        mTts.speak(((News) listOfNewsAdapter.get(currentPage).getItem(0)).getTittle(), TextToSpeech.QUEUE_FLUSH, null, "12345");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Log.d(TAG, "voice ERROR");
                        }
                    });
                }
            }
        });

        Locale spanish = new Locale("es", "ES");
        mTts.setLanguage(spanish);
    }


    @Override
    public int getCount() {
        return listOfNewsAdapter.size();
    }

    @Override
    public boolean isViewFromObject(View v, Object o) {
        return v == ((View) o);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = mInflater.inflate(R.layout.list_of_news, null);

        final ListView listOfNews = (ListView) v.findViewById(R.id.listView);

        if (listOfNewsAdapter.size() >= position) {
//            listOfNewsAdapter.add(position, new NewsAdapter(listOfNewsFeed.get(position), mContext, app));
            listOfNews.setAdapter(listOfNewsAdapter.get(position));
        }

        Button button = (Button) v.findViewById(R.id.speech);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTts.speak(((News) listOfNewsAdapter.get(currentPage).getItem(0)).getTittle(), TextToSpeech.QUEUE_FLUSH, null, "12345");
            }
        });

        container.addView(v, 0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}