package com.androidtv.iseasoft.iSeaMovies.ui.details;

import android.graphics.drawable.GradientDrawable;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidtv.iseasoft.iSeaMovies.R;
import com.androidtv.iseasoft.iSeaMovies.data.models.Genre;
import com.androidtv.iseasoft.iSeaMovies.data.models.MovieDetails;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsViewHolder extends Presenter.ViewHolder {
    @BindView(R.id.movie_title)
    TextView movieTitleTV;

    @BindView(R.id.movie_year)
    TextView movieYearTV;

    @BindView(R.id.overview)
    TextView movieOverview;

    @BindView(R.id.runtime)
    TextView mRuntimeTV;

    @BindView(R.id.tagline)
    TextView mTaglineTV;

    @BindView(R.id.director_tv)
    TextView mDirectorTv;

    @BindView(R.id.overview_label)
    TextView mOverviewLabelTV;

    @BindView(R.id.genres)
    LinearLayout mGenresLayout;

    private View mItemView;

    public MovieDetailsViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        mItemView = view;
    }

    public void bind(MovieDetails movie){
        if(movie != null && movie.getTitle() != null){
            mRuntimeTV.setText(String.format(Locale.getDefault(), "%d minutes", movie.getRuntime()));
            mTaglineTV.setText(movie.getTagline());
            movieTitleTV.setText(movie.getTitle());
            movieYearTV.setText(String.format(Locale.getDefault(), "(%s)", movie.getReleaseDate().substring(0, 4)));
            movieOverview.setText(movie.getOverview());
            mGenresLayout.removeAllViews();

            if (movie.getDirector() != null) {
                mDirectorTv.setText(String.format(Locale.getDefault(), "Director: %s", movie.getDirector()));
            }

            int _16dp = (int) mItemView.getResources().getDimension(R.dimen.full_padding);
            int _8dp = (int) mItemView.getResources().getDimension(R.dimen.half_padding);
            float corner = mItemView.getResources().getDimension(R.dimen.genre_corner);

            // Adds each genre to the genre layout
            for (Genre g : movie.getGenres()) {
                TextView tv = new TextView(mItemView.getContext());
                tv.setText(g.getName());
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(corner);
                shape.setColor(ContextCompat.getColor(mItemView.getContext(), R.color.primary_dark));
                tv.setPadding(_8dp, _8dp, _8dp, _8dp);
                tv.setBackground(shape);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                params.setMargins(0, 0, _16dp, 0);
                tv.setLayoutParams(params);

                mGenresLayout.addView(tv);
            }
        }
    }
}
