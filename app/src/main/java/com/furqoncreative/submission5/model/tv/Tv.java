package com.furqoncreative.submission5.model.tv;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Tv {

    @SerializedName("backdrop_path")
    private String mBackdropPath;
    @SerializedName("first_air_date")
    private String mFirstAirDate;
    @SerializedName("genre_ids")
    private List<Long> mGenreIds;
    @SerializedName("id")
    private Integer mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("origin_country")
    private List<String> mOriginCountry;
    @SerializedName("original_language")
    private String mOriginalLanguage;
    @SerializedName("original_name")
    private String mOriginalName;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("popularity")
    private Double mPopularity;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("vote_average")
    private Double mVoteAverage;
    @SerializedName("vote_count")
    private Long mVoteCount;

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public String getFirstAirDate() {
        return mFirstAirDate;
    }

    public List<Long> getGenreIds() {
        return mGenreIds;
    }

    public Integer getId() {
        return mId;
    }

    public String getOriginalName() {
        return mOriginalName;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public Double getRating() {
        return mVoteAverage;
    }

}
