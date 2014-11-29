package com.madgeeklabs.mglelmundo.Activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.madgeeklabs.mglelmundo.R;
import com.madgeeklabs.mglelmundo.adapters.CategoriesAdapter;
import com.madgeeklabs.mglelmundo.adapters.MenuAdapter;
import com.madgeeklabs.mglelmundo.adapters.NewsAdapter;
import com.madgeeklabs.mglelmundo.api.MundoApi;
import com.madgeeklabs.mglelmundo.base.MundoAplication;
import com.madgeeklabs.mglelmundo.models.Category;
import com.madgeeklabs.mglelmundo.models.News;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.listView_categories)
    ListView drawer;
    @InjectView(R.id.left_drawer)
    RelativeLayout drawerHolder;
    private CategoriesAdapter catAdapter;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;
    private ArrayList<Category> options = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        ((MundoAplication) getApplication()).inject(this);

        Log.d(TAG, "App iniciada");

        final ArrayList<String> categories = new ArrayList<>();
        categories.add("TECNOLOGIA");
        categories.add("ACTUALIDAD");
        categories.add("DEPORTES");

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_action_name,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("USUARIO");
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        options.add(new Category(getResources().getString(R.string.origines), R.drawable.ic_launcher));
        options.add(new Category(getResources().getString(R.string.Wikipedia), R.drawable.ic_launcher));
        options.add(new Category(getResources().getString(R.string.Memes), R.drawable.ic_launcher));
        options.add(new Category(getResources().getString(R.string.Blogs), R.drawable.ic_launcher));
        options.add(new Category(getResources().getString(R.string.Ajustes), R.drawable.ic_launcher));
        options.add(new Category(getResources().getString(R.string.LogOut), R.drawable.ic_launcher));

        MenuAdapter menuAdapter = new MenuAdapter(MainActivity.this, options);

        drawer.setAdapter(menuAdapter);

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawer.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerHolder);
            }
        });


        catAdapter = new CategoriesAdapter(categories, this, (MundoAplication)getApplication());

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                mTitle = ((CategoriesAdapter)pager.getAdapter()).getTypeOfContent(i);
                getActionBar().setTitle(((CategoriesAdapter)pager.getAdapter()).getTypeOfContent(i));
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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_read){
//            catAdapter.speakNews();
            promptSpeechInput();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptSpeechInput() {
        Locale spanish = new Locale("es", "ES");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, spanish);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        Log.d(TAG, "recognized " + result);
                        String normalized = Normalizer.normalize(result.get(0), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                        if (normalized.toLowerCase().contains("actualidad")) {
                            pager.setCurrentItem(catAdapter.getPageOfContent("actualidad"),false);
                            catAdapter.speakNews();
                        } else if (normalized.toLowerCase().contains("deportes")) {
                            pager.setCurrentItem(catAdapter.getPageOfContent("deportes"),false);
                            catAdapter.speakNews();
                        } else if (normalized.toLowerCase().contains("tecnologia")){
                            pager.setCurrentItem(catAdapter.getPageOfContent("tecnologia"),false);
                            catAdapter.speakNews();
                        } else if (normalized.toLowerCase().contains("mas")) {
                            Intent intent = new Intent(MainActivity.this, MoreInfoActivity.class);
                            intent.putExtra("THEME", catAdapter.getCurrentNewsTheme());
                            startActivity(intent);
                        }
                    }
                break;
            }
        }
    }
}
