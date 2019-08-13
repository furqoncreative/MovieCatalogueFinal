package com.furqoncreative.submission5.model.tv;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvsResponse {

    @SerializedName("page")
    private Long mPage;
    @SerializedName("results")
    private List<Tv> mResults;
    @SerializedName("total_pages")
    private Long mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public List<Tv> getTvs() {
        return mResults;
    }

}
