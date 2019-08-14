package com.furqoncreative.submission5.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.furqoncreative.submission5.R;
import com.furqoncreative.submission5.view.fragment.FavoriteFragment;
import com.furqoncreative.submission5.view.fragment.MovieFragment;
import com.furqoncreative.submission5.view.fragment.TvFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.vp_horizontal_ntb)
    ViewPager viewPager;
    @BindView(R.id.ntb_horizontal)
    NavigationTabBar ntb;
    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ViewPagerAdapter adapter;
    String index = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        initUI();

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText(getResources().getString(R.string.app_name));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_change_settings:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                break;
            case R.id.action_exit:
                finish();
                break;
            case R.id.action_search:
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(this);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MovieFragment());
        adapter.addFrag(new TvFragment());
        adapter.addFrag(new FavoriteFragment());
        viewPager.setAdapter(adapter);
    }

    private void initUI() {
        setupViewPager(viewPager);
        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_movie),
                        Color.parseColor(colors[1]))
                        .title(getResources().getString(R.string.tab_movie))
                        .badgeTitle("")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tv_show),
                        Color.parseColor(colors[1]))
                        .title(getResources().getString(R.string.tab_tv))
                        .badgeTitle("")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_favorite),
                        Color.parseColor(colors[1]))
                        .title(getResources().getString(R.string.tab_fav))
                        .badgeTitle("")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setBgColor(getResources().getColor(R.color.softBlack));
        navigationTabBar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        navigationTabBar.setInactiveColor(getResources().getColor(R.color.colorAccent));
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
                index = String.valueOf(position);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(() -> {
            for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                navigationTabBar.postDelayed(() -> model.showBadge(), i * 100);
            }
        }, 500);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent i = new Intent(MainActivity.this, ResultActivity.class);
        i.putExtra(ResultActivity.KEYWORD, query);
        i.putExtra(ResultActivity.INDEX, index);
        Log.i("INDEX", " " + index);
        startActivity(i);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
