package yzw.weather;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import yzw.weather.db.MyDatabaseHelper;
import yzw.weather.fragment.CollectionFragment;
import yzw.weather.fragment.PredictionFragment;
import yzw.weather.fragment.SettingFragment;
import yzw.weather.model.City;

/**
 * Created by yangzhiwei on 2016/8/8.
 */
public class MainActivity extends AppCompatActivity implements CollectionFragment.CollectionFragmentCallback,LeftmenuAdapter.leftmenucallback,PredictionFragment.predictioncallback {
    private DrawerLayout drawer;
//    private NavigationView mNavigationView;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private RelativeLayout mRelativeLayout;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int cur = 1;
    int LEFTMENU_UP = 1;
    int LEFTMENU_DOWN = 2;
    LeftmenuAdapter upLeftmenuAdapter;
    LeftmenuAdapter downLeftmenuAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRelativeLayout = (RelativeLayout)findViewById(R.id.leftmenu);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_left_recyclerview);
        upLeftmenuAdapter = new LeftmenuAdapter(LEFTMENU_UP,-1,this);
        mRecyclerView.setAdapter(upLeftmenuAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView2 = (RecyclerView)findViewById(R.id.recycler_left_recyclerview2);
        downLeftmenuAdapter = new LeftmenuAdapter(LEFTMENU_DOWN,-1,this);
        mRecyclerView2.setAdapter(downLeftmenuAdapter);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();





//        MenuItem prediction = mNavigationView.getMenu().findItem(R.id.nav_prediction);
//        MenuItem collection = mNavigationView.getMenu().findItem(R.id.nav_collection);

        dbHelper = new MyDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        preferences = getSharedPreferences("weather",MODE_PRIVATE);
//        loadfavouriteid();






//        prediction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                cur = 1;
//                invalidateOptionsMenu();
//                item.setChecked(false);
//                selectItem(1);
//                return false;
//            }
//        });

//        collection.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                cur = 2;
//                invalidateOptionsMenu();
//                item.setChecked(false);
//                selectItem(2);
//                return false;
//            }
//        });

        if (savedInstanceState == null) {
            selectItem(1);
        }

    }


    public void selectItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 1:
                // TODO

                if(preferences.getLong("_id",0)==0 ){
//                    Cursor cursor = db.rawQuery("select * from favourite",null);
//                    cursor.moveToFirst();
//                    int id = cursor.getInt(City.COLUMN_INDEX_ID);
//                    String name = cursor.getString(City.COLUMN_INDEX_NAME);
                      int id = 1790437;
                      String name = "珠海";
                    fragment = PredictionFragment.newInstance(id,name);
                }
                else {
                    Long id = preferences.getLong("_id",0);
                    String name = preferences.getString("name",null);
                    fragment = PredictionFragment.newInstance(id,name);
                }


                break;
            case 2:
                fragment = CollectionFragment.newInstance();
                break;

            case 5:
                fragment = new SettingFragment();
                break;

            default:
                break;
        }
        replaceFragment(fragment);
        drawer.closeDrawer(mRelativeLayout);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_view, fragment);
        ft.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the toggle_item; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        if (cur ==1)
            menuInflater.inflate(R.menu.toggle_item, menu);
        else menuInflater.inflate(R.menu.toggle_item_fragment, menu);
        return true;
    }



    @Override
    public void onCollectionFragmentClickCity(long cityId, String cityName) {
        cur = 1;
        Log.i("xxx", String.valueOf(cityId));
        editor = preferences.edit();
        editor.putLong("_id",cityId);
        editor.putString("name",cityName);
        editor.putString("predictioncity","collection");
        editor.commit();
        Fragment fragment = PredictionFragment.newInstance(cityId, cityName);




        replaceFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                selectItem(1);
                break;
            // action with ID action_settings was selected
            case R.id.action_search:

                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public void mleftmenucallback(int position) {
        switch (position){
            case 0:
                cur = 1;
                invalidateOptionsMenu();
                selectItem(1);
                break;
            case 1:
                cur = 2;
                invalidateOptionsMenu();
                selectItem(2);
                break;
            case 5:
                cur = 2;
                invalidateOptionsMenu();
                selectItem(5);
            default:break;

        }
    }

    @Override
    public void mleftmenucolor(int recyclermode) {
        if(recyclermode == 1){
            downLeftmenuAdapter.changedate();
            upLeftmenuAdapter.notifyDataSetChanged();
        }

        else{
            downLeftmenuAdapter.notifyDataSetChanged();
            upLeftmenuAdapter.changedate();
        }


    }


    @Override
    public void predictioncallbackchangemenucolor() {

//        selectItem(1);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_left_recyclerview);
        upLeftmenuAdapter = new LeftmenuAdapter(LEFTMENU_UP,0,this);
        mRecyclerView.setAdapter(upLeftmenuAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void predictioncallbackreflesh() {
        selectItem(1);
    }
}
