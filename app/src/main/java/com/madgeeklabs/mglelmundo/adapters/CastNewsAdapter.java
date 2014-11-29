package com.madgeeklabs.mglelmundo.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.madgeeklabs.mglelmundo.Interfaces.PageChanged;
import com.madgeeklabs.mglelmundo.R;
import com.madgeeklabs.mglelmundo.api.MundoApi;
import com.madgeeklabs.mglelmundo.base.MundoAplication;
import com.madgeeklabs.mglelmundo.models.News;

import java.util.ArrayList;
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

public class CastNewsAdapter extends PagerAdapter {
    private static final String TAG = CastNewsAdapter.class.getName();
    private final TextToSpeech mTts;
    private ArrayList<News> contents;
    private LayoutInflater mInflater;
    private MundoAplication app;
    private Context mContext;
    private int currentPage;
    private PageChanged callbackPage;
    @Inject
    ImageLoader mLoader;

    public CastNewsAdapter(final ArrayList<News> categories, final Context mContext, final MundoAplication app, PageChanged cb) {
        this.contents = categories;
        mInflater = LayoutInflater.from(mContext);
        this.app = app;
        this.mContext = mContext;
        this.callbackPage = cb;
        app.inject(this);

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

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (currentPage < contents.size() -1){
                                            currentPage++;
                                            callbackPage.pageChanged(currentPage);
                                            mTts.speak(contents.get(currentPage).getTittle(), TextToSpeech.QUEUE_FLUSH, null, "12345");
                                        }
                                    }
                                });
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
        return contents.size();
    }

    @Override
    public boolean isViewFromObject(View v, Object o) {
        return v == ((View) o);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = mInflater.inflate(R.layout.news_item_cast, null);

        TextView title = (TextView) v.findViewById(R.id.news_title);
        TextView content = (TextView) v.findViewById(R.id.news_content);
        NetworkImageView image = (NetworkImageView) v.findViewById(R.id.news_image);

        title.setText(contents.get(position).getTittle());
        content.setText(contents.get(position).getContent());
        image.setImageUrl(contents.get(position).getImageUrl(), mLoader);

        container.addView(v, 0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void speakNews () {
        mTts.speak(contents.get(0).getTittle(), TextToSpeech.QUEUE_FLUSH, null, "12345");
    }
}