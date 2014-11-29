package com.madgeeklabs.mglelmundo.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.madgeeklabs.mglelmundo.R;
import com.madgeeklabs.mglelmundo.adapters.CategoriesAdapter;
import com.madgeeklabs.mglelmundo.adapters.NewsAdapter;
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


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();
    @InjectView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        ((MundoAplication) getApplication()).inject(this);

        Log.d(TAG, "App iniciada");

        final ArrayList<String> categories = new ArrayList<>();
        categories.add("TECNOLOGIA");
        categories.add("DEPORTE");
        categories.add("ACTUALIDAD");

        CategoriesAdapter catAdapter = new CategoriesAdapter(categories, this, (MundoAplication)getApplication());

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                getActionBar().setTitle(categories.get(i));
                ((CategoriesAdapter)pager.getAdapter()).setCurrentPage(i);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        pager.setAdapter(catAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
