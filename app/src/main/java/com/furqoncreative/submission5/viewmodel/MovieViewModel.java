package com.furqoncreative.submission5.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.furqoncreative.submission5.model.movie.Movie;
import com.furqoncreative.submission5.model.movie.MovieGenre;
import com.furqoncreative.submission5.model.movie.MovieGenresResponse;
import com.furqoncreative.submission5.model.movie.MoviesResponse;
import com.furqoncreative.submission5.network.ApiInterface;
import com.furqoncreative.submission5.util.ApiUtils;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.furqoncreative.submission5.BuildConfig.API_KEY;

@SuppressWarnings("ALL")
public class MovieViewModel extends ViewModel {

    private static final ApiInterface apiInterface = new ApiUtils().getApi();
    private final MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<MovieGenre>> listMovieGenre = new MutableLiveData<>();
    private final MutableLiveData<Movie> movie = new MutableLiveData<>();

    public LiveData<ArrayList<Movie>> getMovies() {
        return listMovies;
    }

    public void setMovies(final String language) {
        apiInterface.getMovies(API_KEY, language).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Movie> movies = new ArrayList<>(Objects.requireNonNull(response.body()).getMovies());
                    listMovies.postValue(movies);
                    Log.d("Movie", "success loading from API");

                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.d("Movie", "error loading from API");

            }
        });

    }

    public LiveData<ArrayList<MovieGenre>> getGenres() {
        return listMovieGenre;
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

    public void setMovie(int tv_id, String language) {
        apiInterface.getMovie(tv_id, API_KEY, language).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    movie.postValue(response.body());
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");

            }
        });
    }

    public void setGenre(String language) {
        apiInterface.getMovieGenres(API_KEY, language).enqueue(new Callback<MovieGenresResponse>() {
            @Override
            public void onResponse(Call<MovieGenresResponse> call, Response<MovieGenresResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<MovieGenre> movieGenres = new ArrayList<>(Objects.requireNonNull(response.body()).getGenres());
                    listMovieGenre.postValue(movieGenres);
                    Log.d("Movie Genre", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<MovieGenresResponse> call, Throwable t) {
                Log.d("Movie Genre", "error loading from API");

            }
        });
    }


}
