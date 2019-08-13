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
import com.furqoncreative.submission5.model.tv.Tv;
import com.furqoncreative.submission5.model.tv.TvGenre;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.furqoncreative.submission5.util.ApiUtils.IMAGE_URL;

public class TvsAdapter extends RecyclerView.Adapter<TvsAdapter.ViewHolder> {


    private final Context mContext;
    private final PostItemListener mItemListener;
    private List<Tv> mItems;
    private List<TvGenre> mTvGenre;

    public TvsAdapter(Context context, List<Tv> posts, List<TvGenre> genres, PostItemListener itemListener) {
        mTvGenre = genres;
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

        Tv tvs = mItems.get(position);
        holder.itemMovieReleaseDate.setText(tvs.getFirstAirDate().split("-")[0]);
        if (tvs.getOriginalName().length() > 15) {
            holder.itemMovieTitle.setText((tvs.getOriginalName().substring(0, 15) + "..."));
        } else {
            holder.itemMovieTitle.setText(tvs.getOriginalName());
        }
        holder.itemMovieRating.setText(String.valueOf(tvs.getRating()));
        holder.itemMovieGenre.setText(holder.getTvGenres(tvs.getGenreIds()));
        Glide.with(mContext)
                .load(IMAGE_URL + tvs.getPosterPath())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(holder.itemMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addTvs(List<Tv> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void addGenres(List<TvGenre> items) {
        mTvGenre = items;
        notifyDataSetChanged();
    }

    private Tv getItem(int adapterPosition) {
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

        private String getTvGenres(List<Long> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Long genreId : genreIds) {
                for (TvGenre genre : mTvGenre) {
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
            Tv item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getId());
            notifyDataSetChanged();
        }
    }
}
