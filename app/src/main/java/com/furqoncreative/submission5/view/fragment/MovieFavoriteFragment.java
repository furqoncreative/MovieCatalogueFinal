package com.furqoncreative.submission5.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.furqoncreative.submission5.R;
import com.furqoncreative.submission5.adapter.MovieFavoriteAdapter;
import com.furqoncreative.submission5.database.FavoriteHelper;
import com.furqoncreative.submission5.model.favorite.Favorite;
import com.furqoncreative.submission5.view.activity.DetailMovieActivity;
import com.stone.vega.library.VegaLayoutManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.furqoncreative.submission5.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.furqoncreative.submission5.helper.MappingHelper.getMovieFavoriteList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFavoriteFragment extends Fragment {

    private static final String EXTRA_STATE = "EXTRA_STATE";
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.null_layout)
    LinearLayout nullLayout;
    private LoadFavoriteCallback callback;
    private MovieFavoriteAdapter adapter;
    private FavoriteHelper helper;

    public MovieFavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_favorite, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();

        if (savedInstanceState == null) {
            new LoadFavoriteAsync(getContext(), callback).execute();
        } else {
            ArrayList<Favorite> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListFavorite(list);
            }
        }
    }

    private void setupView() {
        callback = new LoadFavoriteCallback() {
            @Override
            public void preExecute() {

            }

            @Override
            public void postExecute(Cursor cursor) {
                ArrayList<Favorite> list = getMovieFavoriteList(cursor);
                if (list.size() > 0) {
                    adapter.setListFavorite(list);
                    nullLayout.setVisibility(View.GONE);
                    rvMovie.setVisibility(View.VISIBLE);

                } else {
                    adapter.setListFavorite(new ArrayList<>());
                    nullLayout.setVisibility(View.VISIBLE);
                    rvMovie.setVisibility(View.GONE);
                }
            }
        };
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getContext(), callback);
        Objects.requireNonNull(getActivity()).getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);

        rvMovie.setLayoutManager(new VegaLayoutManager());
        rvMovie.setHasFixedSize(true);

        helper = FavoriteHelper.getInstance(getContext());
        helper.open();

        adapter = new MovieFavoriteAdapter(getActivity(), id -> {
            Intent intent = new Intent(getContext(), DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.MID, id);
            startActivity(intent);
        });
        rvMovie.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListFavorite());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
    }

    interface LoadFavoriteCallback {
        void preExecute();

        void postExecute(Cursor cursor);
    }

    private static class LoadFavoriteAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoriteCallback> weakCallback;

        private LoadFavoriteAsync(Context context, LoadFavoriteCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    static class DataObserver extends ContentObserver {

        final Context context;
        final LoadFavoriteCallback callback;

        DataObserver(Handler handler, Context context, LoadFavoriteCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadFavoriteAsync(context, callback).execute();

        }
    }

}
