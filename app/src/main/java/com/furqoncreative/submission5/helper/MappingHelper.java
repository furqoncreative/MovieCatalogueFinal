package com.furqoncreative.submission5.helper;

import android.database.Cursor;

import com.furqoncreative.submission5.model.favorite.Favorite;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.furqoncreative.submission5.database.DatabaseContract.FavoriteColumns.BACKDROP;
import static com.furqoncreative.submission5.database.DatabaseContract.FavoriteColumns.CATEGORY;
import static com.furqoncreative.submission5.database.DatabaseContract.FavoriteColumns.ID;
import static com.furqoncreative.submission5.database.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.furqoncreative.submission5.database.DatabaseContract.FavoriteColumns.POSTER;
import static com.furqoncreative.submission5.database.DatabaseContract.FavoriteColumns.RATING;
import static com.furqoncreative.submission5.database.DatabaseContract.FavoriteColumns.RELEASE_DATE;
import static com.furqoncreative.submission5.database.DatabaseContract.FavoriteColumns.TITLE;

public class MappingHelper {

    public static ArrayList<Favorite> getMovieFavoriteList(Cursor cursor) {
        ArrayList<Favorite> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            int mId = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(POSTER));
            String backdrop = cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP));
            String rating = cursor.getString(cursor.getColumnIndexOrThrow(RATING));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY));
            if (category.equals("movie")) {
                list.add(new Favorite(id, mId, title, poster, backdrop, rating, releaseDate, overview, category));
            }
        }

        return list;
    }

    public static ArrayList<Favorite> getTvFavoriteList(Cursor cursor) {
        ArrayList<Favorite> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            int mId = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(POSTER));
            String backdrop = cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP));
            String rating = cursor.getString(cursor.getColumnIndexOrThrow(RATING));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY));
            if (category.equals("tv")) {
                list.add(new Favorite(id, mId, title, poster, backdrop, rating, releaseDate, overview, category));
            }
        }

        return list;
    }
}
