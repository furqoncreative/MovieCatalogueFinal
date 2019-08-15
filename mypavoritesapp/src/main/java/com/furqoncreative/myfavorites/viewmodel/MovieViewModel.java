package com.furqoncreative.myfavorites.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.furqoncreative.myfavorites.model.Movie;
import com.furqoncreative.myfavorites.network.ApiInterface;
import com.furqoncreative.myfavorites.util.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.furqoncreative.myfavorites.BuildConfig.API_KEY;

@SuppressWarnings("ALL")
public class MovieViewModel extends ViewModel {

    private static final ApiInterface apiInterface = new ApiUtils().getApi();
    private final MutableLiveData<Movie> movie = new MutableLiveData<>();

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


}
