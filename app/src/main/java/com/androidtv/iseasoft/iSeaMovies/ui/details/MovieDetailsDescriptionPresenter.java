package com.androidtv.iseasoft.iSeaMovies.ui.details;

import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtv.iseasoft.iSeaMovies.R;
import com.androidtv.iseasoft.iSeaMovies.data.models.MovieDetails;

public class MovieDetailsDescriptionPresenter extends Presenter {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_movie_details, parent, false);
        return new MovieDetailsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        MovieDetails movieDetails = (MovieDetails) item;
        MovieDetailsViewHolder detailsViewHolder = (MovieDetailsViewHolder) viewHolder;
        detailsViewHolder.bind(movieDetails);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
