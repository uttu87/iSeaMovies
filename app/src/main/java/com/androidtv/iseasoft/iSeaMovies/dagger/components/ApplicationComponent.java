package com.androidtv.iseasoft.iSeaMovies.dagger.components;


import com.androidtv.iseasoft.iSeaMovies.App;
import com.androidtv.iseasoft.iSeaMovies.dagger.AppScope;
import com.androidtv.iseasoft.iSeaMovies.dagger.modules.ApplicationModule;
import com.androidtv.iseasoft.iSeaMovies.dagger.modules.HttpClientModule;
import com.androidtv.iseasoft.iSeaMovies.ui.details.MovieDetailsFragment;
import com.androidtv.iseasoft.iSeaMovies.ui.main.MainFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by <a href="mailto:marcus@gabilheri.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/4/16.
 */
@AppScope
@Singleton
@Component(modules = {
        ApplicationModule.class,
        HttpClientModule.class,
})
public interface ApplicationComponent {

    void inject(App app);
    void inject(MainFragment mainFragment);
    void inject(MovieDetailsFragment detailsFragment);
}
