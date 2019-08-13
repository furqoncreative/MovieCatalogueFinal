package com.furqoncreative.submission5.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.furqoncreative.submission5.R;
import com.furqoncreative.submission5.model.favorite.Favorite;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.furqoncreative.submission5.util.ApiUtils.IMAGE_URL;

public class TvFavoriteAdapter extends RecyclerView.Adapter<TvFavoriteAdapter.Viewholder> {
    private final ArrayList<Favorite> favorites = new ArrayList<>();
    private final Activity activity;
    private final PostItemListener postItemListener;

    public TvFavoriteAdapter(Activity activity, PostItemListener postItemListener) {
        this.activity = activity;
        this.postItemListener = postItemListener;
    }

    public ArrayList<Favorite> getListFavorite() {
        return favorites;
    }

    public void setListFavorite(ArrayList<Favorite> listFavorite) {

        if (listFavorite.size() > 0) {
            this.favorites.clear();
        }
        this.favorites.addAll(listFavorite);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_favorite, parent, false);

        return new Viewholder(postView, this.postItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Favorite favorite = favorites.get(position);
        if (favorite.getTitle().length() > 15) {
            holder.itemMovieTitle.setText((favorite.getTitle().substring(0, 15) + "..."));
        } else {
            holder.itemMovieTitle.setText(favorite.getTitle());
        }
        holder.itemMovieRating.setText(favorite.getRating());
        holder.itemMovieReleaseDate.setText(favorite.getReleaseDate().split("-")[0]);
        Glide.with(activity)
                .load(IMAGE_URL + favorites.get(position).getPoster())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(holder.itemMoviePoster);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    private Favorite getItem(int adapterPosition) {
        return favorites.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(int mId);
    }

    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final PostItemListener mItemListener;
        @BindView(R.id.item_movie_poster)
        ImageView itemMoviePoster;
        @BindView(R.id.item_movie_release_date)
        TextView itemMovieReleaseDate;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.item_movie_title)
        TextView itemMovieTitle;
        @BindView(R.id.item_movie_rating)
        TextView itemMovieRating;
        @BindView(R.id.btn_movie)
        CardView btnMovie;

        Viewholder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Favorite item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getmId());
            notifyDataSetChanged();
        }
    }
}
