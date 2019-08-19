package com.furqoncreative.submission5.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.furqoncreative.submission5.R;
import com.furqoncreative.submission5.adapter.MoviesAdapter;
import com.furqoncreative.submission5.adapter.TvsAdapter;
import com.furqoncreative.submission5.model.movie.Movie;
import com.furqoncreative.submission5.model.movie.MovieGenre;
import com.furqoncreative.submission5.model.tv.Tv;
import com.furqoncreative.submission5.model.tv.TvGenre;
import com.furqoncreative.submission5.viewmodel.MovieViewModel;
import com.furqoncreative.submission5.viewmodel.TvViewModel;
import com.stone.vega.library.VegaLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity {

    static final String KEYWORD = "keyword";
    static final String INDEX = "index";
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.null_result)
    LinearLayout nullresult;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtResult)
    TextView txtResult;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    private String QUERY;
    private String LANGUAGE;
    private MoviesAdapter moviesAdapter;
    private final Observer<ArrayList<Movie>> getMovies = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> items) {
            if (items != null) {
                moviesAdapter.addMovies(items);
                showLoading();
                if (moviesAdapter.getItemCount() == 0) {
                    showNull();
                    rvSearch.setVisibility(View.GONE);
                }
            } else {
                showLoading();
                showNull();
            }
        }
    };

    private final Observer<ArrayList<MovieGenre>> getMovieGenres = new Observer<ArrayList<MovieGenre>>() {
        @Override
        public void onChanged(ArrayList<MovieGenre> items) {
            if (items != null) {
                moviesAdapter.addGenres(items);
                showLoading();
            }
        }
    };

    private MovieViewModel movieViewModel;
    private TvsAdapter tvsAdapter;
    private final Observer<ArrayList<Tv>> getTvs = new Observer<ArrayList<Tv>>() {
        @Override
        public void onChanged(ArrayList<Tv> items) {
            if (items != null) {
                showLoading();
                tvsAdapter.addTvs(items);
                if (tvsAdapter.getItemCount() == 0) {
                    showNull();
                    rvSearch.setVisibility(View.GONE);
                }
            } else {
                showLoading();
                showNull();
            }
        }
    };

    private final Observer<ArrayList<TvGenre>> getTvGenres = new Observer<ArrayList<TvGenre>>() {
        @Override
        public void onChanged(ArrayList<TvGenre> items) {
            if (items != null) {
                tvsAdapter.addGenres(items);
                showLoading();
            } else {
                showLoading();
                showNull();
            }
        }
    };
    private TvViewModel tvViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        QUERY = getIntent().getStringExtra(KEYWORD);
        String CATEGORY = getIntent().getStringExtra(INDEX);
        setupToolbar();
        checkConnection();
        getLanguage();


        if (CATEGORY.equals("0")) {
            setupMovieViewModeL();
            setupMovieData();
            setupMovieView();
            Log.i("Categotry", "MOVIE");
        } else {
            setupTvViewModeL();
            setupTvData();
            setupTvView();
            Log.i("Categotry", "TV");

        }

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText((getResources().getString(R.string.result) + " \""+QUERY+"\""));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) Objects.requireNonNull(getApplicationContext())
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = Objects.requireNonNull(connMgr).getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            showLoading();
            showError();
        }
    }

    private void getLanguage() {
        LANGUAGE = Locale.getDefault().toString();
        if (LANGUAGE.equals("in_ID")) {
            LANGUAGE = "id_ID";
        }
    }

    private void setupTvView() {
        tvsAdapter = new TvsAdapter(getApplicationContext(), new ArrayList<>(), new ArrayList<>(), id -> {
            Intent intent = new Intent(getApplicationContext(), DetailTvActivity.class);
            intent.putExtra(DetailTvActivity.TID, id);
            startActivity(intent);
        });
        rvSearch.setLayoutManager(new VegaLayoutManager());
        rvSearch.setAdapter(tvsAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getApplicationContext()), DividerItemDecoration.VERTICAL);
        rvSearch.addItemDecoration(itemDecoration);
    }


    private void setupTvData() {
        tvViewModel.searchTvs(LANGUAGE, QUERY);
        tvViewModel.setGenre(LANGUAGE);
    }

    private void setupTvViewModeL() {
        tvViewModel = new ViewModelProvider(this).get(TvViewModel.class);
        tvViewModel.getTvs().observe(this, getTvs);
        tvViewModel.getGenres().observe(this, getTvGenres);

    }

    private void setupMovieView() {
        moviesAdapter = new MoviesAdapter(getApplicationContext(), new ArrayList<>(), new ArrayList<>(), id -> {
            Intent intent = new Intent(getApplicationContext(), DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.MID, id);
            startActivity(intent);
        });
        rvSearch.setLayoutManager(new VegaLayoutManager());
        rvSearch.setAdapter(moviesAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getApplicationContext()), DividerItemDecoration.VERTICAL);
        rvSearch.addItemDecoration(itemDecoration);

    }

    private void setupMovieData() {
        movieViewModel.searchMovies(LANGUAGE, QUERY);
        movieViewModel.setGenre(LANGUAGE);
    }

    private void setupMovieViewModeL() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, getMovies);
        movieViewModel.getGenres().observe(this, getMovieGenres);

    }


    private void showError() {
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void showNull() {
        nullresult.setVisibility(View.VISIBLE);
        txtResult.setText(("\""+QUERY + "\" " + getResources().getString(R.string.null_result)));
    }

    private void showLoading() {
        if (false) {
            avi.show();
        } else {
            avi.hide();
        }
    }
}
