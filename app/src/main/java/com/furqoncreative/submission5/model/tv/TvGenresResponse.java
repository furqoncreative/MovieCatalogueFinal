package com.furqoncreative.submission5.model.tv;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TvGenresResponse {

    @SerializedName("genres")
    private List<TvGenre> mGenres;

    public List<TvGenre> getGenres() {
        return mGenres;
    }

}
