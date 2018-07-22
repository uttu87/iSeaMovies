package com.androidtv.iseasoft.iSeaMovies.ui.details;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewLogoPresenter;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v7.graphics.Palette;

import com.androidtv.iseasoft.iSeaMovies.App;
import com.androidtv.iseasoft.iSeaMovies.Config;
import com.androidtv.iseasoft.iSeaMovies.dagger.modules.HttpClientModule;
import com.androidtv.iseasoft.iSeaMovies.data.Api.TheMovieDbAPI;
import com.androidtv.iseasoft.iSeaMovies.data.models.Movie;
import com.androidtv.iseasoft.iSeaMovies.data.models.MovieDetails;
import com.androidtv.iseasoft.iSeaMovies.data.models.PaletteColors;
import com.androidtv.iseasoft.iSeaMovies.ui.utils.PaletteUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MovieDetailsFragment extends DetailsFragment implements Palette.PaletteAsyncListener{
    public static String TRANSITION_NAME = "poster_transition";

    @Inject
    TheMovieDbAPI mDbAPI;

    private Movie mMovie;
    private MovieDetails mMovieDetails;
    private ArrayObjectAdapter mAdtapter;
    private CustomMovieDetailsPresenter mFullWidthMovieDetailsPresenter;
    private DetailsOverviewRow mDetailsOverviewRow;

    public static MovieDetailsFragment newInstance(Movie movie){
        Bundle args = new Bundle();
        args.putParcelable(Movie.class.getSimpleName(), movie);
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        App.instance().appComponent().inject(this);
        if(getArguments() == null || !getArguments().containsKey(Movie.class.getSimpleName())){
            throw new RuntimeException("An Movie is necessary for MovieDetailsFragment");
        }

        mMovie = getArguments().getParcelable(Movie.class.getSimpleName());
        setUpAdapter();
        setUpDetailsOverviewRow();
    }

    /**
     * Sets up the adapter for this Fragment
     */
    private void setUpAdapter() {
        mFullWidthMovieDetailsPresenter = new CustomMovieDetailsPresenter(new MovieDetailsDescriptionPresenter(),
                new DetailsOverviewLogoPresenter());

        FullWidthDetailsOverviewSharedElementHelper helper = new FullWidthDetailsOverviewSharedElementHelper();
        helper.setSharedElementEnterTransition(getActivity(), TRANSITION_NAME);
        mFullWidthMovieDetailsPresenter.setListener(helper);

        mFullWidthMovieDetailsPresenter.setParticipatingEntranceTransition(false);
        ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
        classPresenterSelector.addClassPresenter(DetailsOverviewRow.class, mFullWidthMovieDetailsPresenter);
        classPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mAdtapter = new ArrayObjectAdapter(classPresenterSelector);

        setAdapter(mAdtapter);

    }

    /**
     * Sets up the details overview rows
     */
    private void setUpDetailsOverviewRow() {
        mDetailsOverviewRow = new DetailsOverviewRow(new MovieDetails());
        mAdtapter.add(mDetailsOverviewRow);
        loadImage(HttpClientModule.POSTER_URL + mMovie.getPosterPath());
        fetchMovieDetails();
    }

    private void fetchMovieDetails(){
        mDbAPI.getMovieDetails(mMovie.getId(), Config.API_KEY_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::bindMovieDetails, e -> {
                    Timber.e(e, "Error fetching data: %s", e.getMessage());
                });
    }

    private void bindMovieDetails(MovieDetails movieDetails){
        this.mMovieDetails = movieDetails;
        mDetailsOverviewRow.setItem(this.mMovieDetails);
    }

    private SimpleTarget<GlideDrawable> mGlideDrawableSimpleTarget = new SimpleTarget<GlideDrawable>() {
        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            mDetailsOverviewRow.setImageDrawable(resource);
        }
    };

    private void loadImage(String url){
        Glide.with(getActivity())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        changePalette(((GlideBitmapDrawable)resource).getBitmap());
                        return false;
                    }
                })
                .into(mGlideDrawableSimpleTarget);
    }

    private void changePalette(Bitmap bitmap){
        Palette.from(bitmap).generate(this);
    }

    @Override
    public void onGenerated(Palette palette) {
        PaletteColors colors = PaletteUtils.getPaletteColors(palette);
        mFullWidthMovieDetailsPresenter.setActionsBackgroundColor(colors.getStatusBarColor());
        mFullWidthMovieDetailsPresenter.setBackgroundColor(colors.getToolbarBackgroundColor());

        if(mMovieDetails != null){
            mMovieDetails.setPaletteColors(colors);
        }
        notifyDetailsChanged();
    }

    private void notifyDetailsChanged(){
        mDetailsOverviewRow.setItem(mMovieDetails);
        int index = mAdtapter.indexOf(mDetailsOverviewRow);
        mAdtapter.notifyArrayItemRangeChanged(index, 1);
    }
}
