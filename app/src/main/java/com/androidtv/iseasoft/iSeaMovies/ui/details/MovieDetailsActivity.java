package com.androidtv.iseasoft.iSeaMovies.ui.details;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.androidtv.iseasoft.iSeaMovies.R;
import com.androidtv.iseasoft.iSeaMovies.dagger.modules.HttpClientModule;
import com.androidtv.iseasoft.iSeaMovies.data.models.Movie;
import com.androidtv.iseasoft.iSeaMovies.data.models.MovieDetails;
import com.androidtv.iseasoft.iSeaMovies.ui.base.BaseTvActivity;
import com.androidtv.iseasoft.iSeaMovies.ui.base.GlideBackgroundManager;

public class MovieDetailsActivity extends BaseTvActivity {
    GlideBackgroundManager mBackgroundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Movie movie = getIntent().getExtras().getParcelable(Movie.class.getSimpleName());
        MovieDetailsFragment detailsFragment = MovieDetailsFragment.newInstance(movie);
        addFragment(detailsFragment);

        mBackgroundManager = new GlideBackgroundManager(this);
        if(movie != null && movie.getBackdropPath() != null){
            mBackgroundManager.loadImage(HttpClientModule.BACKDROP_URL + movie.getBackdropPath());
        } else {
            mBackgroundManager.setBackground(ContextCompat.getDrawable(this, R.drawable.material_bg));
        }

    }
}
