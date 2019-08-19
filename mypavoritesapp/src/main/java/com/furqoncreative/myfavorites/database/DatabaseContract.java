package com.furqoncreative.myfavorites.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    private static final String AUTHORITY = "com.furqoncreative.submission5";
    private static final String SCHEME = "content";


    public static final class FavoriteColumns implements BaseColumns {
        public static final String ID = "mId";
        public static final String TITLE = "title";
        public static final String POSTER = "poster";
        public static final String BACKDROP = "backdrop";
        public static final String RATING = "rating";
        public static final String RELEASE_DATE = "release_date";
        public static final String OVERVIEW = "overview";
        public static final String CATEGORY = "category";
        static final String TABLE_FAVORITE = "db_favorite";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITE)
                .build();

    }


}
