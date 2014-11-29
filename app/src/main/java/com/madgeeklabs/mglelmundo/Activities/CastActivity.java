package com.madgeeklabs.mglelmundo.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.madgeeklabs.mglelmundo.Interfaces.PageChanged;
import com.madgeeklabs.mglelmundo.R;
import com.madgeeklabs.mglelmundo.adapters.CastNewsAdapter;
import com.madgeeklabs.mglelmundo.api.MundoApi;
import com.madgeeklabs.mglelmundo.base.MundoAplication;
import com.madgeeklabs.mglelmundo.models.News;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CastActivity extends Activity implements PageChanged{

    private static final String TAG = CastActivity.class.getName();
    @InjectView(R.id.pager_cast) ViewPager castPager;
    @Inject RestAdapter restAdapter;
    private CastNewsAdapter currentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);

        ButterKnife.inject(this);
        ((MundoAplication) getApplication()).inject(this);

        MundoApi api = restAdapter.create(MundoApi.class);

        String type = getIntent().getStringExtra("CONTENT");


        switch (type.toLowerCase()) {
            case "actualidad":
                api.getBreakingNews(new Callback<List<News>>() {
                    @Override
                    public void success(List<News> newses, Response response) {
                        currentAdapter = new CastNewsAdapter((ArrayList<News>) newses, CastActivity.this, (MundoAplication)getApplication(), CastActivity.this);
                        castPager.setAdapter(currentAdapter);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "something failed" + error.getMessage());
                    }
                });
                break;
            case "deportes":
                api.getSportsNews(new Callback<List<News>>() {
                    @Override
                    public void success(List<News> newses, Response response) {
                        currentAdapter = new CastNewsAdapter((ArrayList<News>) newses, CastActivity.this, (MundoAplication)getApplication(), CastActivity.this);
                        castPager.setAdapter(currentAdapter);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "something failed" + error.getMessage());
                    }
                });
                break;
            case "tecnologia":
                api.getTecnolgiesNews(new Callback<List<News>>() {
                    @Override
                    public void success(List<News> newses, Response response) {
                        currentAdapter = new CastNewsAdapter((ArrayList<News>) newses, CastActivity.this, (MundoAplication)getApplication(), CastActivity.this);
                        castPager.setAdapter(currentAdapter);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "something failed" + error.getMessage());
                    }
                });
                break;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_read) {
            currentAdapter.speakNews();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void pageChanged(int page) {
        castPager.setCurrentItem(page, true);
    }
}
