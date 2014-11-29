package com.madgeeklabs.mglelmundo.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.madgeeklabs.mglelmundo.R;
import com.madgeeklabs.mglelmundo.adapters.NewsAdapter;
import com.madgeeklabs.mglelmundo.api.MundoApi;
import com.madgeeklabs.mglelmundo.base.MundoAplication;
import com.madgeeklabs.mglelmundo.models.News;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MoreInfoActivity extends Activity {

    @Inject
    RestAdapter restAdapter;
    private MundoApi api;
    @InjectView(R.id.more_info_activity)
    ListView relatedNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        ButterKnife.inject(this);
        ((MundoAplication) getApplication()).inject(this);

        api = restAdapter.create(MundoApi.class);

        String theme = getIntent().getStringExtra("THEME");

        getActionBar().setTitle("Mas sobre " + theme);

        api.getRelatedNews(theme, new Callback<List<News>>() {
            @Override
            public void success(List<News> newses, Response response) {
                NewsAdapter adapter = new NewsAdapter((java.util.ArrayList<News>) newses, MoreInfoActivity.this, (MundoAplication) getApplication());
                relatedNews.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_more_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
