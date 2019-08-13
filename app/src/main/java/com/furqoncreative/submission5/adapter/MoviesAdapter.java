package com.furqoncreative.submission5.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.furqoncreative.submission5.R;
import com.furqoncreative.submission5.model.movie.Movie;
import com.furqoncreative.submission5.model.movie.MovieGenre;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.furqoncreative.submission5.util.ApiUtils.IMAGE_URL;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private final Context mContext;
    private final PostItemListener mItemListener;
    private List<Movie> mItems;
    private List<MovieGenre> movieGenre;

    public MoviesAdapter(Context context, List<Movie> posts, List<MovieGenre> genres, PostItemListener itemListener) {
        movieGenre = genres;
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(postView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Movie movie = mItems.get(position);
        holder.itemMovieReleaseDate.setText(movie.getReleaseDate().split("-")[0]);
        if (movie.getTitle().length() > 15) {
            holder.itemMovieTitle.setText((movie.getTitle().substring(0, 15) + "..."));
        } else {
            holder.itemMovieTitle.setText(movie.getTitle());
        }
        holder.itemMovieRating.setText(String.valueOf(movie.getRating()));
        holder.itemMovieGenre.setText(holder.getMovieGenres(movie.getGenreIds()));
        Glide.with(mContext)
                .load(IMAGE_URL + movie.getPosterPath())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(holder.itemMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addMovies(List<Movie> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void addGenres(List<MovieGenre> items) {
        movieGenre = items;
        notifyDataSetChanged();
    }

    private Movie getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(int id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final PostItemListener mItemListener;
        @BindView(R.id.item_movie_poster)
        ImageView itemMoviePoster;
        @BindView(R.id.item_movie_release_date)
        TextView itemMovieReleaseDate;
        @BindView(R.id.item_movie_title)
        TextView itemMovieTitle;
        @BindView(R.id.item_movie_genre)
        TextView itemMovieGenre;
        @BindView(R.id.item_movie_rating)
        TextView itemMovieRating;
        @BindView(R.id.btn_movie)
        CardView btnMovie;

        ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        private String getMovieGenres(List<Long> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Long genreId : genreIds) {
                for (MovieGenre genre : movieGenre) {
                    if (genre.getId().equals(genreId)) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }

        @Override
        public void onClick(View view) {
            Movie item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getId());
            notifyDataSetChanged();
        }
    }
}