package com.furqoncreative.myfavorites.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.furqoncreative.myfavorites.model.Tv;
import com.furqoncreative.myfavorites.network.ApiInterface;
import com.furqoncreative.myfavorites.util.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.furqoncreative.myfavorites.BuildConfig.API_KEY;

public class TvViewModel extends ViewModel {

    private static final ApiInterface apiInterface = ApiUtils.getApi();
    private final MutableLiveData<Tv> tv = new MutableLiveData<>();

    public LiveData<Tv> getTv() {
        return tv;
    }


    public void setTv(int tv_id, String language) {
        apiInterface.getTv(tv_id, API_KEY, language).enqueue(new Callback<Tv>() {
            @Override
            public void onResponse(Call<Tv> call, Response<Tv> response) {
                if (response.isSuccessful()) {
                    tv.postValue(response.body());
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<Tv> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");

            }
        });
    }


}
