package com.furqoncreative.submission5.model.favorite;


import android.os.Parcel;
import android.os.Parcelable;

public class Favorite implements Parcelable {

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
    private int id;
    private int mId;
    private String title;
    private String poster;
    private String backdrop;
    private String rating;
    private String releaseDate;
    private String overview;
    private String category;

    public Favorite() {
    }

    public Favorite(int id, int mId, String title, String poster, String backdrop, String rating, String releaseDate, String overview, String category) {
        this.id = id;
        this.mId = mId;
        this.title = title;
        this.poster = poster;
        this.backdrop = backdrop;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.category = category;
    }


    private Favorite(Parcel in) {
        id = in.readInt();
        mId = in.readInt();
        title = in.readString();
        poster = in.readString();
        backdrop = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        category = in.readString();
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setCategoty(String categoty) {
        this.category = categoty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(mId);
        parcel.writeString(title);
        parcel.writeString(poster);
        parcel.writeString(backdrop);
        parcel.writeString(rating);
        parcel.writeString(releaseDate);
        parcel.writeString(overview);
        parcel.writeString(category);
    }
}
