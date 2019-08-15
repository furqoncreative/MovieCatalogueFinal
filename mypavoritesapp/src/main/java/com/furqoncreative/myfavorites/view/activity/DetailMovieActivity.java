package com.furqoncreative.myfavorites.view.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.furqoncreative.myfavorites.R;
import com.furqoncreative.myfavorites.model.Favorite;
import com.furqoncreative.myfavorites.model.Movie;
import com.furqoncreative.myfavorites.viewmodel.MovieViewModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.furqoncreative.myfavorites.database.DatabaseContract.FavoriteColumns.BACKDROP;
import static com.furqoncreative.myfavorites.database.DatabaseContract.FavoriteColumns.CATEGORY;
import static com.furqoncreative.myfavorites.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.furqoncreative.myfavorites.database.DatabaseContract.FavoriteColumns.ID;
import static com.furqoncreative.myfavorites.database.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.furqoncreative.myfavorites.database.DatabaseContract.FavoriteColumns.POSTER;
import static com.furqoncreative.myfavorites.database.DatabaseContract.FavoriteColumns.RATING;
import static com.furqoncreative.myfavorites.database.DatabaseContract.FavoriteColumns.RELEASE_DATE;
import static com.furqoncreative.myfavorites.database.DatabaseContract.FavoriteColumns.TITLE;
import static com.furqoncreative.myfavorites.util.ApiUtils.IMAGE_URL;

@SuppressWarnings("ALL")
public class DetailMovieActivity extends AppCompatActivity {

    public static final String MID = "movie_id";
    public static final String GENRE = "genre";
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.imgBackdrop)
    ImageView imgBackdrop;
    @BindView(R.id.imgPoster)
    ImageView imgPoster;
    @BindView(R.id.txtReleaseDate)
    TextView txtReleaseDate;
    @BindView(R.id.imgRating)
    ImageView imgRating;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtRating)
    TextView txtRating;
    @BindView(R.id.labelOverview)
    TextView labelOverview;
    @BindView(R.id.txtOverview)
    TextView txtOverview;
    @BindView(R.id.imgFavorite)
    ImageView imgFavorite;
    private MovieViewModel movieViewModel;
    private int MOVIE_ID;
    private String MOVIE_GENRE;
    private int id, mId;
    private String backdropPath, title, releaseDate, overview, poster, rating, category;
    private final Observer<Movie> getMovie = new Observer<Movie>() {
        @Override
        public void onChanged(Movie movie) {
            if (movie != null) {
                showLoading();
                imgBack.setVisibility(View.VISIBLE);
                imgFavorite.setVisibility(View.VISIBLE);
                imgBackdrop.setVisibility(View.VISIBLE);
                Glide.with(DetailMovieActivity.this)
                        .load(IMAGE_URL + movie.getBackdropPath())
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .into(imgBackdrop);

                imgPoster.setVisibility(View.VISIBLE);
                Glide.with(DetailMovieActivity.this)
                        .load(IMAGE_URL + movie.getPosterPath())
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .into(imgPoster);
                txtTitle.setText(movie.getTitle());
                txtReleaseDate.setVisibility(View.VISIBLE);
                txtReleaseDate.setText(movie.getReleaseDate());
                imgRating.setVisibility(View.VISIBLE);
                txtRating.setText(movie.getRating().toString());
                labelOverview.setVisibility(View.VISIBLE);

                mId = movie.getId();
                title = movie.getTitle();
                poster = movie.getPosterPath();
                backdropPath = movie.getBackdropPath();
                releaseDate = movie.getReleaseDate();
                rating = movie.getRating().toString();
                overview = movie.getOverview();
                checkFavorite();
                if (movie.getOverview().length() == 0) {
                    txtOverview.setText(getResources().getString(R.string.not_found));
                } else {
                    txtOverview.setText(movie.getOverview());
                }
            }
        }
    };
    private Favorite favorite = new Favorite();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);
        checkConnection();
        setupViewModeL();
        setupData();

    }

    private void checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            showLoading();
            showError();
        }
    }

    private void setupData() {
        MOVIE_ID = getIntent().getIntExtra(MID, MOVIE_ID);
        String LANGUANGE = Locale.getDefault().toString();
        if (LANGUANGE.equals("in_ID")) {
            LANGUANGE = "id_ID";
        }
        movieViewModel.setMovie(MOVIE_ID, LANGUANGE);

    }

    private void setupViewModeL() {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovie().observe(this, getMovie);

    }

    private void showLoading() {
        if (false) {
            avi.smoothToShow();
        } else {
            avi.smoothToHide();
        }
    }


    private void showError() {
        errorLayout.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.imgBack})
    public void doBack(View view) {
        finish();

    }

    @OnClick({R.id.imgFavorite})
    public void doFavorite(View view) {
        if (checkFavorite()) {
            Uri uri = Uri.parse(CONTENT_URI + "/" + id);
            int i = getContentResolver().delete(uri, null, null);
            imgFavorite.setImageResource(R.drawable.ic_unfavorite);
            Toast.makeText(this, getString(R.string.unfavorite), Toast.LENGTH_SHORT).show();

        } else {
            favorite.setmId(mId);
            favorite.setTitle(title);
            favorite.setPoster(poster);
            favorite.setBackdrop(backdropPath);
            favorite.setRating(rating);
            favorite.setReleaseDate(releaseDate);
            favorite.setOverview(overview);
            favorite.setCategoty("movie");

            ContentValues values = new ContentValues();
            values.put(ID, mId);
            values.put(TITLE, title);
            values.put(POSTER, poster);
            values.put(BACKDROP, backdropPath);
            values.put(RATING, rating);
            values.put(RELEASE_DATE, releaseDate);
            values.put(OVERVIEW, overview);
            values.put(CATEGORY, "movie");

            if (getContentResolver().insert(CONTENT_URI, values) != null) {
                Toast.makeText(this, title + " " + getString(R.string.favorite), Toast.LENGTH_SHORT).show();
                imgFavorite.setImageResource(R.drawable.ic_favorite);
            } else {
                Toast.makeText(this, title + " " + getString(R.string.favorite_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkFavorite() {
        Uri uri = Uri.parse(CONTENT_URI + "");
        boolean favorite = false;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int getmId;
        if (cursor.moveToFirst()) {
            do {
                getmId = cursor.getInt(1);
                if (getmId == mId) {
                    id = cursor.getInt(0);
                    imgFavorite.setImageResource(R.drawable.ic_favorite);
                    favorite = true;
                }
            } while (cursor.moveToNext());

        }

        return favorite;
    }

}
