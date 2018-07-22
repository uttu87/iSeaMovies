package com.androidtv.iseasoft.iSeaMovies.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import com.androidtv.iseasoft.iSeaMovies.App;
import com.androidtv.iseasoft.iSeaMovies.Config;
import com.androidtv.iseasoft.iSeaMovies.R;
import com.androidtv.iseasoft.iSeaMovies.dagger.modules.HttpClientModule;
import com.androidtv.iseasoft.iSeaMovies.data.Api.TheMovieDbAPI;
import com.androidtv.iseasoft.iSeaMovies.data.models.Movie;
import com.androidtv.iseasoft.iSeaMovies.data.models.MovieResponse;
import com.androidtv.iseasoft.iSeaMovies.ui.base.GlideBackgroundManager;
import com.androidtv.iseasoft.iSeaMovies.ui.details.MovieDetailsActivity;
import com.androidtv.iseasoft.iSeaMovies.ui.details.MovieDetailsFragment;
import com.androidtv.iseasoft.iSeaMovies.ui.movies.MovieCardView;
import com.androidtv.iseasoft.iSeaMovies.ui.movies.MoviePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by <a href="mailto:marcus@gabilheri.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 10/8/16.
 */

public class MainFragment extends BrowseFragment implements OnItemViewSelectedListener,
        OnItemViewClickedListener{

    @Inject
    TheMovieDbAPI mDbAPI;

    private GlideBackgroundManager mBackgroundManager;

    private static final int NOW_PLAYING = 0;
    private static final int TOP_RATED = 1;
    private static final int POPULAR = 2;
    private static final int UPCOMING = 3;

    SparseArray<MovieRow> mRows;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        App.instance().appComponent().inject(this);

        // The background manager allows us to manage a dimmed background that does not interfere with the rows
        // It is the preferred way to set the background of a fragment
        mBackgroundManager = new GlideBackgroundManager(getActivity());

        // Add code here for initialization of HeadersFragment and badge title
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.primary_transparent));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.powered_by));


        createDataRows();
        createRows();
        prepareEntranceTransition();
        fetchNowPlayingMovies();
        fetchTopRatedMovies();
        fetchPopularMovies();
        fetchUpcomingMovies();
    }

    /**
     * Creates the data rows objects
     */
    private void createDataRows() {
        mRows = new SparseArray<>();
        MoviePresenter moviePresenter = new MoviePresenter();
        mRows.put(NOW_PLAYING, new MovieRow()
                .setId(NOW_PLAYING)
                .setAdapter(new ArrayObjectAdapter(moviePresenter))
                .setTitle("Now Playing")
                .setPage(1)
        );
        mRows.put(TOP_RATED, new MovieRow()
                .setId(TOP_RATED)
                .setAdapter(new ArrayObjectAdapter(moviePresenter))
                .setTitle("Top Rated")
                .setPage(1)
        );
        mRows.put(POPULAR, new MovieRow()
                .setId(POPULAR)
                .setAdapter(new ArrayObjectAdapter(moviePresenter))
                .setTitle("Popular")
                .setPage(1)
        );
        mRows.put(UPCOMING, new MovieRow()
                .setId(UPCOMING)
                .setAdapter(new ArrayObjectAdapter(moviePresenter))
                .setTitle("Upcoming")
                .setPage(1)
        );
    }

    /**
     * Creates the rows and sets up the adapter of the fragment
     */
    private void createRows() {
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        for(int i = 0; i < mRows.size(); i++){
            MovieRow row = mRows.get(i);

            HeaderItem headerItem = new HeaderItem(row.getId(), row.getTitle());
            ListRow listRow = new ListRow(headerItem, row.getAdapter());
            rowsAdapter.add(listRow);
        }

        setAdapter(rowsAdapter);
        setOnItemViewSelectedListener(this);
        setOnItemViewClickedListener(this);
    }

    /**
     * Fetches now playing movies from TMDB
     */
    private void fetchNowPlayingMovies() {
        mDbAPI.getNowPlayingMovies(Config.API_KEY_URL, mRows.get(NOW_PLAYING).getPage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    bindMovieResponse(response, NOW_PLAYING);
                    startEntranceTransition();
                }, e -> {
                    Timber.e(e, "Error fetching now playing movies: %s", e.getMessage());
                });
    }

    /**
     * Fetches the popular movies from TMDB
     */
    private void fetchPopularMovies() {
        mDbAPI.getPopularMovies(Config.API_KEY_URL, mRows.get(POPULAR).getPage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    bindMovieResponse(response, POPULAR);
                    startEntranceTransition();
                }, e -> {
                    Timber.e(e, "Error fetching popular movies: %s", e.getMessage());
                });
    }

    /**
     * Fetches the upcoming movies from TMDB
     */
    private void fetchUpcomingMovies() {
        mDbAPI.getUpcomingMovies(Config.API_KEY_URL, mRows.get(UPCOMING).getPage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    bindMovieResponse(response, UPCOMING);
                    startEntranceTransition();
                }, e -> {
                    Timber.e(e, "Error fetching upcoming movies: %s", e.getMessage());
                });
    }

    /**
     * Fetches the top rated movies from TMDB
     */
    private void fetchTopRatedMovies() {
        mDbAPI.getTopRatedMovies(Config.API_KEY_URL, mRows.get(TOP_RATED).getPage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    bindMovieResponse(response, TOP_RATED);
                    startEntranceTransition();
                }, e -> {
                    Timber.e(e, "Error fetching top rated movies: %s", e.getMessage());
                });
    }

    /**
     * Binds a movie response to it's adapter
     * @param response
     *      The response from TMDB API
     * @param id
     *      The ID / position of the row
     */
    private void bindMovieResponse(MovieResponse response, int id) {
        MovieRow row = mRows.get(id);
        row.setPage(row.getPage() + 1);
        for(Movie m : response.getResults()) {
            if (m.getPosterPath() != null) { // Avoid showing movie without posters
                row.getAdapter().add(m);
            }
        }
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if(item instanceof Movie){
            Movie movie = (Movie) item;

            if(movie.getBackdropPath() != null){
                mBackgroundManager.loadImage(HttpClientModule.BACKDROP_URL + movie.getBackdropPath());
            } else {
                mBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.material_bg));
            }
        }
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if(item instanceof Movie){
            Movie movie = (Movie)item;
            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
            intent.putExtra(Movie.class.getSimpleName(), movie);

            if(itemViewHolder.view instanceof MovieCardView){
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((MovieCardView)itemViewHolder.view).getPosterIV(),
                        MovieDetailsFragment.TRANSITION_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            } else {
                startActivity(intent);
            }
        }
    }
}
